'use strict';

const express = require('express');
const { z } = require('zod');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { validate, validateQuery } = require('../middleware/validate');
const { serializeMessage } = require('../services/serializers');
const { broadcastToMatch } = require('../ws/chat');
const fcm = require('../services/fcm');

const router = express.Router({ mergeParams: true });

async function ensureMatchMember(matchId, userId) {
  const match = await knex('matches').where({ id: matchId }).first();
  if (!match) return null;
  if (match.user_a !== userId && match.user_b !== userId) return null;
  return match;
}

const listQuerySchema = z.object({
  limit: z.coerce.number().int().min(1).max(100).optional().default(50),
  before: z.string().optional(),
});

router.get('/', authRequired, validateQuery(listQuerySchema), async (req, res, next) => {
  try {
    const match = await ensureMatchMember(req.params.id, req.userId);
    if (!match) {
      return res.status(404).json({ error: { code: 'not_found', message: 'Match not found' } });
    }
    const { limit, before } = req.query;
    let q = knex('messages').where({ match_id: match.id }).orderBy('created_at', 'desc').limit(limit);
    if (before) {
      try {
        const cursor = Buffer.from(before, 'base64').toString('utf8');
        q = q.andWhere('created_at', '<', cursor);
      } catch {
        /* ignore bad cursor */
      }
    }
    const rows = await q;
    const items = rows.map(serializeMessage);
    let nextCursor = null;
    if (rows.length === limit) {
      const lastTs = rows[rows.length - 1].created_at;
      nextCursor = Buffer.from(new Date(lastTs).toISOString()).toString('base64');
    }
    res.json({ items, nextCursor });
  } catch (err) {
    next(err);
  }
});

const createSchema = z.discriminatedUnion('kind', [
  z.object({ kind: z.literal('text'), text: z.string().min(1).max(2000) }),
  z.object({ kind: z.literal('image'), imageUrl: z.string().url() }),
  z.object({ kind: z.literal('workout_invite'), payload: z.record(z.any()) }),
]).or(z.object({ text: z.string().min(1).max(2000) }).transform((v) => ({ kind: 'text', ...v })));

router.post('/', authRequired, validate(createSchema), async (req, res, next) => {
  try {
    const match = await ensureMatchMember(req.params.id, req.userId);
    if (!match) {
      return res.status(404).json({ error: { code: 'not_found', message: 'Match not found' } });
    }
    if (match.is_unmatched) {
      return res.status(400).json({ error: { code: 'unmatched', message: 'Match has been unmatched' } });
    }
    const { kind, text, imageUrl, payload } = req.body;
    const [row] = await knex('messages')
      .insert({
        match_id: match.id,
        sender_id: req.userId,
        kind,
        text: text || null,
        image_url: imageUrl || null,
        payload: payload ? JSON.stringify(payload) : null,
      })
      .returning('*');
    await knex('matches').where({ id: match.id }).update({ last_activity_at: knex.fn.now() });
    const out = serializeMessage(row);
    broadcastToMatch(match.id, { type: 'message', data: out });

    // Push notification to the other participant
    const otherId = match.user_a === req.userId ? match.user_b : match.user_a;
    const sender = await knex('users').where({ id: req.userId }).first();
    const other  = await knex('users').where({ id: otherId }).first();
    if (other?.fcm_token) {
      fcm.send(other.fcm_token, sender?.name ?? 'GymBuddy', out.text ?? '📷 Image', { type: 'message' });
    }

    res.status(201).json(out);
  } catch (err) {
    next(err);
  }
});

module.exports = router;
