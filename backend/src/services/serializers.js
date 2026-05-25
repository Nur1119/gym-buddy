'use strict';

const knex = require('../db/knex');

/**
 * Format a user row + photos into the public User shape from the API contract.
 */
async function serializeUser(row, opts = {}) {
  if (!row) return null;
  const photos = await knex('user_photos')
    .where({ user_id: row.id })
    .orderBy('position', 'asc')
    .select('id', 'url', 'position');

  const base = {
    id: row.id,
    name: row.name,
    username: row.username,
    userHandle: row.user_handle || '',
    age: row.age,
    height: row.height,
    weight: row.weight,
    bio: row.bio,
    goal: row.goal,
    level: row.level,
    gymName: row.gym_name,
    gymLat: row.gym_lat,
    gymLng: row.gym_lng,
    schedule: row.schedule || [],
    interests: row.interests || [],
    color: row.color,
    color2: row.color2,
    photos,
    stats: {
      level: row.stat_level,
      xp: row.stat_xp,
      xpToNext: row.stat_xp_to_next,
      totalXp: row.stat_total_xp,
      streak: row.stat_streak,
      bestStreak: row.stat_best_streak,
      coins: row.stat_coins,
      totalWorkouts: row.stat_total_workouts,
      workoutsThisWeek: row.stat_workouts_this_week,
    },
    createdAt: row.created_at,
  };
  if (opts.includeEmail) base.email = row.email;
  return base;
}

function serializeExercise(row) {
  if (!row) return null;
  return {
    id: row.id,
    name: row.name,
    muscle: row.muscle,
    equipment: row.equipment,
    icon: row.icon,
    notes: row.notes,
    isCustom: row.is_custom,
    ownerId: row.owner_id,
  };
}

function serializeMessage(row) {
  if (!row) return null;
  return {
    id: row.id,
    matchId: row.match_id,
    senderId: row.sender_id,
    kind: row.kind,
    text: row.text,
    imageUrl: row.image_url,
    payload: row.payload,
    readAt: row.read_at,
    createdAt: row.created_at,
  };
}

async function serializeMatch(matchRow, currentUserId) {
  if (!matchRow) return null;
  const otherUserId = matchRow.user_a === currentUserId ? matchRow.user_b : matchRow.user_a;
  const otherUserRow = await knex('users').where({ id: otherUserId }).first();
  const otherUser = await serializeUser(otherUserRow);

  const lastMessage = await knex('messages')
    .where({ match_id: matchRow.id })
    .orderBy('created_at', 'desc')
    .first();

  const unreadCount = await knex('messages')
    .where({ match_id: matchRow.id })
    .whereNot({ sender_id: currentUserId })
    .whereNull('read_at')
    .count('* as c')
    .first();

  return {
    id: matchRow.id,
    user: otherUser,
    matchedAt: matchRow.created_at,
    lastMessage: lastMessage ? serializeMessage(lastMessage) : null,
    unreadCount: parseInt(unreadCount.c, 10),
    isUnmatched: matchRow.is_unmatched,
  };
}

async function serializeRoutine(row) {
  if (!row) return null;
  const exRows = await knex('routine_exercises')
    .where({ routine_id: row.id })
    .orderBy('position', 'asc');
  const exercises = exRows.map((e) => ({
    exerciseId: e.exercise_id,
    sets: e.sets,
    targetReps: e.target_reps,
    restSec: e.rest_sec,
  }));
  const totalSets = exercises.reduce((s, e) => s + e.sets, 0);
  return {
    id: row.id,
    name: row.name,
    exercises,
    totalSets,
    estimatedDurationMin: row.estimated_duration_min,
    color: row.color,
    ownerId: row.owner_id,
    createdAt: row.created_at,
  };
}

async function serializeWorkout(row) {
  if (!row) return null;
  const weRows = await knex('workout_exercises')
    .where({ workout_id: row.id })
    .orderBy('position', 'asc');
  const exercises = [];
  for (const we of weRows) {
    const sets = await knex('workout_sets')
      .where({ workout_exercise_id: we.id })
      .orderBy('position', 'asc')
      .select('weight', 'reps', 'completed', 'rest_sec');
    exercises.push({
      exerciseId: we.exercise_id,
      sets: sets.map((s) => ({
        weight: s.weight,
        reps: s.reps,
        completed: s.completed,
        restSec: s.rest_sec,
      })),
    });
  }
  return {
    id: row.id,
    name: row.name,
    startedAt: row.started_at,
    finishedAt: row.finished_at,
    plannedFor: row.planned_for,
    routineId: row.routine_id,
    exercises,
    totalVolumeKg: row.total_volume_kg,
    xpAwarded: row.xp_awarded,
  };
}

module.exports = {
  serializeUser,
  serializeExercise,
  serializeMessage,
  serializeMatch,
  serializeRoutine,
  serializeWorkout,
};
