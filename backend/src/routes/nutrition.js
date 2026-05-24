'use strict';

const express = require('express');
const { z } = require('zod');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { validate, validateQuery } = require('../middleware/validate');

const router = express.Router();

const daySchema = z.object({
  date: z.string().regex(/^\d{4}-\d{2}-\d{2}$/).optional(),
});

router.get('/day', authRequired, validateQuery(daySchema), async (req, res, next) => {
  try {
    const date = req.query.date || new Date().toISOString().slice(0, 10);
    const rows = await knex('meals')
      .where({ user_id: req.userId, date })
      .orderBy('time', 'asc');
    const totals = rows.reduce(
      (a, r) => ({
        kcal: a.kcal + (r.total_kcal || 0),
        protein: a.protein + (r.total_protein || 0),
        carbs: a.carbs + (r.total_carbs || 0),
        fats: a.fats + (r.total_fats || 0),
      }),
      { kcal: 0, protein: 0, carbs: 0, fats: 0 }
    );
    res.json({
      date,
      meals: rows.map((r) => ({
        id: r.id,
        kind: r.kind,
        time: r.time,
        items: r.items,
        totalKcal: r.total_kcal,
        totalProtein: r.total_protein,
        totalCarbs: r.total_carbs,
        totalFats: r.total_fats,
      })),
      totals,
    });
  } catch (err) {
    next(err);
  }
});

const itemSchema = z.object({
  name: z.string().min(1),
  kcal: z.number().min(0),
  protein: z.number().min(0),
  carbs: z.number().min(0),
  fats: z.number().min(0),
});

const createSchema = z.object({
  kind: z.enum(['breakfast', 'lunch', 'dinner', 'snack']),
  time: z.string().regex(/^\d{2}:\d{2}$/).optional(),
  date: z.string().regex(/^\d{4}-\d{2}-\d{2}$/).optional(),
  items: z.array(itemSchema).min(1),
});

router.post('/meals', authRequired, validate(createSchema), async (req, res, next) => {
  try {
    const { kind, time, date, items } = req.body;
    const totals = items.reduce(
      (a, i) => ({
        kcal: a.kcal + i.kcal,
        protein: a.protein + i.protein,
        carbs: a.carbs + i.carbs,
        fats: a.fats + i.fats,
      }),
      { kcal: 0, protein: 0, carbs: 0, fats: 0 }
    );
    const [row] = await knex('meals')
      .insert({
        user_id: req.userId,
        date: date || new Date().toISOString().slice(0, 10),
        time: time || '12:00',
        kind,
        items: JSON.stringify(items),
        total_kcal: Math.round(totals.kcal),
        total_protein: Math.round(totals.protein),
        total_carbs: Math.round(totals.carbs),
        total_fats: Math.round(totals.fats),
      })
      .returning('*');
    res.status(201).json({
      id: row.id,
      kind: row.kind,
      time: row.time,
      date: row.date,
      items: row.items,
      totalKcal: row.total_kcal,
      totalProtein: row.total_protein,
      totalCarbs: row.total_carbs,
      totalFats: row.total_fats,
    });
  } catch (err) {
    next(err);
  }
});

router.delete('/meals/:id', authRequired, async (req, res, next) => {
  try {
    const deleted = await knex('meals')
      .where({ id: req.params.id, user_id: req.userId })
      .del();
    if (!deleted) return res.status(404).json({ error: { code: 'not_found', message: 'Meal not found' } });
    res.json({ ok: true });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
