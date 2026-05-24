'use strict';

const express = require('express');
const { z } = require('zod');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { validate, validateQuery } = require('../middleware/validate');
const { serializeUser, serializeMatch } = require('../services/serializers');
const { recordSwipe } = require('../services/matching');

const router = express.Router();

const feedQuerySchema = z.object({
  limit: z.coerce.number().int().min(1).max(50).optional().default(10),
});

router.get('/feed', authRequired, validateQuery(feedQuerySchema), async (req, res, next) => {
  try {
    const { limit } = req.query;
    // Exclude self and anyone already swiped on
    const swiped = await knex('swipes').where({ user_id: req.userId }).pluck('target_user_id');
    const filters = await knex('user_filters').where({ user_id: req.userId }).first();

    const q = knex('users').whereNot({ id: req.userId });
    if (swiped.length) q.whereNotIn('id', swiped);
    if (filters) {
      if (filters.age_min) q.where('age', '>=', filters.age_min);
      if (filters.age_max) q.where('age', '<=', filters.age_max);
      if (filters.goals && filters.goals.length) q.whereIn('goal', filters.goals);
      if (filters.levels && filters.levels.length) q.whereIn('level', filters.levels);
    }
    const rows = await q.orderBy('stat_total_xp', 'desc').limit(limit);
    const items = [];
    for (const row of rows) items.push(await serializeUser(row));
    res.json({ items });
  } catch (err) {
    next(err);
  }
});

router.get('/filters', authRequired, async (req, res, next) => {
  try {
    let row = await knex('user_filters').where({ user_id: req.userId }).first();
    if (!row) {
      // Create default
      await knex('user_filters').insert({ user_id: req.userId });
      row = await knex('user_filters').where({ user_id: req.userId }).first();
    }
    res.json({
      ageMin: row.age_min,
      ageMax: row.age_max,
      maxDistanceKm: row.max_distance_km,
      goals: row.goals || [],
      levels: row.levels || [],
      scheduleDays: row.schedule_days || [],
    });
  } catch (err) {
    next(err);
  }
});

const filtersUpdateSchema = z.object({
  ageMin: z.number().int().min(13).max(120).optional(),
  ageMax: z.number().int().min(13).max(120).optional(),
  maxDistanceKm: z.number().int().min(1).max(500).optional(),
  goals: z.array(z.string()).optional(),
  levels: z.array(z.string()).optional(),
  scheduleDays: z.array(z.number().int().min(0).max(6)).optional(),
});

router.put('/filters', authRequired, validate(filtersUpdateSchema), async (req, res, next) => {
  try {
    const { ageMin, ageMax, maxDistanceKm, goals, levels, scheduleDays } = req.body;
    const exists = await knex('user_filters').where({ user_id: req.userId }).first();
    const update = {
      ...(ageMin !== undefined && { age_min: ageMin }),
      ...(ageMax !== undefined && { age_max: ageMax }),
      ...(maxDistanceKm !== undefined && { max_distance_km: maxDistanceKm }),
      ...(goals !== undefined && { goals }),
      ...(levels !== undefined && { levels }),
      ...(scheduleDays !== undefined && { schedule_days: scheduleDays }),
      updated_at: knex.fn.now(),
    };
    if (exists) {
      await knex('user_filters').where({ user_id: req.userId }).update(update);
    } else {
      await knex('user_filters').insert({ user_id: req.userId, ...update });
    }
    const row = await knex('user_filters').where({ user_id: req.userId }).first();
    res.json({
      ageMin: row.age_min,
      ageMax: row.age_max,
      maxDistanceKm: row.max_distance_km,
      goals: row.goals || [],
      levels: row.levels || [],
      scheduleDays: row.schedule_days || [],
    });
  } catch (err) {
    next(err);
  }
});

const swipeSchema = z.object({
  targetUserId: z.string().uuid(),
  direction: z.enum(['like', 'pass', 'superlike']),
});

router.post('/swipe', authRequired, validate(swipeSchema), async (req, res, next) => {
  try {
    const { targetUserId, direction } = req.body;
    if (targetUserId === req.userId) {
      return res.status(400).json({
        error: { code: 'invalid_target', message: 'Cannot swipe on self' },
      });
    }
    const target = await knex('users').where({ id: targetUserId }).first();
    if (!target) {
      return res.status(404).json({ error: { code: 'not_found', message: 'Target user not found' } });
    }
    const { matched, match } = await recordSwipe(req.userId, targetUserId, direction);
    if (!matched) return res.json({ matched: false });
    const serialized = await serializeMatch(match, req.userId);
    res.json({ matched: true, match: serialized });
  } catch (err) {
    next(err);
  }
});

router.post('/rewind', authRequired, async (req, res, next) => {
  try {
    const last = await knex('swipes')
      .where({ user_id: req.userId })
      .orderBy('created_at', 'desc')
      .first();
    if (!last) {
      return res.status(404).json({ error: { code: 'no_swipes', message: 'No swipes to rewind' } });
    }
    await knex('swipes').where({ id: last.id }).del();
    // If a match was created by this swipe and the other side liked us too, we'd also need
    // to clean it up. For simplicity we remove a match if it now lacks both swipes.
    const reciprocal = await knex('swipes')
      .where({ user_id: last.target_user_id, target_user_id: req.userId })
      .whereIn('direction', ['like', 'superlike'])
      .first();
    if (!reciprocal) {
      await knex('matches')
        .where(function () {
          this.where({ user_a: req.userId, user_b: last.target_user_id })
            .orWhere({ user_a: last.target_user_id, user_b: req.userId });
        })
        .del();
    }
    const targetRow = await knex('users').where({ id: last.target_user_id }).first();
    res.json({ user: await serializeUser(targetRow) });
  } catch (err) {
    next(err);
  }
});

router.post('/boost', authRequired, async (req, res) => {
  const expires = new Date(Date.now() + 30 * 60 * 1000).toISOString();
  // No persistence; this is a stub for premium boost.
  res.json({ expiresAt: expires });
});

module.exports = router;
