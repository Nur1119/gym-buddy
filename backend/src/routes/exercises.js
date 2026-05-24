'use strict';

const express = require('express');
const { z } = require('zod');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { validate, validateQuery } = require('../middleware/validate');
const { serializeExercise } = require('../services/serializers');

const router = express.Router();

const listQuerySchema = z.object({
  muscle: z.string().optional(),
  search: z.string().optional(),
  limit: z.coerce.number().int().min(1).max(200).optional().default(100),
});

router.get('/', authRequired, validateQuery(listQuerySchema), async (req, res, next) => {
  try {
    const { muscle, search, limit } = req.query;
    const q = knex('exercises').where(function () {
      this.whereNull('owner_id').orWhere({ owner_id: req.userId });
    });
    if (muscle && muscle !== 'all') q.andWhere({ muscle });
    if (search) q.andWhere('name', 'ilike', `%${search}%`);
    const rows = await q.orderBy('name', 'asc').limit(limit);
    res.json({ items: rows.map(serializeExercise) });
  } catch (err) {
    next(err);
  }
});

const createSchema = z.object({
  name: z.string().min(1),
  muscle: z.enum(['Chest', 'Back', 'Legs', 'Shoulders', 'Arms', 'Core', 'Cardio']),
  equipment: z.enum(['Barbell', 'Dumbbell', 'Machine', 'Cable', 'Bodyweight']),
  notes: z.string().optional(),
  icon: z.string().optional(),
});

router.post('/', authRequired, validate(createSchema), async (req, res, next) => {
  try {
    const { name, muscle, equipment, notes, icon } = req.body;
    const [row] = await knex('exercises')
      .insert({
        name,
        muscle,
        equipment,
        notes: notes || null,
        icon: icon || '💪',
        is_custom: true,
        owner_id: req.userId,
      })
      .returning('*');
    res.status(201).json(serializeExercise(row));
  } catch (err) {
    next(err);
  }
});

router.get('/:id', authRequired, async (req, res, next) => {
  try {
    const row = await knex('exercises')
      .where({ id: req.params.id })
      .andWhere(function () {
        this.whereNull('owner_id').orWhere({ owner_id: req.userId });
      })
      .first();
    if (!row) return res.status(404).json({ error: { code: 'not_found', message: 'Exercise not found' } });
    res.json(serializeExercise(row));
  } catch (err) {
    next(err);
  }
});

module.exports = router;
