'use strict';

const express = require('express');
const { z } = require('zod');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { validate } = require('../middleware/validate');
const { serializeUser } = require('../services/serializers');

const router = express.Router();

router.get('/', authRequired, async (req, res, next) => {
  try {
    const rows = await knex('friends').where({ user_a: req.userId }).orWhere({ user_b: req.userId });
    const friendIds = rows.map((r) => (r.user_a === req.userId ? r.user_b : r.user_a));
    if (!friendIds.length) return res.json({ items: [] });
    const userRows = await knex('users').whereIn('id', friendIds);
    const items = [];
    for (const u of userRows) items.push(await serializeUser(u));
    res.json({ items });
  } catch (err) {
    next(err);
  }
});

const requestSchema = z.object({ userId: z.string().uuid() });

router.post('/requests', authRequired, validate(requestSchema), async (req, res, next) => {
  try {
    const { userId } = req.body;
    if (userId === req.userId) {
      return res.status(400).json({ error: { code: 'invalid_target', message: 'Cannot friend self' } });
    }
    const target = await knex('users').where({ id: userId }).first();
    if (!target) return res.status(404).json({ error: { code: 'not_found', message: 'User not found' } });

    // already friends?
    const friend = await knex('friends')
      .where(function () {
        this.where({ user_a: req.userId, user_b: userId })
          .orWhere({ user_a: userId, user_b: req.userId });
      })
      .first();
    if (friend) return res.status(409).json({ error: { code: 'already_friends', message: 'Already friends' } });

    const [row] = await knex('friend_requests')
      .insert({ from_user_id: req.userId, to_user_id: userId })
      .onConflict(['from_user_id', 'to_user_id'])
      .merge({ status: 'pending' })
      .returning('*');
    res.status(201).json({ id: row.id, status: row.status, toUserId: row.to_user_id, fromUserId: row.from_user_id });
  } catch (err) {
    next(err);
  }
});

router.get('/requests', authRequired, async (req, res, next) => {
  try {
    const incoming = await knex('friend_requests')
      .where({ to_user_id: req.userId, status: 'pending' })
      .orderBy('created_at', 'desc');
    const outgoing = await knex('friend_requests')
      .where({ from_user_id: req.userId, status: 'pending' })
      .orderBy('created_at', 'desc');
    const formatRequest = async (r, direction) => {
      const otherId = direction === 'incoming' ? r.from_user_id : r.to_user_id;
      const other = await knex('users').where({ id: otherId }).first();
      return {
        id: r.id,
        direction,
        user: await serializeUser(other),
        createdAt: r.created_at,
      };
    };
    const incomingItems = [];
    for (const r of incoming) incomingItems.push(await formatRequest(r, 'incoming'));
    const outgoingItems = [];
    for (const r of outgoing) outgoingItems.push(await formatRequest(r, 'outgoing'));
    res.json({ incoming: incomingItems, outgoing: outgoingItems });
  } catch (err) {
    next(err);
  }
});

router.post('/requests/:id/accept', authRequired, async (req, res, next) => {
  try {
    const row = await knex('friend_requests')
      .where({ id: req.params.id, to_user_id: req.userId, status: 'pending' })
      .first();
    if (!row) {
      return res.status(404).json({ error: { code: 'not_found', message: 'Friend request not found' } });
    }
    await knex.transaction(async (trx) => {
      await trx('friend_requests').where({ id: row.id }).update({ status: 'accepted' });
      await trx('friends')
        .insert({ user_a: row.from_user_id, user_b: row.to_user_id })
        .onConflict(['user_a', 'user_b'])
        .ignore();
    });
    res.json({ ok: true });
  } catch (err) {
    next(err);
  }
});

router.post('/requests/:id/decline', authRequired, async (req, res, next) => {
  try {
    const row = await knex('friend_requests')
      .where({ id: req.params.id, to_user_id: req.userId, status: 'pending' })
      .first();
    if (!row) {
      return res.status(404).json({ error: { code: 'not_found', message: 'Friend request not found' } });
    }
    await knex('friend_requests').where({ id: row.id }).update({ status: 'declined' });
    res.json({ ok: true });
  } catch (err) {
    next(err);
  }
});

router.delete('/:id', authRequired, async (req, res, next) => {
  try {
    const otherId = req.params.id;
    const deleted = await knex('friends')
      .where(function () {
        this.where({ user_a: req.userId, user_b: otherId })
          .orWhere({ user_a: otherId, user_b: req.userId });
      })
      .del();
    if (!deleted) {
      return res.status(404).json({ error: { code: 'not_found', message: 'Friend not found' } });
    }
    res.json({ ok: true });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
