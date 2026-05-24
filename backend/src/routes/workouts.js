'use strict';

const express = require('express');
const { z } = require('zod');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { validate } = require('../middleware/validate');
const { serializeWorkout } = require('../services/serializers');
const { computeWorkoutXp, awardXp } = require('../services/xp');

const router = express.Router();

const setSchema = z.object({
  weight: z.number().min(0).max(1000).optional().default(0),
  reps: z.number().int().min(0).max(1000).optional().default(0),
  completed: z.boolean().optional().default(false),
  restSec: z.number().int().min(0).max(3600).optional().default(90),
});

const exerciseSchema = z.object({
  exerciseId: z.string().uuid(),
  sets: z.array(setSchema).optional().default([]),
});

const createSchema = z.object({
  name: z.string().min(1),
  plannedFor: z.string().datetime().nullable().optional(),
  routineId: z.string().uuid().nullable().optional(),
  exercises: z.array(exerciseSchema).optional().default([]),
});

const updateSchema = z.object({
  name: z.string().optional(),
  startedAt: z.string().datetime().nullable().optional(),
  exercises: z.array(exerciseSchema).optional(),
});

router.get('/', authRequired, async (req, res, next) => {
  try {
    const rows = await knex('workouts').where({ user_id: req.userId }).orderBy('created_at', 'desc');
    const items = [];
    for (const r of rows) items.push(await serializeWorkout(r));
    res.json({ items });
  } catch (err) {
    next(err);
  }
});

router.get('/stats', authRequired, async (req, res, next) => {
  try {
    const oneWeekAgo = new Date(Date.now() - 7 * 86400000).toISOString();
    const weekly = await knex('workouts')
      .where({ user_id: req.userId })
      .whereNotNull('finished_at')
      .andWhere('finished_at', '>=', oneWeekAgo);
    const weeklyVolume = weekly.reduce((s, w) => s + (w.total_volume_kg || 0), 0);
    const workoutsThisWeek = weekly.length;

    // muscle heatmap: count workout_exercises grouped by exercise muscle
    const heatmapRows = await knex('workouts as w')
      .where({ 'w.user_id': req.userId })
      .whereNotNull('w.finished_at')
      .andWhere('w.finished_at', '>=', oneWeekAgo)
      .join('workout_exercises as we', 'we.workout_id', 'w.id')
      .join('exercises as e', 'e.id', 'we.exercise_id')
      .groupBy('e.muscle')
      .select('e.muscle')
      .count('* as c');
    const muscleHeatmap = {};
    for (const r of heatmapRows) muscleHeatmap[r.muscle] = parseInt(r.c, 10);

    res.json({ weeklyVolume, muscleHeatmap, workoutsThisWeek });
  } catch (err) {
    next(err);
  }
});

router.get('/:id', authRequired, async (req, res, next) => {
  try {
    const row = await knex('workouts').where({ id: req.params.id, user_id: req.userId }).first();
    if (!row) return res.status(404).json({ error: { code: 'not_found', message: 'Workout not found' } });
    res.json(await serializeWorkout(row));
  } catch (err) {
    next(err);
  }
});

router.post('/', authRequired, validate(createSchema), async (req, res, next) => {
  try {
    const { name, plannedFor, routineId, exercises } = req.body;
    const [row] = await knex('workouts')
      .insert({
        user_id: req.userId,
        name,
        planned_for: plannedFor || null,
        routine_id: routineId || null,
      })
      .returning('*');
    await writeExercises(row.id, exercises);
    res.status(201).json(await serializeWorkout(row));
  } catch (err) {
    next(err);
  }
});

router.patch('/:id', authRequired, validate(updateSchema), async (req, res, next) => {
  try {
    const row = await knex('workouts').where({ id: req.params.id, user_id: req.userId }).first();
    if (!row) return res.status(404).json({ error: { code: 'not_found', message: 'Workout not found' } });
    const { name, startedAt, exercises } = req.body;
    const update = {};
    if (name !== undefined) update.name = name;
    if (startedAt !== undefined) update.started_at = startedAt;
    if (Object.keys(update).length) await knex('workouts').where({ id: row.id }).update(update);
    if (exercises) {
      await knex('workout_exercises').where({ workout_id: row.id }).del();
      await writeExercises(row.id, exercises);
    }
    const updated = await knex('workouts').where({ id: row.id }).first();
    res.json(await serializeWorkout(updated));
  } catch (err) {
    next(err);
  }
});

router.post('/:id/finish', authRequired, async (req, res, next) => {
  try {
    const row = await knex('workouts').where({ id: req.params.id, user_id: req.userId }).first();
    if (!row) return res.status(404).json({ error: { code: 'not_found', message: 'Workout not found' } });
    if (row.finished_at) {
      return res.status(400).json({ error: { code: 'already_finished', message: 'Workout already finished' } });
    }
    // Compute volume + XP
    const setsAgg = await knex('workout_sets as s')
      .join('workout_exercises as we', 'we.id', 's.workout_exercise_id')
      .where({ 'we.workout_id': row.id, 's.completed': true })
      .select(knex.raw('COALESCE(SUM(s.weight * s.reps), 0) as volume'), knex.raw('COUNT(*) as count'))
      .first();
    const totalVolumeKg = Math.round(parseFloat(setsAgg.volume) || 0);
    const completedSets = parseInt(setsAgg.count, 10) || 0;
    const xp = computeWorkoutXp({ totalVolumeKg, completedSets });

    await knex('workouts').where({ id: row.id }).update({
      finished_at: knex.fn.now(),
      total_volume_kg: totalVolumeKg,
      xp_awarded: xp,
    });
    await awardXp(req.userId, xp);
    await knex('users')
      .where({ id: req.userId })
      .increment('stat_total_workouts', 1)
      .increment('stat_workouts_this_week', 1);

    const updated = await knex('workouts').where({ id: row.id }).first();
    res.json(await serializeWorkout(updated));
  } catch (err) {
    next(err);
  }
});

async function writeExercises(workoutId, exercises) {
  for (let i = 0; i < exercises.length; i += 1) {
    const ex = exercises[i];
    const [we] = await knex('workout_exercises')
      .insert({ workout_id: workoutId, exercise_id: ex.exerciseId, position: i })
      .returning('*');
    if (ex.sets && ex.sets.length) {
      const setRows = ex.sets.map((s, idx) => ({
        workout_exercise_id: we.id,
        position: idx,
        weight: s.weight,
        reps: s.reps,
        completed: s.completed,
        rest_sec: s.restSec,
      }));
      await knex('workout_sets').insert(setRows);
    }
  }
}

module.exports = router;
