'use strict';

const bcrypt = require('bcrypt');
const { v5: uuidv5 } = require('uuid');

// Deterministic UUID namespace so seed data has predictable IDs.
const NS = '6ba7b810-9dad-11d1-80b4-00c04fd430c8';
const id = (key) => uuidv5(key, NS);

const PASSWORD = 'password123';

const EXERCISES = [
  { code: 'e1', name: 'Bench Press', muscle: 'Chest', equipment: 'Barbell', icon: '🏋️' },
  { code: 'e2', name: 'Incline Dumbbell Press', muscle: 'Chest', equipment: 'Dumbbell', icon: '💪' },
  { code: 'e3', name: 'Pull-Ups', muscle: 'Back', equipment: 'Bodyweight', icon: '🤸' },
  { code: 'e4', name: 'Deadlift', muscle: 'Back', equipment: 'Barbell', icon: '🏋️' },
  { code: 'e5', name: 'Squat', muscle: 'Legs', equipment: 'Barbell', icon: '🦵' },
  { code: 'e6', name: 'Leg Press', muscle: 'Legs', equipment: 'Machine', icon: '🦵' },
  { code: 'e7', name: 'Overhead Press', muscle: 'Shoulders', equipment: 'Barbell', icon: '💪' },
  { code: 'e8', name: 'Lateral Raise', muscle: 'Shoulders', equipment: 'Dumbbell', icon: '💪' },
  { code: 'e9', name: 'Bicep Curl', muscle: 'Arms', equipment: 'Dumbbell', icon: '💪' },
  { code: 'e10', name: 'Tricep Pushdown', muscle: 'Arms', equipment: 'Cable', icon: '💪' },
  { code: 'e11', name: 'Cable Fly', muscle: 'Chest', equipment: 'Cable', icon: '🏋️' },
  { code: 'e12', name: 'Lat Pulldown', muscle: 'Back', equipment: 'Cable', icon: '💪' },
  { code: 'e13', name: 'Romanian Deadlift', muscle: 'Legs', equipment: 'Barbell', icon: '🦵' },
  { code: 'e14', name: 'Plank', muscle: 'Core', equipment: 'Bodyweight', icon: '🧘' },
  { code: 'e15', name: 'Hanging Leg Raise', muscle: 'Core', equipment: 'Bodyweight', icon: '🧘' },
  { code: 'e16', name: 'Running', muscle: 'Cardio', equipment: 'Bodyweight', icon: '🏃' },
];

const ROUTINES = [
  { code: 'r1', name: 'Upper body — Monday', color: '#7C5CFF', duration: 65, exercises: ['e2', 'e3', 'e11', 'e7', 'e9', 'e10'] },
  { code: 'r2', name: 'Pull day', color: '#00C2FF', duration: 55, exercises: ['e3', 'e4', 'e12', 'e9'] },
  { code: 'r3', name: 'Leg day crusher', color: '#3DDC97', duration: 70, exercises: ['e5', 'e6', 'e13'] },
  { code: 'r4', name: 'Push day', color: '#FFB020', duration: 50, exercises: ['e1', 'e2', 'e7', 'e10', 'e11'] },
  { code: 'r5', name: 'Core finisher', color: '#FF4D6D', duration: 25, exercises: ['e14', 'e15'] },
];

const DEMO_USER = {
  code: 'alex',
  email: 'alex@gymbuddy.app',
  name: 'Alex',
  username: '@alex_lifts',
  age: 28,
  height: 181,
  weight: 82,
  bio: 'Powerbuilder. Coffee fueled. Always down for legs.',
  goal: 'Hypertrophy',
  level: 'Advanced',
  gym_name: 'Iron Temple',
  gym_lat: 40.7589,
  gym_lng: -73.9851,
  schedule: '{1,3,5}',
  interests: '{Powerlifting,Bodybuilding,Coffee}',
  color: '#7C5CFF',
  color2: '#00C2FF',
  stat_level: 79,
  stat_xp: 3854,
  stat_xp_to_next: 5213,
  stat_total_xp: 158913,
  stat_streak: 125,
  stat_best_streak: 125,
  stat_coins: 3099,
  stat_total_workouts: 412,
  stat_workouts_this_week: 4,
};

const DISCOVER_USERS = [
  {
    code: 'u1', email: 'alina@gymbuddy.app', name: 'Alina', username: '@alina_lifts',
    age: 24, height: 168, weight: 62,
    bio: 'Powerlifter looking for a serious lifting partner. PRs > small talk.',
    goal: 'Strength', level: 'Advanced',
    gym_name: 'PowerHouse Gym', gym_lat: 40.7505, gym_lng: -73.9934,
    schedule: '{1,3,5}', interests: '{Powerlifting,"Squat 140kg",Coffee}',
    color: '#FF4D6D', color2: '#7C5CFF',
    stat_level: 62, stat_total_xp: 124000, stat_streak: 80, stat_total_workouts: 290,
  },
  {
    code: 'u2', email: 'marcus@gymbuddy.app', name: 'Marcus', username: '@marcus_calisthenics',
    age: 27, height: 182, weight: 78,
    bio: 'Calisthenics & street workout. Park sessions every weekend.',
    goal: 'Calisthenics', level: 'Elite',
    gym_name: 'Central Park', gym_lat: 40.7829, gym_lng: -73.9654,
    schedule: '{0,1,2,3,4,5,6}', interests: '{Planche,"Front lever",Vegan}',
    color: '#3DDC97', color2: '#00C2FF',
    stat_level: 88, stat_total_xp: 200000, stat_streak: 200, stat_total_workouts: 510,
  },
  {
    code: 'u3', email: 'sofia@gymbuddy.app', name: 'Sofia', username: '@sofia_flows',
    age: 22, height: 165, weight: 55,
    bio: 'Yoga + functional. Looking for a balanced training partner.',
    goal: 'Mobility', level: 'Intermediate',
    gym_name: 'Zenith Studio', gym_lat: 40.7614, gym_lng: -73.9776,
    schedule: '{2,4,6}', interests: '{Yoga,Pilates,Plant-based}',
    color: '#7C5CFF', color2: '#00C2FF',
    stat_level: 45, stat_total_xp: 80000, stat_streak: 40, stat_total_workouts: 180,
  },
  {
    code: 'u4', email: 'daniel@gymbuddy.app', name: 'Daniel', username: '@daniel_bb',
    age: 29, height: 178, weight: 88,
    bio: 'Bodybuilder, prep for stage. 6×/week. Need someone to push leg day.',
    goal: 'Hypertrophy', level: 'Advanced',
    gym_name: 'Iron Temple', gym_lat: 40.7589, gym_lng: -73.9851,
    schedule: '{1,2,3,4,5,6}', interests: '{Bodybuilding,"Chicken & rice","Cardio hater"}',
    color: '#00C2FF', color2: '#3DDC97',
    stat_level: 71, stat_total_xp: 142000, stat_streak: 95, stat_total_workouts: 350,
  },
  {
    code: 'u5', email: 'emma@gymbuddy.app', name: 'Emma', username: '@emma_wod',
    age: 26, height: 170, weight: 64,
    bio: 'CrossFit coach. WODs at 6am, coffee at 7. Energy off the charts.',
    goal: 'CrossFit', level: 'Elite',
    gym_name: 'BoxFit', gym_lat: 40.7549, gym_lng: -73.9840,
    schedule: '{1,2,3,4,5}', interests: '{"Olympic lifts",Murph,"Trail running"}',
    color: '#FFB020', color2: '#FF4D6D',
    stat_level: 84, stat_total_xp: 175000, stat_streak: 150, stat_total_workouts: 460,
  },
  {
    code: 'u6', email: 'noah@gymbuddy.app', name: 'Noah', username: '@noah_pl',
    age: 31, height: 185, weight: 95,
    bio: 'Geared powerlifter. Big three or nothing.',
    goal: 'Strength', level: 'Elite',
    gym_name: 'Strongman Gym', gym_lat: 40.7308, gym_lng: -73.9973,
    schedule: '{1,3,5}', interests: '{Powerlifting,Strongman,Steaks}',
    color: '#FF4D6D', color2: '#FFB020',
    stat_level: 90, stat_total_xp: 210000, stat_streak: 60, stat_total_workouts: 540,
  },
  {
    code: 'u7', email: 'maya@gymbuddy.app', name: 'Maya', username: '@maya_runs',
    age: 25, height: 172, weight: 60,
    bio: 'Marathoner training for Boston. Long runs welcome.',
    goal: 'Cardio', level: 'Intermediate',
    gym_name: 'Riverside Track', gym_lat: 40.7960, gym_lng: -73.9728,
    schedule: '{0,2,4,6}', interests: '{Running,Marathon,Trail}',
    color: '#3DDC97', color2: '#FFB020',
    stat_level: 55, stat_total_xp: 102000, stat_streak: 110, stat_total_workouts: 220,
  },
  {
    code: 'u8', email: 'liam@gymbuddy.app', name: 'Liam', username: '@liam_hybrid',
    age: 28, height: 180, weight: 80,
    bio: 'Hybrid athlete — lift heavy, run far. Looking for balance.',
    goal: 'Hypertrophy', level: 'Advanced',
    gym_name: 'FitPlex', gym_lat: 40.7411, gym_lng: -74.0033,
    schedule: '{1,2,4,5}', interests: '{Hybrid,Cycling,Books}',
    color: '#00C2FF', color2: '#7C5CFF',
    stat_level: 67, stat_total_xp: 134000, stat_streak: 70, stat_total_workouts: 305,
  },
  {
    code: 'u9', email: 'priya@gymbuddy.app', name: 'Priya', username: '@priya_yoga',
    age: 30, height: 162, weight: 58,
    bio: 'Yoga teacher exploring strength training. New to barbell, eager.',
    goal: 'Mobility', level: 'Beginner',
    gym_name: 'Zenith Studio', gym_lat: 40.7614, gym_lng: -73.9776,
    schedule: '{0,3,5}', interests: '{Yoga,Meditation,Tea}',
    color: '#B967FF', color2: '#3DDC97',
    stat_level: 22, stat_total_xp: 32000, stat_streak: 15, stat_total_workouts: 65,
  },
  {
    code: 'u10', email: 'kenji@gymbuddy.app', name: 'Kenji', username: '@kenji_cf',
    age: 26, height: 175, weight: 72,
    bio: 'CrossFitter chasing a sub-3min Fran. Always game for a metcon.',
    goal: 'CrossFit', level: 'Advanced',
    gym_name: 'BoxFit', gym_lat: 40.7549, gym_lng: -73.9840,
    schedule: '{1,2,4,5,6}', interests: '{CrossFit,"Olympic lifts",Sushi}',
    color: '#FFB020', color2: '#00C2FF',
    stat_level: 72, stat_total_xp: 146000, stat_streak: 85, stat_total_workouts: 360,
  },
];

const FRIEND_USERS = [
  { code: 'f1', email: 'jake@gymbuddy.app', name: 'Jake T.', username: '@jake_t',
    age: 32, height: 183, weight: 92, bio: 'Bench day every day',
    goal: 'Hypertrophy', level: 'Advanced', gym_name: 'Iron Temple',
    schedule: '{1,3,5}', interests: '{Bench,BBQ}', color: '#FF4D6D', color2: '#FFB020',
    stat_level: 64, stat_total_xp: 128340 },
  { code: 'f2', email: 'lena@gymbuddy.app', name: 'Lena K.', username: '@lena_k',
    age: 28, height: 167, weight: 60, bio: 'PR squat 130kg',
    goal: 'Strength', level: 'Elite', gym_name: 'PowerHouse Gym',
    schedule: '{2,4,6}', interests: '{Powerlifting,Coffee}', color: '#00C2FF', color2: '#7C5CFF',
    stat_level: 92, stat_total_xp: 172110 },
  { code: 'f5', email: 'carlos@gymbuddy.app', name: 'Carlos P.', username: '@carlos_p',
    age: 35, height: 178, weight: 78, bio: 'Hit goal weight — keeping it',
    goal: 'CrossFit', level: 'Elite', gym_name: 'BoxFit',
    schedule: '{0,1,2,3,4,5}', interests: '{CrossFit,Cycling}', color: '#FFB020', color2: '#FF4D6D',
    stat_level: 105, stat_total_xp: 184220 },
];

const MEDALS = [
  { code: 'streak_100', name: '100-day streak', icon: '🔥', description: 'Train 100 days in a row' },
  { code: 'bench_100', name: 'First 100kg bench', icon: '💪', description: 'Bench press 100kg' },
  { code: 'top_10_weekly', name: 'Top 10 weekly', icon: '🏆', description: 'Reach top 10 on the weekly leaderboard' },
  { code: 'squat_2x_bw', name: 'Squat 2× bodyweight', icon: '🦵', description: 'Squat double your bodyweight' },
  { code: 'sets_1000', name: '1000 sets logged', icon: '⚡', description: 'Log 1000 sets' },
  { code: 'level_100', name: 'Level 100', icon: '🌟', description: 'Reach level 100' },
];

const QUESTS = [
  { code: 'train_3_week', text: 'Train 3 times this week', total: 3, xp: 250, period: 'weekly' },
  { code: 'sets_12_today', text: 'Log 12 sets today', total: 12, xp: 80, period: 'daily' },
  { code: 'try_new_exercise', text: 'Try a new exercise', total: 1, xp: 100, period: 'weekly' },
];

exports.seed = async function seed(knex) {
  // Clean slate (children first)
  await knex('user_quests').del();
  await knex('quests').del();
  await knex('user_medals').del();
  await knex('medals').del();
  await knex('meals').del();
  await knex('friend_requests').del();
  await knex('friends').del();
  await knex('workout_sets').del();
  await knex('workout_exercises').del();
  await knex('workouts').del();
  await knex('routine_exercises').del();
  await knex('routines').del();
  await knex('messages').del();
  await knex('matches').del();
  await knex('swipes').del();
  await knex('user_filters').del();
  await knex('user_photos').del();
  await knex('exercises').del();
  await knex('users').del();

  const passwordHash = await bcrypt.hash(PASSWORD, 10);

  // ── Users ─────────────────────────────────────────────────
  const allUsers = [DEMO_USER, ...DISCOVER_USERS, ...FRIEND_USERS];
  const userRows = allUsers.map((u) => ({
    id: id('user-' + u.code),
    email: u.email,
    password_hash: passwordHash,
    name: u.name,
    username: u.username,
    age: u.age,
    height: u.height,
    weight: u.weight,
    bio: u.bio,
    goal: u.goal,
    level: u.level,
    gym_name: u.gym_name || null,
    gym_lat: u.gym_lat || null,
    gym_lng: u.gym_lng || null,
    schedule: u.schedule,
    interests: u.interests,
    color: u.color,
    color2: u.color2,
    stat_level: u.stat_level || 1,
    stat_xp: u.stat_xp || 0,
    stat_xp_to_next: u.stat_xp_to_next || 1000,
    stat_total_xp: u.stat_total_xp || 0,
    stat_streak: u.stat_streak || 0,
    stat_best_streak: u.stat_best_streak || u.stat_streak || 0,
    stat_coins: u.stat_coins || 0,
    stat_total_workouts: u.stat_total_workouts || 0,
    stat_workouts_this_week: u.stat_workouts_this_week || 0,
  }));
  await knex('users').insert(userRows);

  // ── User filters (default for demo user)
  await knex('user_filters').insert({
    user_id: id('user-alex'),
    age_min: 21,
    age_max: 35,
    max_distance_km: 15,
    goals: '{Strength,Hypertrophy}',
    levels: '{Intermediate,Advanced,Elite}',
    schedule_days: '{1,2,3,4,5}',
  });

  // ── User photos (placeholder URLs)
  const photoRows = [];
  for (const u of allUsers) {
    const n = u === DEMO_USER ? 3 : Math.floor(Math.random() * 3) + 2;
    for (let i = 0; i < n; i += 1) {
      photoRows.push({
        id: id('photo-' + u.code + '-' + i),
        user_id: id('user-' + u.code),
        url: `https://picsum.photos/seed/${u.code}-${i}/600/800`,
        position: i,
      });
    }
  }
  await knex('user_photos').insert(photoRows);

  // ── Exercises (built-in)
  const exerciseRows = EXERCISES.map((e) => ({
    id: id('exercise-' + e.code),
    name: e.name,
    muscle: e.muscle,
    equipment: e.equipment,
    icon: e.icon,
    is_custom: false,
    owner_id: null,
  }));
  await knex('exercises').insert(exerciseRows);

  // ── Routines (owned by demo user)
  const routineRows = ROUTINES.map((r) => ({
    id: id('routine-' + r.code),
    name: r.name,
    owner_id: id('user-alex'),
    color: r.color,
    estimated_duration_min: r.duration,
  }));
  await knex('routines').insert(routineRows);

  const routineExerciseRows = [];
  for (const r of ROUTINES) {
    r.exercises.forEach((ex, idx) => {
      routineExerciseRows.push({
        id: id('routine-ex-' + r.code + '-' + ex),
        routine_id: id('routine-' + r.code),
        exercise_id: id('exercise-' + ex),
        position: idx,
        sets: 3,
        target_reps: 10,
        rest_sec: 90,
      });
    });
  }
  await knex('routine_exercises').insert(routineExerciseRows);

  // ── Matches (demo user matched with u1, u3, u5)
  const matchSeeds = [
    { code: 'm-u1', other: 'u1', daysAgo: 0 },
    { code: 'm-u3', other: 'u3', daysAgo: 1 },
    { code: 'm-u5', other: 'u5', daysAgo: 3 },
  ];
  const now = new Date();
  const matchRows = matchSeeds.map((m) => {
    const d = new Date(now.getTime() - m.daysAgo * 86400000);
    return {
      id: id('match-' + m.code),
      user_a: id('user-alex'),
      user_b: id('user-' + m.other),
      last_activity_at: d.toISOString(),
      created_at: d.toISOString(),
    };
  });
  await knex('matches').insert(matchRows);

  // Mutual swipes for those matches
  const swipeRows = [];
  for (const m of matchSeeds) {
    swipeRows.push({
      id: id('swipe-alex-' + m.other),
      user_id: id('user-alex'),
      target_user_id: id('user-' + m.other),
      direction: 'like',
    });
    swipeRows.push({
      id: id('swipe-' + m.other + '-alex'),
      user_id: id('user-' + m.other),
      target_user_id: id('user-alex'),
      direction: 'like',
    });
  }
  await knex('swipes').insert(swipeRows);

  // ── Messages (from CHAT_THREADS)
  const messageRows = [];
  const chatThreads = {
    'm-u1': [
      { from: 'u1', text: 'Hey! Saw your bench PR, impressive 💪' },
      { from: 'alex', text: 'Thanks! Your squat is insane btw' },
      { from: 'u1', text: 'Wanna train together? I do legs Mondays' },
      { from: 'alex', text: "Let's do it. PowerHouse?" },
      { from: 'u1', text: 'Up for legs tomorrow?' },
    ],
    'm-u3': [
      { from: 'u3', text: 'Hi! Yoga class at 7am tomorrow?' },
      { from: 'alex', text: 'Count me in' },
      { from: 'u3', text: 'See you at 7am 💪' },
    ],
    'm-u5': [
      { from: 'u5', text: 'Murph today??' },
      { from: 'alex', text: 'Are you insane' },
      { from: 'u5', text: 'Yes 😈' },
      { from: 'u5', text: 'That Murph was brutal' },
    ],
  };
  for (const [matchCode, msgs] of Object.entries(chatThreads)) {
    const baseTime = now.getTime() - 86400000;
    msgs.forEach((msg, i) => {
      messageRows.push({
        id: id('msg-' + matchCode + '-' + i),
        match_id: id('match-' + matchCode),
        sender_id: id('user-' + msg.from),
        kind: 'text',
        text: msg.text,
        created_at: new Date(baseTime + i * 60000).toISOString(),
      });
    });
  }
  await knex('messages').insert(messageRows);

  // ── Friends (demo user friends with f1, f2, f5)
  await knex('friends').insert([
    { user_a: id('user-alex'), user_b: id('user-f1') },
    { user_a: id('user-alex'), user_b: id('user-f2') },
    { user_a: id('user-alex'), user_b: id('user-f5') },
  ]);

  // ── Medals
  const medalRows = MEDALS.map((m) => ({
    id: id('medal-' + m.code),
    code: m.code,
    name: m.name,
    icon: m.icon,
    description: m.description,
  }));
  await knex('medals').insert(medalRows);

  // Demo user unlocks 4/6 medals
  await knex('user_medals').insert([
    { user_id: id('user-alex'), medal_id: id('medal-streak_100') },
    { user_id: id('user-alex'), medal_id: id('medal-bench_100') },
    { user_id: id('user-alex'), medal_id: id('medal-top_10_weekly') },
    { user_id: id('user-alex'), medal_id: id('medal-sets_1000') },
  ]);

  // ── Quests
  const questRows = QUESTS.map((q) => ({
    id: id('quest-' + q.code),
    code: q.code,
    text: q.text,
    total: q.total,
    xp: q.xp,
    period: q.period,
  }));
  await knex('quests').insert(questRows);

  await knex('user_quests').insert([
    { user_id: id('user-alex'), quest_id: id('quest-train_3_week'), progress: 2 },
    { user_id: id('user-alex'), quest_id: id('quest-sets_12_today'), progress: 8 },
    { user_id: id('user-alex'), quest_id: id('quest-try_new_exercise'), progress: 0 },
  ]);

  // ── A completed workout for the demo user (for stats)
  const workoutId = id('workout-demo-1');
  await knex('workouts').insert({
    id: workoutId,
    user_id: id('user-alex'),
    routine_id: id('routine-r1'),
    name: 'Upper body — Monday',
    started_at: new Date(now.getTime() - 2 * 86400000).toISOString(),
    finished_at: new Date(now.getTime() - 2 * 86400000 + 65 * 60000).toISOString(),
    total_volume_kg: 8400,
    xp_awarded: 320,
  });
  const we1 = id('we-demo-1-bench');
  const we2 = id('we-demo-1-pullups');
  await knex('workout_exercises').insert([
    { id: we1, workout_id: workoutId, exercise_id: id('exercise-e1'), position: 0 },
    { id: we2, workout_id: workoutId, exercise_id: id('exercise-e3'), position: 1 },
  ]);
  const setRows = [];
  for (let i = 0; i < 3; i += 1) {
    setRows.push({ workout_exercise_id: we1, position: i, weight: 100, reps: 8, completed: true });
    setRows.push({ workout_exercise_id: we2, position: i, weight: 0, reps: 10, completed: true });
  }
  await knex('workout_sets').insert(setRows);

  console.log('Seed complete.');
  console.log('Demo login: alex@gymbuddy.app / password123');
};
