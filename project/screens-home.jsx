// screens-home.jsx — Home dashboard

function StreakDay({ day, value, theme }) {
  const t = theme;
  const filled = value === 1;
  const partial = value > 0 && value < 1;
  const bg = filled ? t.grad : partial ? t.gradSoft : t.chip;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 6, flex: 1 }}>
      <div style={{ fontSize: 10, fontWeight: 700, color: filled ? t.p2 : t.textDim, letterSpacing: 0.5 }}>{day}</div>
      <div style={{
        width: 32, height: 32, borderRadius: 16, background: bg,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        color: filled ? '#fff' : t.textDim,
        border: !filled && !partial ? `1px solid ${t.border}` : 'none',
      }}>
        {filled && <Icon name="check" size={16} color="#fff"/>}
        {partial && <div style={{width:8,height:8,borderRadius:4,background:t.p2}}/>}
      </div>
    </div>
  );
}

function MuscleHeatmap({ theme }) {
  const t = theme;
  // Show body silhouette with muscle activity
  const muscles = [
    { name: 'chest', cx: 50, cy: 26, level: 0.7 },
    { name: 'shoulder-l', cx: 36, cy: 22, level: 0.5 },
    { name: 'shoulder-r', cx: 64, cy: 22, level: 0.5 },
    { name: 'arm-l', cx: 30, cy: 38, level: 0.6 },
    { name: 'arm-r', cx: 70, cy: 38, level: 0.6 },
    { name: 'core', cx: 50, cy: 44, level: 0.3 },
    { name: 'quad-l', cx: 44, cy: 68, level: 0.8 },
    { name: 'quad-r', cx: 56, cy: 68, level: 0.8 },
    { name: 'calf-l', cx: 44, cy: 88, level: 0.2 },
    { name: 'calf-r', cx: 56, cy: 88, level: 0.2 },
  ];
  const colorAt = (lvl) => {
    if (lvl > 0.6) return t.danger;
    if (lvl > 0.3) return t.warn;
    if (lvl > 0) return t.p1;
    return t.border;
  };
  return (
    <svg viewBox="0 0 100 100" style={{ width: '100%', maxWidth: 140, height: 'auto' }}>
      {/* Body silhouette */}
      <ellipse cx="50" cy="14" rx="8" ry="9" fill={t.surface2} stroke={t.border}/>
      <path d="M30 20 L70 20 L72 50 L66 80 L60 95 L55 95 L52 60 L48 60 L45 95 L40 95 L34 80 L28 50 Z"
            fill={t.surface2} stroke={t.border}/>
      {muscles.map(m => (
        <circle key={m.name} cx={m.cx} cy={m.cy} r={5} fill={colorAt(m.level)} opacity={0.85}/>
      ))}
    </svg>
  );
}

function HomeScreen({ theme }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const days = lang === 'ru' ? ['Пн','Вт','Ср','Чт','Пт','Сб','Вс'] : ['Mo','Tu','We','Th','Fr','Sa','Su'];

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      {/* Header */}
      <div style={{ padding: '8px 18px 4px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
          <Avatar name={ME.name} size={42} color={t.p3} color2={t.p2}/>
          <div>
            <div style={{ fontSize: 12, color: t.textDim, fontWeight: 500 }}>{tr('greeting')}</div>
            <div style={{ fontSize: 16, fontWeight: 700, color: t.text }}>{ME.name}</div>
          </div>
        </div>
        <div style={{ display: 'flex', gap: 8 }}>
          <div style={{
            display: 'flex', alignItems: 'center', gap: 4, padding: '6px 10px',
            background: t.surface, border: `1px solid ${t.border}`, borderRadius: 12,
          }}>
            <Icon name="flame" size={14} color={t.danger}/>
            <span style={{ fontSize: 13, fontWeight: 700, color: t.text }}>{ME.streak}</span>
          </div>
          <div style={{
            display: 'flex', alignItems: 'center', gap: 4, padding: '6px 10px',
            background: t.surface, border: `1px solid ${t.border}`, borderRadius: 12,
          }}>
            <Icon name="bolt" size={14} color={t.warn}/>
            <span style={{ fontSize: 13, fontWeight: 700, color: t.text }}>{ME.coins}</span>
          </div>
        </div>
      </div>

      {/* Hero — Today's plan */}
      <div style={{ padding: '16px 18px 0' }}>
        <div style={{
          background: t.grad, borderRadius: 20, padding: 18, color: '#fff',
          position: 'relative', overflow: 'hidden',
          boxShadow: '0 12px 32px rgba(0,194,255,0.25)',
        }}>
          <div style={{
            position: 'absolute', right: -30, top: -30, width: 160, height: 160,
            borderRadius: '50%', background: 'rgba(255,255,255,0.12)',
          }}/>
          <div style={{
            position: 'absolute', right: 20, bottom: -20, width: 80, height: 80,
            borderRadius: '50%', background: 'rgba(255,255,255,0.08)',
          }}/>
          <div style={{ fontSize: 11, fontWeight: 700, letterSpacing: 1, opacity: 0.85, textTransform: 'uppercase' }}>{tr('todayPlan')}</div>
          <div style={{ fontSize: 22, fontWeight: 800, marginTop: 4, letterSpacing: -0.5 }}>Upper body — Monday</div>
          <div style={{ display: 'flex', gap: 14, marginTop: 12, fontSize: 13, opacity: 0.95 }}>
            <span>⏱ 65 min</span><span>· 6 ex</span><span>· 28 sets</span>
          </div>
          <button style={{
            marginTop: 16, padding: '10px 18px', borderRadius: 12, border: 'none',
            background: '#fff', color: '#0B1020', fontWeight: 700, fontSize: 14, cursor: 'pointer',
            display: 'flex', alignItems: 'center', gap: 8, fontFamily: 'inherit',
          }}>
            <Icon name="play" size={14} color="#0B1020"/>
            {tr('startWorkout')}
          </button>
        </div>
      </div>

      {/* Streak row */}
      <div style={{ padding: '0 18px', marginTop: 18 }}>
        <Card padding={16}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 12 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <Icon name="flame" size={18} color={t.danger}/>
              <span style={{ fontSize: 15, fontWeight: 700, color: t.text }}>{tr('streak')}</span>
              <span style={{ fontSize: 13, color: t.textMuted }}>· {ME.streak} {tr('streakDays')}</span>
            </div>
            <Icon name="chevron-right" size={16} color={t.textDim}/>
          </div>
          <div style={{ display: 'flex', gap: 4 }}>
            {STREAK_WEEK.map((v, i) => <StreakDay key={i} day={days[i]} value={v} theme={t}/>)}
          </div>
        </Card>
      </div>

      {/* Quests */}
      <SectionHeader title={tr('achievements')} action={tr('done')+' →'} theme={t}/>
      <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 10 }}>
        {QUESTS.map(q => (
          <Card key={q.id} padding={14}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 }}>
              <div style={{ fontSize: 14, fontWeight: 600, color: t.text, flex: 1 }}>
                {lang === 'ru' ? q.textRu : q.text}
              </div>
              <div style={{
                padding: '4px 10px', borderRadius: 10, background: t.gradSoft,
                color: t.p2, fontSize: 11, fontWeight: 700,
              }}>+{q.xp} XP</div>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
              <ProgressBar value={q.progress} max={q.total} height={6} style={{ flex: 1 }}/>
              <span style={{ fontSize: 12, color: t.textMuted, fontWeight: 600 }}>{q.progress}/{q.total}</span>
            </div>
          </Card>
        ))}
      </div>

      {/* Stats grid */}
      <SectionHeader title={tr('thisWeek')} theme={t}/>
      <div style={{ padding: '0 18px', display: 'flex', gap: 10 }}>
        <StatTile value={ME.workoutsThisWeek} label={tr('workout')} icon="dumbbell" color={t.p2}/>
        <StatTile value="4h 20m" label={tr('clock') || 'Time'} icon="clock" color={t.p1}/>
        <StatTile value="12.4t" label="Volume" icon="chart" color={t.p3}/>
      </div>

      {/* Muscle activity */}
      <SectionHeader title={tr('muscleHeatmap')} theme={t}/>
      <div style={{ padding: '0 18px' }}>
        <Card padding={16}>
          <div style={{ display: 'flex', gap: 14, alignItems: 'center' }}>
            <MuscleHeatmap theme={t}/>
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 10 }}>
              {[
                { label: lang === 'ru' ? 'Грудь' : 'Chest', val: 70, c: t.danger },
                { label: lang === 'ru' ? 'Ноги' : 'Legs', val: 85, c: t.danger },
                { label: lang === 'ru' ? 'Спина' : 'Back', val: 45, c: t.warn },
                { label: lang === 'ru' ? 'Кор' : 'Core', val: 25, c: t.p1 },
              ].map((m, i) => (
                <div key={i}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, marginBottom: 4 }}>
                    <span style={{ color: t.textMuted, fontWeight: 600 }}>{m.label}</span>
                    <span style={{ color: t.text, fontWeight: 700 }}>{m.val}%</span>
                  </div>
                  <ProgressBar value={m.val} color={m.c} height={5}/>
                </div>
              ))}
            </div>
          </div>
        </Card>
      </div>

      {/* Recent routines */}
      <SectionHeader title={tr('recentRoutines')} action="→" theme={t}/>
      <div style={{ paddingLeft: 18, display: 'flex', gap: 10, overflowX: 'auto', paddingBottom: 4, paddingRight: 18 }}>
        {ROUTINES.slice(0, 4).map(r => (
          <div key={r.id} style={{
            minWidth: 180, padding: 14, borderRadius: 16,
            background: `linear-gradient(135deg, ${r.color}22, ${r.color}11)`,
            border: `1px solid ${r.color}33`,
            position: 'relative', overflow: 'hidden', flexShrink: 0,
          }}>
            <div style={{
              width: 36, height: 36, borderRadius: 10,
              background: r.color, display: 'flex', alignItems: 'center', justifyContent: 'center',
              marginBottom: 10,
            }}>
              <Icon name="dumbbell" size={18} color="#fff"/>
            </div>
            <div style={{ fontSize: 14, fontWeight: 700, color: t.text, marginBottom: 4 }}>
              {lang === 'ru' ? r.nameRu : r.name}
            </div>
            <div style={{ fontSize: 11, color: t.textMuted }}>{r.sets} sets · {r.duration} min</div>
          </div>
        ))}
      </div>
    </div>
  );
}

window.HomeScreen = HomeScreen;
