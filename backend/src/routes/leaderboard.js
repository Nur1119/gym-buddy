'use strict';

const express = require('express');
const { z } = require('zod');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { validateQuery } = require('../middleware/validate');

const router = express.Router();

const querySchema = z.object({
  scope: z.enum(['global', 'friends']).optional().default('global'),
  period: z.enum(['week', 'month', 'all']).optional().default('all'),
  limit: z.coerce.number().int().min(1).max(100).optional().default(20),
});

router.get('/', authRequired, validateQuery(querySchema), async (req, res, next) => {
  try {
    const { scope, limit } = req.query;
    // Note: period filtering would require an xp_events table; here we use stat_total_xp.
    let userIds = null;
    if (scope === 'friends') {
      const friends = await knex('friends')
        .where({ user_a: req.userId })
        .orWhere({ user_b: req.userId });
      userIds = friends.map((r) => (r.user_a === req.userId ? r.user_b : r.user_a));
      userIds.push(req.userId);
    }
    let q = knex('users').select('id', 'name', 'username', 'stat_total_xp', 'stat_level', 'color');
    if (userIds) q = q.whereIn('id', userIds);
    const rows = await q.orderBy('stat_total_xp', 'desc').limit(limit);
    const items = rows.map((r, i) => ({
      rank: i + 1,
      userId: r.id,
      name: r.name,
      username: r.username,
      xp: r.stat_total_xp,
      level: r.stat_level,
      color: r.color,
      isMe: r.id === req.userId,
    }));
    res.json({ items, scope: req.query.scope, period: req.query.period });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
