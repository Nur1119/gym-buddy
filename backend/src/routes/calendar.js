'use strict';

const express = require('express');
const { z } = require('zod');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { validateQuery } = require('../middleware/validate');

const router = express.Router();

const querySchema = z.object({
  month: z.string().regex(/^\d{4}-\d{2}$/).optional(),
});

router.get('/', authRequired, validateQuery(querySchema), async (req, res, next) => {
  try {
    const month = req.query.month || new Date().toISOString().slice(0, 7);
    const [yearStr, monthStr] = month.split('-');
    const year = parseInt(yearStr, 10);
    const mIdx = parseInt(monthStr, 10) - 1;
    const start = new Date(Date.UTC(year, mIdx, 1));
    const end = new Date(Date.UTC(year, mIdx + 1, 1));

    const completed = await knex('workouts')
      .where({ user_id: req.userId })
      .whereNotNull('finished_at')
      .andWhere('finished_at', '>=', start.toISOString())
      .andWhere('finished_at', '<', end.toISOString());

    const planned = await knex('workouts')
      .where({ user_id: req.userId })
      .whereNotNull('planned_for')
      .andWhere('planned_for', '>=', start.toISOString())
      .andWhere('planned_for', '<', end.toISOString());

    const formatDay = (ts) => new Date(ts).toISOString().slice(0, 10);

    res.json({
      month,
      completed: completed.map((w) => ({
        id: w.id,
        date: formatDay(w.finished_at),
        name: w.name,
        totalVolumeKg: w.total_volume_kg,
        xpAwarded: w.xp_awarded,
      })),
      planned: planned.map((w) => ({
        id: w.id,
        date: formatDay(w.planned_for),
        name: w.name,
      })),
    });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
