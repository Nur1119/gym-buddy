'use strict';

/**
 * Initial schema for GymBuddy.
 *
 * Tables created:
 *   users, user_photos, user_filters,
 *   swipes, matches, messages,
 *   exercises, custom_exercises (handled via exercises.is_custom flag),
 *   routines, routine_exercises,
 *   workouts, workout_exercises, workout_sets,
 *   friends, friend_requests,
 *   meals,
 *   medals, user_medals,
 *   quests, user_quests
 */

exports.up = async function up(knex) {
  // Enable uuid generation
  await knex.raw('CREATE EXTENSION IF NOT EXISTS "pgcrypto"');

  // ── users ───────────────────────────────────────────────────
  await knex.schema.createTable('users', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.string('email').notNullable().unique();
    t.string('password_hash').notNullable();
    t.string('name').notNullable();
    t.string('username').notNullable().unique();
    t.integer('age');
    t.integer('height'); // cm
    t.integer('weight'); // kg
    t.text('bio');
    t.string('goal'); // Strength | Hypertrophy | Mobility | Calisthenics | CrossFit | Cardio
    t.string('level'); // Beginner | Intermediate | Advanced | Elite
    t.string('gym_name');
    t.double('gym_lat');
    t.double('gym_lng');
    t.specificType('schedule', 'integer[]').defaultTo('{}'); // 0=Sun..6=Sat
    t.specificType('interests', 'text[]').defaultTo('{}');
    t.string('color').defaultTo('#7C5CFF');
    t.string('color2').defaultTo('#00C2FF');

    // Stats
    t.integer('stat_level').defaultTo(1);
    t.integer('stat_xp').defaultTo(0);
    t.integer('stat_xp_to_next').defaultTo(1000);
    t.integer('stat_total_xp').defaultTo(0);
    t.integer('stat_streak').defaultTo(0);
    t.integer('stat_best_streak').defaultTo(0);
    t.integer('stat_coins').defaultTo(0);
    t.integer('stat_total_workouts').defaultTo(0);
    t.integer('stat_workouts_this_week').defaultTo(0);

    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.timestamp('updated_at').defaultTo(knex.fn.now());

    t.index('email');
    t.index('username');
  });

  // ── user_photos ─────────────────────────────────────────────
  await knex.schema.createTable('user_photos', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('user_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.string('url').notNullable();
    t.integer('position').defaultTo(0);
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.index('user_id');
  });

  // ── user_filters ────────────────────────────────────────────
  await knex.schema.createTable('user_filters', (t) => {
    t.uuid('user_id').primary().references('id').inTable('users').onDelete('CASCADE');
    t.integer('age_min').defaultTo(18);
    t.integer('age_max').defaultTo(60);
    t.integer('max_distance_km').defaultTo(25);
    t.specificType('goals', 'text[]').defaultTo('{}');
    t.specificType('levels', 'text[]').defaultTo('{}');
    t.specificType('schedule_days', 'integer[]').defaultTo('{}');
    t.timestamp('updated_at').defaultTo(knex.fn.now());
  });

  // ── swipes ──────────────────────────────────────────────────
  await knex.schema.createTable('swipes', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('user_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.uuid('target_user_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.string('direction').notNullable(); // like | pass | superlike
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.unique(['user_id', 'target_user_id']);
    t.index(['user_id', 'created_at']);
  });

  // ── matches ─────────────────────────────────────────────────
  await knex.schema.createTable('matches', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('user_a').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.uuid('user_b').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.boolean('is_unmatched').defaultTo(false);
    t.uuid('unmatched_by');
    t.timestamp('last_activity_at').defaultTo(knex.fn.now());
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.unique(['user_a', 'user_b']);
    t.index('user_a');
    t.index('user_b');
    t.index('last_activity_at');
  });

  // ── messages ────────────────────────────────────────────────
  await knex.schema.createTable('messages', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('match_id').notNullable().references('id').inTable('matches').onDelete('CASCADE');
    t.uuid('sender_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.string('kind').notNullable().defaultTo('text'); // text | image | workout_invite
    t.text('text');
    t.string('image_url');
    t.jsonb('payload');
    t.timestamp('read_at');
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.index(['match_id', 'created_at']);
  });

  // ── exercises ───────────────────────────────────────────────
  await knex.schema.createTable('exercises', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.string('name').notNullable();
    t.string('muscle').notNullable();
    t.string('equipment').notNullable();
    t.string('icon').defaultTo('💪');
    t.text('notes');
    t.boolean('is_custom').defaultTo(false);
    t.uuid('owner_id').references('id').inTable('users').onDelete('CASCADE');
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.index('muscle');
    t.index('owner_id');
  });

  // ── routines ────────────────────────────────────────────────
  await knex.schema.createTable('routines', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.string('name').notNullable();
    t.uuid('owner_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.string('color').defaultTo('#7C5CFF');
    t.integer('estimated_duration_min').defaultTo(60);
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.index('owner_id');
  });

  // ── routine_exercises ───────────────────────────────────────
  await knex.schema.createTable('routine_exercises', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('routine_id').notNullable().references('id').inTable('routines').onDelete('CASCADE');
    t.uuid('exercise_id').notNullable().references('id').inTable('exercises').onDelete('CASCADE');
    t.integer('position').defaultTo(0);
    t.integer('sets').defaultTo(3);
    t.integer('target_reps').defaultTo(10);
    t.integer('rest_sec').defaultTo(90);
    t.index('routine_id');
  });

  // ── workouts ────────────────────────────────────────────────
  await knex.schema.createTable('workouts', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('user_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.uuid('routine_id').references('id').inTable('routines').onDelete('SET NULL');
    t.string('name').notNullable();
    t.timestamp('planned_for');
    t.timestamp('started_at');
    t.timestamp('finished_at');
    t.integer('total_volume_kg').defaultTo(0);
    t.integer('xp_awarded').defaultTo(0);
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.index(['user_id', 'planned_for']);
    t.index(['user_id', 'finished_at']);
  });

  // ── workout_exercises ───────────────────────────────────────
  await knex.schema.createTable('workout_exercises', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('workout_id').notNullable().references('id').inTable('workouts').onDelete('CASCADE');
    t.uuid('exercise_id').notNullable().references('id').inTable('exercises').onDelete('CASCADE');
    t.integer('position').defaultTo(0);
    t.index('workout_id');
  });

  // ── workout_sets ────────────────────────────────────────────
  await knex.schema.createTable('workout_sets', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('workout_exercise_id')
      .notNullable()
      .references('id')
      .inTable('workout_exercises')
      .onDelete('CASCADE');
    t.integer('position').defaultTo(0);
    t.double('weight').defaultTo(0);
    t.integer('reps').defaultTo(0);
    t.integer('rest_sec').defaultTo(90);
    t.boolean('completed').defaultTo(false);
    t.index('workout_exercise_id');
  });

  // ── friends ─────────────────────────────────────────────────
  await knex.schema.createTable('friends', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('user_a').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.uuid('user_b').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.unique(['user_a', 'user_b']);
  });

  // ── friend_requests ─────────────────────────────────────────
  await knex.schema.createTable('friend_requests', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('from_user_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.uuid('to_user_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.string('status').defaultTo('pending'); // pending | accepted | declined
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.unique(['from_user_id', 'to_user_id']);
    t.index('to_user_id');
  });

  // ── meals ───────────────────────────────────────────────────
  await knex.schema.createTable('meals', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('user_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.date('date').notNullable();
    t.string('time'); // HH:MM
    t.string('kind').notNullable(); // breakfast | lunch | dinner | snack
    t.jsonb('items').defaultTo('[]');
    t.integer('total_kcal').defaultTo(0);
    t.integer('total_protein').defaultTo(0);
    t.integer('total_carbs').defaultTo(0);
    t.integer('total_fats').defaultTo(0);
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.index(['user_id', 'date']);
  });

  // ── medals ──────────────────────────────────────────────────
  await knex.schema.createTable('medals', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.string('code').notNullable().unique();
    t.string('name').notNullable();
    t.string('icon').defaultTo('🏅');
    t.text('description');
  });

  // ── user_medals ─────────────────────────────────────────────
  await knex.schema.createTable('user_medals', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('user_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.uuid('medal_id').notNullable().references('id').inTable('medals').onDelete('CASCADE');
    t.timestamp('unlocked_at').defaultTo(knex.fn.now());
    t.unique(['user_id', 'medal_id']);
  });

  // ── quests ──────────────────────────────────────────────────
  await knex.schema.createTable('quests', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.string('code').notNullable().unique();
    t.string('text').notNullable();
    t.integer('total').defaultTo(1);
    t.integer('xp').defaultTo(50);
    t.string('period').defaultTo('daily'); // daily | weekly
  });

  // ── user_quests ─────────────────────────────────────────────
  await knex.schema.createTable('user_quests', (t) => {
    t.uuid('id').primary().defaultTo(knex.raw('gen_random_uuid()'));
    t.uuid('user_id').notNullable().references('id').inTable('users').onDelete('CASCADE');
    t.uuid('quest_id').notNullable().references('id').inTable('quests').onDelete('CASCADE');
    t.integer('progress').defaultTo(0);
    t.boolean('completed').defaultTo(false);
    t.timestamp('created_at').defaultTo(knex.fn.now());
    t.unique(['user_id', 'quest_id']);
  });
};

exports.down = async function down(knex) {
  await knex.schema.dropTableIfExists('user_quests');
  await knex.schema.dropTableIfExists('quests');
  await knex.schema.dropTableIfExists('user_medals');
  await knex.schema.dropTableIfExists('medals');
  await knex.schema.dropTableIfExists('meals');
  await knex.schema.dropTableIfExists('friend_requests');
  await knex.schema.dropTableIfExists('friends');
  await knex.schema.dropTableIfExists('workout_sets');
  await knex.schema.dropTableIfExists('workout_exercises');
  await knex.schema.dropTableIfExists('workouts');
  await knex.schema.dropTableIfExists('routine_exercises');
  await knex.schema.dropTableIfExists('routines');
  await knex.schema.dropTableIfExists('exercises');
  await knex.schema.dropTableIfExists('messages');
  await knex.schema.dropTableIfExists('matches');
  await knex.schema.dropTableIfExists('swipes');
  await knex.schema.dropTableIfExists('user_filters');
  await knex.schema.dropTableIfExists('user_photos');
  await knex.schema.dropTableIfExists('users');
};
