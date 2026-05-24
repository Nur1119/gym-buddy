// screens-workout.jsx — Workout tracker, exercises, routines, custom, active workout

// ── Tab pill (Tracker / My Plan)
function TabPills({ tabs, active, onChange, theme }) {
  const t = theme;
  return (
    <div style={{
      display: 'flex', padding: 4, borderRadius: 14,
      background: t.surface2, gap: 4,
    }}>
      {tabs.map(x => (
        <button key={x.key} onClick={() => onChange(x.key)} style={{
          flex: 1, padding: '10px 12px', borderRadius: 10,
          border: 'none', cursor: 'pointer', fontFamily: 'inherit',
          background: active === x.key ? t.surface : 'transparent',
          color: active === x.key ? t.text : t.textMuted,
          fontWeight: 700, fontSize: 14, transition: 'all .15s',
          boxShadow: active === x.key ? '0 2px 6px rgba(0,0,0,0.06)' : 'none',
        }}>{x.label}</button>
      ))}
    </div>
  );
}

// ── Big action card
function ActionCard({ title, icon, color, onClick, theme }) {
  const t = theme;
  return (
    <button onClick={onClick} style={{
      width: '100%', padding: 18, borderRadius: 16,
      background: t.surface, border: `1px solid ${t.border}`,
      cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'space-between',
      fontFamily: 'inherit', textAlign: 'left',
    }}>
      <div style={{ fontSize: 16, fontWeight: 700, color: t.text }}>{title}</div>
      <div style={{
        width: 44, height: 44, borderRadius: 12,
        background: `linear-gradient(135deg, ${color}, ${color}99)`,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        boxShadow: `0 6px 16px ${color}44`,
      }}>
        <Icon name={icon} size={22} color="#fff"/>
      </div>
    </button>
  );
}

function WorkoutTrackerScreen({ theme, onNav }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      {/* Hero stats strip */}
      <div style={{ padding: '8px 18px 0' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 12 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <Avatar name={ME.name} size={40} color={t.p3} color2={t.p2}/>
            <div>
              <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600 }}>Lv.{ME.level}</div>
              <div style={{ width: 80, height: 4, background: t.chip, borderRadius: 2, marginTop: 2 }}>
                <div style={{ width: '74%', height: '100%', background: t.grad, borderRadius: 2 }}/>
              </div>
            </div>
          </div>
          <div style={{ display: 'flex', gap: 8 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 4, padding: '6px 10px', background: t.surface, border: `1px solid ${t.border}`, borderRadius: 12 }}>
              <Icon name="flame" size={14} color={t.danger}/>
              <span style={{ fontSize: 13, fontWeight: 700, color: t.text }}>{ME.streak}</span>
            </div>
            <IconBtn name="plus" size={32} iconSize={18} bg={t.grad} color="#fff"/>
          </div>
        </div>
      </div>

      {/* Tracker/MyPlan tabs */}
      <div style={{ padding: '16px 18px 0' }}>
        <TabPills active="tracker" onChange={()=>{}} theme={t} tabs={[
          { key: 'tracker', label: tr('tracker') },
          { key: 'plan', label: tr('myPlan') },
        ]}/>
      </div>

      {/* Planned workout */}
      <SectionHeader title={tr('plannedWorkout')} theme={t}/>
      <div style={{ padding: '0 18px' }}>
        <button onClick={() => onNav('createRoutine')} style={{
          width: '100%', padding: 18, borderRadius: 16,
          background: t.surface, border: `1px dashed ${t.borderStrong}`,
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          cursor: 'pointer', fontFamily: 'inherit',
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 14 }}>
            <div style={{ width: 44, height: 44, borderRadius: 12, background: t.gradSoft, display: 'flex', alignItems: 'center', justifyContent: 'center', color: t.p2 }}>
              <Icon name="edit" size={20}/>
            </div>
            <div style={{ fontSize: 16, fontWeight: 700, color: t.text }}>{tr('createPlan')}</div>
          </div>
          <Icon name="chevron-right" size={18} color={t.textDim}/>
        </button>
      </div>

      {/* New workout */}
      <SectionHeader title={lang === 'ru' ? 'Новая тренировка' : 'New workout'} theme={t}/>
      <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 10 }}>
        <ActionCard title={tr('startEmptyWorkout')} icon="dumbbell" color={t.p2} theme={t} onClick={() => onNav('activeWorkout')}/>
        <ActionCard title={tr('generateWorkout')} icon="spark" color={t.p1} theme={t}/>
      </div>

      {/* Routines */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 18px', marginTop: 18, marginBottom: 10 }}>
        <div style={{ fontSize: 17, fontWeight: 700, color: t.text }}>{tr('routines')}</div>
        <div style={{ display: 'flex', gap: 6 }}>
          <IconBtn name="list" size={32} iconSize={16}/>
          <IconBtn name="plus" size={32} iconSize={16}/>
        </div>
      </div>

      <div style={{ padding: '0 18px 4px', fontSize: 13, color: t.textMuted, fontWeight: 600 }}>
        {tr('myRoutines')} ({ROUTINES.length})
      </div>

      <div style={{ padding: '8px 18px 0', display: 'flex', flexDirection: 'column', gap: 10 }}>
        {ROUTINES.map(r => (
          <Card key={r.id} padding={14} onClick={() => onNav('routineDetail', r)}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 10 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                <div style={{ width: 6, height: 32, borderRadius: 3, background: r.color }}/>
                <div>
                  <div style={{ fontSize: 15, fontWeight: 700, color: t.text }}>
                    {lang === 'ru' ? r.nameRu : r.name}
                  </div>
                  <div style={{ fontSize: 11, color: t.textMuted, marginTop: 2 }}>
                    {r.sets} {tr('sets')} · {r.duration} min
                  </div>
                </div>
              </div>
              <IconBtn name="more" size={28} iconSize={16}/>
            </div>
            {/* Mini exercise list */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6, paddingLeft: 16 }}>
              {r.exercises.slice(0, 2).map(eid => {
                const ex = EXERCISE_LIB.find(x => x.id === eid);
                return ex ? (
                  <div key={eid} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                    <div style={{
                      width: 24, height: 24, borderRadius: 6, background: t.chip,
                      display: 'flex', alignItems: 'center', justifyContent: 'center',
                      fontSize: 12,
                    }}>{ex.icon}</div>
                    <div style={{ fontSize: 12, color: t.text, fontWeight: 500 }}>
                      {lang === 'ru' ? ex.nameRu : ex.name}
                    </div>
                    <div style={{ fontSize: 11, color: t.textDim, marginLeft: 'auto' }}>3 {tr('sets')}</div>
                  </div>
                ) : null;
              })}
              {r.exercises.length > 2 && (
                <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600 }}>
                  +{r.exercises.length - 2} more
                </div>
              )}
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
}

// ── Exercises Screen (full library)
function ExercisesScreen({ theme, onNav, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const [filter, setFilter] = React.useState('all');
  const [query, setQuery] = React.useState('');
  const list = EXERCISE_LIB.filter(e =>
    (filter === 'all' || e.muscle === filter) &&
    ((lang === 'ru' ? e.nameRu : e.name).toLowerCase().includes(query.toLowerCase()))
  );

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      <ScreenHeader
        title={tr('exercises')}
        left={<IconBtn name="arrow-left" size={36} iconSize={18} onClick={onBack}/>}
        right={<IconBtn name="plus" size={36} iconSize={18} bg={t.grad} color="#fff" onClick={() => onNav('createExercise')}/>}
        large
        theme={t}
      />

      {/* Search */}
      <div style={{ padding: '0 18px 12px' }}>
        <div style={{
          display: 'flex', alignItems: 'center', gap: 10,
          padding: '12px 14px', borderRadius: 12,
          background: t.surface, border: `1px solid ${t.border}`,
        }}>
          <Icon name="search" size={18} color={t.textDim}/>
          <input
            placeholder={tr('search')}
            value={query}
            onChange={e => setQuery(e.target.value)}
            style={{
              flex: 1, border: 'none', outline: 'none', background: 'transparent',
              fontSize: 15, color: t.text, fontFamily: 'inherit',
            }}
          />
        </div>
      </div>

      {/* Filter pills */}
      <div style={{
        padding: '0 18px 12px', display: 'flex', gap: 8, overflowX: 'auto',
      }}>
        {MUSCLE_GROUPS.map(g => (
          <Pill key={g.key} active={filter === g.key} onClick={() => setFilter(g.key)} color={t.p2}>
            {lang === 'ru' ? g.ru : g.en}
          </Pill>
        ))}
      </div>

      {/* List */}
      <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 8 }}>
        {list.map(ex => (
          <div key={ex.id} style={{
            display: 'flex', alignItems: 'center', gap: 12, padding: 12,
            background: t.surface, border: `1px solid ${t.border}`, borderRadius: 14,
          }}>
            <div style={{
              width: 44, height: 44, borderRadius: 10,
              background: t.chip, display: 'flex', alignItems: 'center', justifyContent: 'center',
              fontSize: 22,
            }}>{ex.icon}</div>
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ fontSize: 14, fontWeight: 700, color: t.text }}>
                {lang === 'ru' ? ex.nameRu : ex.name}
              </div>
              <div style={{ fontSize: 11, color: t.textMuted, marginTop: 2 }}>
                {lang === 'ru' ? ex.muscleRu : ex.muscle} · {ex.equipment}
              </div>
            </div>
            <Icon name="chevron-right" size={16} color={t.textDim}/>
          </div>
        ))}
      </div>
    </div>
  );
}

// ── Create Custom Exercise
function CreateExerciseScreen({ theme, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const [name, setName] = React.useState('');
  const [muscle, setMuscle] = React.useState('Chest');
  const [equipment, setEquipment] = React.useState('Barbell');

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      <ScreenHeader
        title={tr('createCustom')}
        left={<button onClick={onBack} style={{ border: 'none', background: 'transparent', color: t.p2, fontWeight: 600, cursor: 'pointer', fontFamily: 'inherit', fontSize: 15 }}>{tr('cancel')}</button>}
        right={<button style={{ border: 'none', background: 'transparent', color: t.p2, fontWeight: 700, cursor: 'pointer', fontFamily: 'inherit', fontSize: 15 }}>{tr('save')}</button>}
        large
        theme={t}
      />

      <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 14 }}>
        {/* Image picker */}
        <button style={{
          width: '100%', height: 120, borderRadius: 16,
          border: `1px dashed ${t.borderStrong}`, background: t.surface,
          display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 6,
          cursor: 'pointer', fontFamily: 'inherit',
        }}>
          <Icon name="camera" size={28} color={t.textDim}/>
          <span style={{ fontSize: 13, color: t.textMuted, fontWeight: 600 }}>
            {lang === 'ru' ? 'Добавить фото' : 'Add photo'}
          </span>
        </button>

        {/* Name */}
        <div>
          <div style={{ fontSize: 12, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginBottom: 6, paddingLeft: 4 }}>{tr('name')}</div>
          <input
            value={name}
            onChange={e => setName(e.target.value)}
            placeholder={lang === 'ru' ? 'Жим узким хватом' : 'Close-grip bench'}
            style={{
              width: '100%', padding: '14px 16px', borderRadius: 12,
              border: `1px solid ${t.border}`, background: t.surface,
              color: t.text, fontSize: 15, fontFamily: 'inherit', outline: 'none', boxSizing: 'border-box',
            }}
          />
        </div>

        {/* Muscle group */}
        <div>
          <div style={{ fontSize: 12, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginBottom: 6, paddingLeft: 4 }}>{lang === 'ru' ? 'Группа мышц' : 'Muscle group'}</div>
          <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
            {MUSCLE_GROUPS.filter(g => g.key !== 'all').map(g => (
              <Pill key={g.key} active={muscle === g.key} onClick={() => setMuscle(g.key)} color={t.p2}>
                {lang === 'ru' ? g.ru : g.en}
              </Pill>
            ))}
          </div>
        </div>

        {/* Equipment */}
        <div>
          <div style={{ fontSize: 12, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginBottom: 6, paddingLeft: 4 }}>{lang === 'ru' ? 'Оборудование' : 'Equipment'}</div>
          <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
            {['Barbell','Dumbbell','Machine','Cable','Bodyweight'].map(eq => (
              <Pill key={eq} active={equipment === eq} onClick={() => setEquipment(eq)} color={t.p1}>{eq}</Pill>
            ))}
          </div>
        </div>

        {/* Notes */}
        <div>
          <div style={{ fontSize: 12, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginBottom: 6, paddingLeft: 4 }}>{lang === 'ru' ? 'Заметки' : 'Notes'}</div>
          <textarea
            placeholder={lang === 'ru' ? 'Техника, советы…' : 'Form cues, tips…'}
            style={{
              width: '100%', minHeight: 80, padding: '14px 16px', borderRadius: 12,
              border: `1px solid ${t.border}`, background: t.surface,
              color: t.text, fontSize: 14, fontFamily: 'inherit', outline: 'none', resize: 'none', boxSizing: 'border-box',
            }}
          />
        </div>

        <GradButton icon="check">{lang === 'ru' ? 'Создать упражнение' : 'Create exercise'}</GradButton>
      </div>
    </div>
  );
}

// ── Active Workout (in-progress tracker)
function ActiveWorkoutScreen({ theme, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const [timer, setTimer] = React.useState(0);
  const [resting, setResting] = React.useState(false);
  const [rest, setRest] = React.useState(0);

  React.useEffect(() => {
    const id = setInterval(() => setTimer(s => s + 1), 1000);
    return () => clearInterval(id);
  }, []);
  React.useEffect(() => {
    if (!resting) return;
    const id = setInterval(() => setRest(r => Math.max(0, r - 1)), 1000);
    return () => clearInterval(id);
  }, [resting]);

  const fmt = (s) => `${String(Math.floor(s / 60)).padStart(2,'0')}:${String(s % 60).padStart(2,'0')}`;

  const exercises = [
    { ex: EXERCISE_LIB[1], sets: [{w:50,r:10,done:true},{w:55,r:8,done:true},{w:55,r:8,done:false}] },
    { ex: EXERCISE_LIB[2], sets: [{w:0,r:10,done:true},{w:0,r:9,done:false},{w:0,r:8,done:false}] },
    { ex: EXERCISE_LIB[10], sets: [{w:25,r:12,done:false},{w:25,r:12,done:false},{w:25,r:12,done:false}] },
  ];

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 80 }}>
      {/* Header with timer */}
      <div style={{
        padding: '4px 18px 12px', display: 'flex', alignItems: 'center', justifyContent: 'space-between',
      }}>
        <button onClick={onBack} style={{
          border: 'none', background: t.chip, color: t.text, padding: '8px 14px',
          borderRadius: 999, cursor: 'pointer', fontFamily: 'inherit',
          fontSize: 13, fontWeight: 600,
        }}>{tr('back')}</button>
        <div style={{ display: 'flex', alignItems: 'center', gap: 6, color: t.p1, fontSize: 18, fontWeight: 800, fontFamily: 'ui-monospace, monospace' }}>
          <Icon name="clock" size={18}/>
          {fmt(timer)}
        </div>
        <button style={{
          border: 'none', background: t.success, color: '#fff', padding: '8px 14px',
          borderRadius: 999, cursor: 'pointer', fontFamily: 'inherit',
          fontSize: 13, fontWeight: 700,
        }}>{tr('finishWorkout')}</button>
      </div>

      {/* Rest banner */}
      {resting && rest > 0 && (
        <div style={{ padding: '0 18px 12px' }}>
          <div style={{
            padding: 14, borderRadius: 14, background: t.grad, color: '#fff',
            display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          }}>
            <div>
              <div style={{ fontSize: 11, opacity: 0.85, fontWeight: 700, textTransform: 'uppercase', letterSpacing: 1 }}>{tr('restTimer')}</div>
              <div style={{ fontSize: 28, fontWeight: 800, fontFamily: 'ui-monospace, monospace' }}>{fmt(rest)}</div>
            </div>
            <button onClick={() => setResting(false)} style={{
              border: 'none', background: 'rgba(255,255,255,0.25)', color: '#fff',
              padding: '8px 14px', borderRadius: 999, cursor: 'pointer', fontWeight: 700, fontFamily: 'inherit',
            }}>Skip</button>
          </div>
        </div>
      )}

      {/* Exercise blocks */}
      <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 12 }}>
        {exercises.map((eb, ei) => (
          <Card key={ei} padding={14}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 12 }}>
              <div style={{ width: 36, height: 36, borderRadius: 8, background: t.chip, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 18 }}>
                {eb.ex.icon}
              </div>
              <div style={{ flex: 1 }}>
                <div style={{ fontSize: 15, fontWeight: 700, color: t.text }}>
                  {lang === 'ru' ? eb.ex.nameRu : eb.ex.name}
                </div>
                <div style={{ fontSize: 11, color: t.textMuted }}>
                  {lang === 'ru' ? eb.ex.muscleRu : eb.ex.muscle}
                </div>
              </div>
              <IconBtn name="more" size={28} iconSize={14}/>
            </div>

            {/* Set table */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
              <div style={{ display: 'flex', fontSize: 10, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', padding: '0 4px' }}>
                <div style={{ width: 36 }}>{tr('set')}</div>
                <div style={{ flex: 1 }}>{tr('weight')}</div>
                <div style={{ flex: 1 }}>{tr('reps')}</div>
                <div style={{ width: 30 }}/>
              </div>
              {eb.sets.map((s, i) => (
                <div key={i} style={{
                  display: 'flex', alignItems: 'center', padding: '8px 4px', borderRadius: 8,
                  background: s.done ? t.gradSoft : 'transparent',
                }}>
                  <div style={{ width: 36, fontSize: 14, fontWeight: 700, color: s.done ? t.p2 : t.text }}>{i + 1}</div>
                  <div style={{ flex: 1, fontSize: 14, color: t.text, fontWeight: 600 }}>{s.w > 0 ? `${s.w} kg` : '—'}</div>
                  <div style={{ flex: 1, fontSize: 14, color: t.text, fontWeight: 600 }}>{s.r}</div>
                  <button onClick={() => { setResting(true); setRest(90); }} style={{
                    width: 30, height: 30, borderRadius: 8, border: 'none', cursor: 'pointer',
                    background: s.done ? t.p1 : t.chip,
                    color: s.done ? '#fff' : t.textDim,
                    display: 'flex', alignItems: 'center', justifyContent: 'center',
                  }}>
                    <Icon name="check" size={14}/>
                  </button>
                </div>
              ))}
              <button style={{
                marginTop: 6, padding: '8px', borderRadius: 8, border: `1px dashed ${t.borderStrong}`,
                background: 'transparent', color: t.textMuted, fontWeight: 600, fontSize: 13, cursor: 'pointer',
                fontFamily: 'inherit',
              }}>+ {lang === 'ru' ? 'Подход' : 'Set'}</button>
            </div>
          </Card>
        ))}

        <button style={{
          padding: 14, borderRadius: 14, border: `1px dashed ${t.borderStrong}`,
          background: 'transparent', color: t.p2, fontWeight: 700, fontSize: 14, cursor: 'pointer',
          fontFamily: 'inherit', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 6,
        }}>
          <Icon name="plus" size={16}/>
          {tr('addExercise')}
        </button>
      </div>
    </div>
  );
}

Object.assign(window, { WorkoutTrackerScreen, ExercisesScreen, CreateExerciseScreen, ActiveWorkoutScreen, TabPills });
