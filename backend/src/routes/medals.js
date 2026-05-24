'use strict';

const express = require('express');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');

const router = express.Router();

router.get('/', authRequired, async (req, res, next) => {
  try {
    const all = await knex('medals').orderBy('code');
    const unlocked = await knex('user_medals').where({ user_id: req.userId }).pluck('medal_id');
    const unlockedSet = new Set(unlocked);
    const items = all.map((m) => ({
      id: m.id,
      code: m.code,
      name: m.name,
      icon: m.icon,
      description: m.description,
      unlocked: unlockedSet.has(m.id),
    }));
    res.json({ items });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
