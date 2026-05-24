'use strict';

const express = require('express');
const { z } = require('zod');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { validate } = require('../middleware/validate');
const { serializeRoutine } = require('../services/serializers');

const router = express.Router();

const exerciseSchema = z.object({
  exerciseId: z.string().uuid(),
  sets: z.number().int().min(1).max(20).optional().default(3),
  targetReps: z.number().int().min(1).max(100).optional().default(10),
  restSec: z.number().int().min(0).max(900).optional().default(90),
});

const createSchema = z.object({
  name: z.string().min(1),
  color: z.string().optional(),
  estimatedDurationMin: z.number().int().min(1).max(600).optional(),
  exercises: z.array(exerciseSchema).min(1),
});

const updateSchema = createSchema.partial();

router.get('/', authRequired, async (req, res, next) => {
  try {
    const rows = await knex('routines').where({ owner_id: req.userId }).orderBy('created_at', 'desc');
    const items = [];
    for (const r of rows) items.push(await serializeRoutine(r));
    res.json({ items });
  } catch (err) {
    next(err);
  }
});

router.post('/', authRequired, validate(createSchema), async (req, res, next) => {
  try {
    const { name, color, estimatedDurationMin, exercises } = req.body;
    const [row] = await knex('routines')
      .insert({
        name,
        owner_id: req.userId,
        color: color || '#7C5CFF',
        estimated_duration_min: estimatedDurationMin || 60,
      })
      .returning('*');
    const rxRows = exercises.map((e, i) => ({
      routine_id: row.id,
      exercise_id: e.exerciseId,
      position: i,
      sets: e.sets,
      target_reps: e.targetReps,
      rest_sec: e.restSec,
    }));
    await knex('routine_exercises').insert(rxRows);
    res.status(201).json(await serializeRoutine(row));
  } catch (err) {
    next(err);
  }
});

router.get('/:id', authRequired, async (req, res, next) => {
  try {
    const row = await knex('routines').where({ id: req.params.id, owner_id: req.userId }).first();
    if (!row) return res.status(404).json({ error: { code: 'not_found', message: 'Routine not found' } });
    res.json(await serializeRoutine(row));
  } catch (err) {
    next(err);
  }
});

router.patch('/:id', authRequired, validate(updateSchema), async (req, res, next) => {
  try {
    const row = await knex('routines').where({ id: req.params.id, owner_id: req.userId }).first();
    if (!row) return res.status(404).json({ error: { code: 'not_found', message: 'Routine not found' } });
    const { name, color, estimatedDurationMin, exercises } = req.body;
    const update = {};
    if (name !== undefined) update.name = name;
    if (color !== undefined) update.color = color;
    if (estimatedDurationMin !== undefined) update.estimated_duration_min = estimatedDurationMin;
    if (Object.keys(update).length) await knex('routines').where({ id: row.id }).update(update);
    if (exercises) {
      await knex('routine_exercises').where({ routine_id: row.id }).del();
      const rxRows = exercises.map((e, i) => ({
        routine_id: row.id,
        exercise_id: e.exerciseId,
        position: i,
        sets: e.sets,
        target_reps: e.targetReps,
        rest_sec: e.restSec,
      }));
      await knex('routine_exercises').insert(rxRows);
    }
    const updated = await knex('routines').where({ id: row.id }).first();
    res.json(await serializeRoutine(updated));
  } catch (err) {
    next(err);
  }
});

router.delete('/:id', authRequired, async (req, res, next) => {
  try {
    const row = await knex('routines').where({ id: req.params.id, owner_id: req.userId }).first();
    if (!row) return res.status(404).json({ error: { code: 'not_found', message: 'Routine not found' } });
    await knex('routines').where({ id: row.id }).del();
    res.json({ ok: true });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
