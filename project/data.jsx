// data.jsx — mock data for the entire app

// ── Exercise library
const EXERCISE_LIB = [
  { id: 'e1', name: 'Bench Press', nameRu: 'Жим лёжа', muscle: 'Chest', muscleRu: 'Грудь', equipment: 'Barbell', icon: '🏋️' },
  { id: 'e2', name: 'Incline Dumbbell Press', nameRu: 'Жим гантелей наклонно', muscle: 'Chest', muscleRu: 'Грудь', equipment: 'Dumbbell', icon: '💪' },
  { id: 'e3', name: 'Pull-Ups', nameRu: 'Подтягивания', muscle: 'Back', muscleRu: 'Спина', equipment: 'Bodyweight', icon: '🤸' },
  { id: 'e4', name: 'Deadlift', nameRu: 'Становая тяга', muscle: 'Back', muscleRu: 'Спина', equipment: 'Barbell', icon: '🏋️' },
  { id: 'e5', name: 'Squat', nameRu: 'Приседания', muscle: 'Legs', muscleRu: 'Ноги', equipment: 'Barbell', icon: '🦵' },
  { id: 'e6', name: 'Leg Press', nameRu: 'Жим ногами', muscle: 'Legs', muscleRu: 'Ноги', equipment: 'Machine', icon: '🦵' },
  { id: 'e7', name: 'Overhead Press', nameRu: 'Армейский жим', muscle: 'Shoulders', muscleRu: 'Плечи', equipment: 'Barbell', icon: '💪' },
  { id: 'e8', name: 'Lateral Raise', nameRu: 'Махи в стороны', muscle: 'Shoulders', muscleRu: 'Плечи', equipment: 'Dumbbell', icon: '💪' },
  { id: 'e9', name: 'Bicep Curl', nameRu: 'Сгибание на бицепс', muscle: 'Arms', muscleRu: 'Руки', equipment: 'Dumbbell', icon: '💪' },
  { id: 'e10', name: 'Tricep Pushdown', nameRu: 'Разгибания на трицепс', muscle: 'Arms', muscleRu: 'Руки', equipment: 'Cable', icon: '💪' },
  { id: 'e11', name: 'Cable Fly', nameRu: 'Сведения в кроссовере', muscle: 'Chest', muscleRu: 'Грудь', equipment: 'Cable', icon: '🏋️' },
  { id: 'e12', name: 'Lat Pulldown', nameRu: 'Тяга верхнего блока', muscle: 'Back', muscleRu: 'Спина', equipment: 'Cable', icon: '💪' },
  { id: 'e13', name: 'Romanian Deadlift', nameRu: 'Румынская тяга', muscle: 'Legs', muscleRu: 'Ноги', equipment: 'Barbell', icon: '🦵' },
  { id: 'e14', name: 'Plank', nameRu: 'Планка', muscle: 'Core', muscleRu: 'Кор', equipment: 'Bodyweight', icon: '🧘' },
  { id: 'e15', name: 'Hanging Leg Raise', nameRu: 'Подъём ног в висе', muscle: 'Core', muscleRu: 'Кор', equipment: 'Bodyweight', icon: '🧘' },
  { id: 'e16', name: 'Running', nameRu: 'Бег', muscle: 'Cardio', muscleRu: 'Кардио', equipment: 'Bodyweight', icon: '🏃' },
];

const MUSCLE_GROUPS = [
  { key: 'all', en: 'All', ru: 'Все' },
  { key: 'Chest', en: 'Chest', ru: 'Грудь' },
  { key: 'Back', en: 'Back', ru: 'Спина' },
  { key: 'Legs', en: 'Legs', ru: 'Ноги' },
  { key: 'Shoulders', en: 'Shoulders', ru: 'Плечи' },
  { key: 'Arms', en: 'Arms', ru: 'Руки' },
  { key: 'Core', en: 'Core', ru: 'Кор' },
  { key: 'Cardio', en: 'Cardio', ru: 'Кардио' },
];

// ── Routines
const ROUTINES = [
  {
    id: 'r1', name: 'Upper body — Monday', nameRu: 'Верх тела — понедельник', sets: 28, duration: 65,
    exercises: ['e2', 'e3', 'e11', 'e7', 'e9', 'e10'],
    color: '#7C5CFF',
  },
  {
    id: 'r2', name: 'Pull day', nameRu: 'Тяговый день', sets: 22, duration: 55,
    exercises: ['e3', 'e4', 'e12', 'e9'],
    color: '#00C2FF',
  },
  {
    id: 'r3', name: 'Leg day crusher', nameRu: 'Убойный день ног', sets: 24, duration: 70,
    exercises: ['e5', 'e6', 'e13'],
    color: '#3DDC97',
  },
  {
    id: 'r4', name: 'Push day', nameRu: 'Жимовой день', sets: 20, duration: 50,
    exercises: ['e1', 'e2', 'e7', 'e10', 'e11'],
    color: '#FFB020',
  },
  {
    id: 'r5', name: 'Core finisher', nameRu: 'Финишер для кора', sets: 12, duration: 25,
    exercises: ['e14', 'e15'],
    color: '#FF4D6D',
  },
];

// ── Calendar memories (workouts done by day)
const WORKOUT_HISTORY = {
  18: { type: 'Upper', muscles: ['chest', 'back', 'shoulders'] },
  19: { type: 'Pull', muscles: ['back', 'arms'] },
  20: { type: 'Legs', muscles: ['legs'] },
  23: { type: 'Full', muscles: ['chest', 'back', 'legs'] },
};

// ── Discover users (Tinder-style cards)
const DISCOVER_USERS = [
  {
    id: 'u1', name: 'Alina', age: 24, distance: 1.2,
    bio: 'Powerlifter looking for a serious lifting partner. PRs > small talk.',
    bioRu: 'Пауэрлифтер ищу серьёзного напарника. PR-ы важнее болтовни.',
    goal: 'Strength', goalRu: 'Сила',
    gym: 'PowerHouse Gym', level: 'Advanced', levelRu: 'Продвинутый',
    schedule: 'Mon · Wed · Fri', scheduleRu: 'Пн · Ср · Пт',
    height: 168, weight: 62,
    interests: ['Powerlifting', 'Squat 140kg', 'Coffee'],
    interestsRu: ['Пауэрлифтинг', 'Присед 140кг', 'Кофе'],
    color: '#FF4D6D',
    color2: '#7C5CFF',
    photos: 4,
  },
  {
    id: 'u2', name: 'Marcus', age: 27, distance: 2.4,
    bio: 'Calisthenics & street workout. Park sessions every weekend.',
    bioRu: 'Калистеника и стрит-воркаут. Каждые выходные на турниках.',
    goal: 'Calisthenics', goalRu: 'Калистеника',
    gym: 'Central Park', level: 'Elite', levelRu: 'Элита',
    schedule: 'Daily', scheduleRu: 'Каждый день',
    height: 182, weight: 78,
    interests: ['Planche', 'Front lever', 'Vegan'],
    interestsRu: ['Планш', 'Передний вис', 'Веган'],
    color: '#3DDC97',
    color2: '#00C2FF',
    photos: 3,
  },
  {
    id: 'u3', name: 'Sofia', age: 22, distance: 0.6,
    bio: 'Yoga + functional. Looking for a balanced training partner.',
    bioRu: 'Йога + функционалка. Ищу сбалансированного напарника.',
    goal: 'Mobility', goalRu: 'Мобильность',
    gym: 'Zenith Studio', level: 'Intermediate', levelRu: 'Средний',
    schedule: 'Tue · Thu · Sat', scheduleRu: 'Вт · Чт · Сб',
    height: 165, weight: 55,
    interests: ['Yoga', 'Pilates', 'Plant-based'],
    interestsRu: ['Йога', 'Пилатес', 'Растит. питание'],
    color: '#7C5CFF',
    color2: '#00C2FF',
    photos: 5,
  },
  {
    id: 'u4', name: 'Daniel', age: 29, distance: 3.8,
    bio: 'Bodybuilder, prep for stage. 6×/week. Need someone to push leg day.',
    bioRu: 'Бодибилдер, готовлюсь к сцене. 6×/нед. Нужен напарник на ноги.',
    goal: 'Hypertrophy', goalRu: 'Гипертрофия',
    gym: 'Iron Temple', level: 'Advanced', levelRu: 'Продвинутый',
    schedule: '6×/week', scheduleRu: '6 раз/нед',
    height: 178, weight: 88,
    interests: ['Bodybuilding', 'Chicken & rice', 'Cardio hater'],
    interestsRu: ['Бодибилдинг', 'Курица и рис', 'Ненавидит кардио'],
    color: '#00C2FF',
    color2: '#3DDC97',
    photos: 4,
  },
  {
    id: 'u5', name: 'Emma', age: 26, distance: 1.9,
    bio: 'CrossFit coach. WODs at 6am, coffee at 7. Energy off the charts.',
    bioRu: 'CrossFit-тренер. WOD-ы в 6 утра, кофе в 7. Энергия зашкаливает.',
    goal: 'CrossFit', goalRu: 'CrossFit',
    gym: 'BoxFit', level: 'Elite', levelRu: 'Элита',
    schedule: '5×/week', scheduleRu: '5 раз/нед',
    height: 170, weight: 64,
    interests: ['Olympic lifts', 'Murph', 'Trail running'],
    interestsRu: ['Олимпийские', 'Murph', 'Трейл'],
    color: '#FFB020',
    color2: '#FF4D6D',
    photos: 3,
  },
];

// ── Matches (already matched users for the Matches list)
const MATCHES = [
  { userId: 'u1', matchedAt: 'Today', matchedAtRu: 'Сегодня', online: true, unread: 2,
    lastMsg: 'Up for legs tomorrow?', lastMsgRu: 'Завтра на ноги?' },
  { userId: 'u3', matchedAt: 'Yesterday', matchedAtRu: 'Вчера', online: false, unread: 0,
    lastMsg: 'See you at 7am 💪', lastMsgRu: 'Увидимся в 7 утра 💪' },
  { userId: 'u5', matchedAt: '3d ago', matchedAtRu: '3д назад', online: true, unread: 5,
    lastMsg: 'That Murph was brutal', lastMsgRu: 'Этот Murph был жестью' },
];

// ── Chat messages
const CHAT_THREADS = {
  u1: [
    { from: 'u1', text: 'Hey! Saw your bench PR, impressive 💪', textRu: 'Привет! Видела твой PR в жиме, впечатляет 💪', time: '14:22' },
    { from: 'me', text: 'Thanks! Your squat is insane btw', textRu: 'Спасибо! Твой присед кстати огонь', time: '14:25' },
    { from: 'u1', text: 'Wanna train together? I do legs Mondays', textRu: 'Тренироваться вместе? Ноги по понедельникам', time: '14:26' },
    { from: 'me', text: "Let's do it. PowerHouse?", textRu: 'Давай. PowerHouse?', time: '14:30' },
    { from: 'u1', text: 'Up for legs tomorrow?', textRu: 'Завтра на ноги?', time: '18:11' },
  ],
  u3: [
    { from: 'u3', text: 'Hi! Yoga class at 7am tomorrow?', textRu: 'Привет! Йога завтра в 7?', time: '09:15' },
    { from: 'me', text: 'Count me in', textRu: 'Я в деле', time: '09:20' },
    { from: 'u3', text: 'See you at 7am 💪', textRu: 'Увидимся в 7 утра 💪', time: '09:21' },
  ],
  u5: [
    { from: 'u5', text: 'Murph today??', textRu: 'Murph сегодня??', time: '06:00' },
    { from: 'me', text: 'Are you insane', textRu: 'Ты с ума сошла', time: '06:01' },
    { from: 'u5', text: 'Yes 😈', textRu: 'Да 😈', time: '06:02' },
    { from: 'u5', text: 'That Murph was brutal', textRu: 'Этот Murph был жестью', time: '10:30' },
  ],
};

// ── Friends (separate from matches; social, not dating)
const FRIENDS = [
  { id: 'f1', name: 'Jake T.', level: 64, streak: 88, online: true, color: '#FF4D6D', last: 'Bench day' },
  { id: 'f2', name: 'Lena K.', level: 92, streak: 210, online: true, color: '#00C2FF', last: 'PR squat 130kg' },
  { id: 'f3', name: 'Max R.', level: 41, streak: 12, online: false, color: '#7C5CFF', last: '5km run' },
  { id: 'f4', name: 'Yuki S.', level: 73, streak: 56, online: false, color: '#3DDC97', last: 'Full body' },
  { id: 'f5', name: 'Carlos P.', level: 105, streak: 320, online: true, color: '#FFB020', last: 'Hit goal weight' },
];

// ── Leaderboard
const LEADERBOARD = [
  { rank: 1, name: 'Carlos P.', xp: 184220, color: '#FFD700' },
  { rank: 2, name: 'Lena K.', xp: 172110, color: '#C0C0C0' },
  { rank: 3, name: 'You', xp: 158913, color: '#CD7F32', isMe: true },
  { rank: 4, name: 'Yuki S.', xp: 141500, color: '#7C5CFF' },
  { rank: 5, name: 'Jake T.', xp: 128340, color: '#00C2FF' },
  { rank: 6, name: 'Marcus L.', xp: 119800, color: '#3DDC97' },
  { rank: 7, name: 'Anya B.', xp: 102450, color: '#FF4D6D' },
];

// ── Medals
const MEDALS = [
  { id: 'm1', icon: '🔥', name: '100-day streak', nameRu: 'Стрик 100 дней', unlocked: true },
  { id: 'm2', icon: '💪', name: 'First 100kg bench', nameRu: 'Первые 100кг в жиме', unlocked: true },
  { id: 'm3', icon: '🏆', name: 'Top 10 weekly', nameRu: 'Топ-10 недели', unlocked: true },
  { id: 'm4', icon: '🦵', name: 'Squat 2× bodyweight', nameRu: 'Присед 2× веса', unlocked: false },
  { id: 'm5', icon: '⚡', name: '1000 sets logged', nameRu: '1000 подходов', unlocked: true },
  { id: 'm6', icon: '🌟', name: 'Level 100', nameRu: 'Уровень 100', unlocked: false },
];

// ── Quests
const QUESTS = [
  { id: 'q1', text: 'Train 3 times this week', textRu: 'Тренируйся 3 раза на неделе', progress: 2, total: 3, xp: 250 },
  { id: 'q2', text: 'Log 12 sets today', textRu: 'Запиши 12 подходов сегодня', progress: 8, total: 12, xp: 80 },
  { id: 'q3', text: 'Try a new exercise', textRu: 'Попробуй новое упражнение', progress: 0, total: 1, xp: 100 },
];

// ── Streak (last 7 days, 1 = trained, 0.5 = partial, 0 = missed)
const STREAK_WEEK = [1, 1, 1, 1, 0.3, 1, 1];

// ── Me (user profile)
const ME = {
  name: 'Alex',
  username: '@alex_lifts',
  age: 28,
  height: 181,
  weight: 82,
  level: 79,
  xp: 3854,
  xpToNext: 5213,
  totalXp: 158913,
  streak: 125,
  bestStreak: 125,
  coins: 3099,
  bio: 'Powerbuilder. Coffee fueled. Always down for legs.',
  bioRu: 'Пауэрбилдер. На кофе. Всегда за день ног.',
  goal: 'Hypertrophy', goalRu: 'Гипертрофия',
  workoutsThisWeek: 4,
  totalWorkouts: 412,
};

window.EXERCISE_LIB = EXERCISE_LIB;
window.MUSCLE_GROUPS = MUSCLE_GROUPS;
window.ROUTINES = ROUTINES;
window.WORKOUT_HISTORY = WORKOUT_HISTORY;
window.DISCOVER_USERS = DISCOVER_USERS;
window.MATCHES = MATCHES;
window.CHAT_THREADS = CHAT_THREADS;
window.FRIENDS = FRIENDS;
window.LEADERBOARD = LEADERBOARD;
window.MEDALS = MEDALS;
window.QUESTS = QUESTS;
window.STREAK_WEEK = STREAK_WEEK;
window.ME = ME;
