'use strict';

const express = require('express');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { serializeMatch } = require('../services/serializers');

const router = express.Router();

router.get('/', authRequired, async (req, res, next) => {
  try {
    const rows = await knex('matches')
      .where(function () {
        this.where({ user_a: req.userId }).orWhere({ user_b: req.userId });
      })
      .andWhere({ is_unmatched: false })
      .orderBy('last_activity_at', 'desc');
    const items = [];
    for (const r of rows) items.push(await serializeMatch(r, req.userId));
    res.json({ items });
  } catch (err) {
    next(err);
  }
});

router.delete('/:id', authRequired, async (req, res, next) => {
  try {
    const row = await knex('matches').where({ id: req.params.id }).first();
    if (!row || (row.user_a !== req.userId && row.user_b !== req.userId)) {
      return res.status(404).json({ error: { code: 'not_found', message: 'Match not found' } });
    }
    await knex('matches').where({ id: row.id }).update({ is_unmatched: true, unmatched_by: req.userId });
    res.json({ ok: true });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
