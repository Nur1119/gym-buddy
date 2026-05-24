'use strict';

const express = require('express');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');

const router = express.Router();

router.get('/', authRequired, async (req, res, next) => {
  try {
    const rows = await knex('quests as q')
      .leftJoin('user_quests as uq', function () {
        this.on('uq.quest_id', 'q.id').andOn('uq.user_id', knex.raw('?', [req.userId]));
      })
      .select(
        'q.id',
        'q.code',
        'q.text',
        'q.total',
        'q.xp',
        'q.period',
        'uq.progress',
        'uq.completed'
      );
    const items = rows.map((r) => ({
      id: r.id,
      code: r.code,
      text: r.text,
      total: r.total,
      progress: r.progress || 0,
      xp: r.xp,
      period: r.period,
      completed: !!r.completed,
    }));
    res.json({ items });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
