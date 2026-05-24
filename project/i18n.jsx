// i18n.jsx — bilingual strings

const STRINGS = {
  en: {
    // tabs
    home: 'Home', workout: 'Workout', discover: 'Discover', friends: 'Friends', profile: 'Profile',
    // home
    greeting: 'Good evening', readyToTrain: 'Ready to train today?',
    todayPlan: "Today's plan", noWorkoutToday: 'No workout planned today',
    thisWeek: 'This week', streak: 'Streak', xp: 'XP', coins: 'Coins',
    quickStart: 'Quick start', startWorkout: 'Start workout', emptyWorkout: 'Empty workout',
    generateAi: 'AI workout', recentRoutines: 'Recent routines',
    weeklyVolume: 'Weekly volume', muscleHeatmap: 'Muscle activity',
    // workout
    tracker: 'Tracker', myPlan: 'My plan', plannedWorkout: 'Planned workout',
    createPlan: 'Create plan', startEmptyWorkout: 'Start empty workout',
    generateWorkout: 'AI workout', routines: 'Routines', myRoutines: 'My routines',
    exercises: 'Exercises', createCustom: 'Create custom exercise',
    addExercise: 'Add exercise', finishWorkout: 'Finish', restTimer: 'Rest',
    sets: 'sets', reps: 'reps', weight: 'kg', set: 'Set',
    // discover (Tinder)
    discoverTitle: 'Discover', discoverSub: 'Find your gym buddy',
    filters: 'Filters', matches: 'Matches', boost: 'Boost', superLike: 'Super Like',
    rewind: 'Rewind', like: 'Like', nope: 'Pass',
    itsAMatch: "It's a match!", sendMessage: 'Send a message',
    inviteToGym: 'Invite to gym', sharedWorkout: 'Shared workout',
    distance: 'km away', goal: 'Goal', schedule: 'Schedule', level: 'Level',
    // profile
    editProfile: 'Edit profile', settings: 'Settings', stats: 'Statistics',
    medals: 'Medals', ranks: 'Ranks', calendar: 'Calendar', nutrition: 'Nutrition',
    achievements: 'Achievements',
    // settings
    appearance: 'Appearance', language: 'Language', units: 'Units',
    notifications: 'Notifications', account: 'Account', logout: 'Logout',
    privacy: 'Privacy', terms: 'Terms',
    // generic
    save: 'Save', cancel: 'Cancel', done: 'Done', next: 'Next', back: 'Back',
    add: 'Add', edit: 'Edit', delete: 'Delete', search: 'Search',
    name: 'Name', age: 'Age', height: 'Height', weightLabel: 'Weight', bio: 'Bio',
    today: 'Today', yesterday: 'Yesterday',
    // workout categories
    push: 'Push', pull: 'Pull', legs: 'Legs', upper: 'Upper', lower: 'Lower',
    full: 'Full body', cardio: 'Cardio', core: 'Core',
    // nutrition
    calories: 'Calories', protein: 'Protein', carbs: 'Carbs', fats: 'Fats',
    addMeal: 'Add meal', breakfast: 'Breakfast', lunch: 'Lunch', dinner: 'Dinner', snack: 'Snack',
    // friends
    online: 'Online', requests: 'Requests', leaderboard: 'Leaderboard',
    // chat
    typeMessage: 'Type a message…', matchedOn: 'Matched',
    // misc
    streakDays: 'day streak', newMatch: 'New match',
    suggestWorkout: 'Suggest a workout',
  },
  ru: {
    home: 'Главная', workout: 'Тренировки', discover: 'Знакомства', friends: 'Друзья', profile: 'Профиль',
    greeting: 'Добрый вечер', readyToTrain: 'Готов потренироваться?',
    todayPlan: 'План на сегодня', noWorkoutToday: 'На сегодня тренировок нет',
    thisWeek: 'На этой неделе', streak: 'Стрик', xp: 'XP', coins: 'Монеты',
    quickStart: 'Быстрый старт', startWorkout: 'Начать тренировку', emptyWorkout: 'Пустая тренировка',
    generateAi: 'AI-тренировка', recentRoutines: 'Недавние рутины',
    weeklyVolume: 'Объём за неделю', muscleHeatmap: 'Активность мышц',
    tracker: 'Трекер', myPlan: 'Мой план', plannedWorkout: 'Запланированная',
    createPlan: 'Создать план', startEmptyWorkout: 'Пустая тренировка',
    generateWorkout: 'AI-тренировка', routines: 'Рутины', myRoutines: 'Мои рутины',
    exercises: 'Упражнения', createCustom: 'Создать упражнение',
    addExercise: 'Добавить упражнение', finishWorkout: 'Завершить', restTimer: 'Отдых',
    sets: 'подходов', reps: 'повторов', weight: 'кг', set: 'Сет',
    discoverTitle: 'Знакомства', discoverSub: 'Найди партнёра по залу',
    filters: 'Фильтры', matches: 'Совпадения', boost: 'Буст', superLike: 'Супер-лайк',
    rewind: 'Назад', like: 'Лайк', nope: 'Пропустить',
    itsAMatch: 'Это совпадение!', sendMessage: 'Написать сообщение',
    inviteToGym: 'Пригласить в зал', sharedWorkout: 'Совместная тренировка',
    distance: 'км', goal: 'Цель', schedule: 'График', level: 'Уровень',
    editProfile: 'Изменить профиль', settings: 'Настройки', stats: 'Статистика',
    medals: 'Медали', ranks: 'Рейтинг', calendar: 'Календарь', nutrition: 'Питание',
    achievements: 'Достижения',
    appearance: 'Тема', language: 'Язык', units: 'Единицы',
    notifications: 'Уведомления', account: 'Аккаунт', logout: 'Выйти',
    privacy: 'Конфиденциальность', terms: 'Условия',
    save: 'Сохранить', cancel: 'Отмена', done: 'Готово', next: 'Далее', back: 'Назад',
    add: 'Добавить', edit: 'Изменить', delete: 'Удалить', search: 'Поиск',
    name: 'Имя', age: 'Возраст', height: 'Рост', weightLabel: 'Вес', bio: 'О себе',
    today: 'Сегодня', yesterday: 'Вчера',
    push: 'Жим', pull: 'Тяга', legs: 'Ноги', upper: 'Верх', lower: 'Низ',
    full: 'Всё тело', cardio: 'Кардио', core: 'Кор',
    calories: 'Калории', protein: 'Белки', carbs: 'Углеводы', fats: 'Жиры',
    addMeal: 'Добавить приём', breakfast: 'Завтрак', lunch: 'Обед', dinner: 'Ужин', snack: 'Перекус',
    online: 'В сети', requests: 'Заявки', leaderboard: 'Лидерборд',
    typeMessage: 'Сообщение…', matchedOn: 'Совпали',
    streakDays: 'дней подряд', newMatch: 'Новое совпадение',
    suggestWorkout: 'Предложить тренировку',
  },
};

const I18nCtx = React.createContext(null);

function I18nProvider({ lang, children }) {
  const t = React.useCallback((key) => (STRINGS[lang] && STRINGS[lang][key]) || STRINGS.en[key] || key, [lang]);
  return <I18nCtx.Provider value={{ t, lang }}>{children}</I18nCtx.Provider>;
}

function useI18n() {
  return React.useContext(I18nCtx);
}

window.I18nProvider = I18nProvider;
window.useI18n = useI18n;
