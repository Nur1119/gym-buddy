// screens-profile.jsx — Profile, Settings, Edit Profile, Calendar, Nutrition, Friends, Ranks

function ProfileScreen({ theme, onNav }) {
  const t = theme;
  const { t: tr, lang } = useI18n();

  const widgets = [
    { id: 'medals', icon: 'medal', label: tr('medals'), color: t.warn, nav: 'medals' },
    { id: 'ranks', icon: 'trophy', label: tr('ranks'), color: t.p3, nav: 'ranks' },
    { id: 'cal', icon: 'calendar', label: tr('calendar'), color: t.p2, nav: 'calendar' },
    { id: 'nutr', icon: 'apple', label: tr('nutrition'), color: t.p1, nav: 'nutrition' },
    { id: 'stats', icon: 'chart', label: tr('stats'), color: t.danger, nav: 'stats' },
    { id: 'edit', icon: 'edit', label: tr('editProfile'), color: t.p2, nav: 'editProfile' },
  ];

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      {/* Header */}
      <div style={{ padding: '4px 18px 12px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
          <Avatar name={ME.name} size={42} color={t.p3} color2={t.p2}/>
          <div>
            <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600 }}>Lv.{ME.level}</div>
            <div style={{ width: 88, height: 4, background: t.chip, borderRadius: 2, marginTop: 2 }}>
              <div style={{ width: '74%', height: '100%', background: t.grad, borderRadius: 2 }}/>
            </div>
          </div>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 4, padding: '6px 10px', background: t.surface, border: `1px solid ${t.border}`, borderRadius: 12 }}>
            <Icon name="flame" size={14} color={t.danger}/>
            <span style={{ fontSize: 13, fontWeight: 700, color: t.text }}>{ME.streak}</span>
          </div>
          <IconBtn name="cog" onClick={() => onNav('settings')}/>
        </div>
      </div>

      {/* Profile hero */}
      <div style={{ padding: '0 18px 16px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <div style={{ position: 'relative', marginBottom: 12 }}>
          <div style={{ width: 110, height: 110, borderRadius: 55, overflow: 'hidden', border: `4px solid ${t.surface}`, boxShadow: `0 0 0 3px ${t.p2}` }}>
            <PhotoSlot color={t.p3} color2={t.p2} style={{ width: '100%', height: '100%' }} label="your photo"/>
          </div>
          <div style={{
            position: 'absolute', bottom: 0, right: 0,
            width: 32, height: 32, borderRadius: 16, background: t.grad,
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            border: `3px solid ${t.bg}`,
          }}>
            <Icon name="camera" size={14} color="#fff"/>
          </div>
        </div>
        <div style={{ fontSize: 22, fontWeight: 800, color: t.text }}>{ME.name}</div>
        <div style={{ fontSize: 13, color: t.textMuted, marginTop: 2 }}>{ME.username}</div>

        {/* Level + XP */}
        <Card padding={14} style={{ marginTop: 16, width: '100%' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 14 }}>
            <div style={{
              width: 56, height: 56, position: 'relative',
            }}>
              <svg viewBox="0 0 56 56" style={{ position: 'absolute', inset: 0 }}>
                <polygon points="28,4 50,18 50,38 28,52 6,38 6,18"
                  fill="none" stroke={t.border} strokeWidth="3"/>
                <polygon points="28,4 50,18 50,38 28,52 6,38 6,18"
                  fill="none" stroke="url(#hxg)" strokeWidth="3"
                  strokeDasharray="200" strokeDashoffset="52" strokeLinejoin="round"/>
                <defs>
                  <linearGradient id="hxg" x1="0" y1="0" x2="1" y2="1">
                    <stop offset="0" stopColor={t.p3}/>
                    <stop offset="0.5" stopColor={t.p2}/>
                    <stop offset="1" stopColor={t.p1}/>
                  </linearGradient>
                </defs>
              </svg>
              <div style={{ position: 'absolute', inset: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 18, fontWeight: 800, color: t.text }}>{ME.level}</div>
            </div>
            <div style={{ flex: 1 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4 }}>
                <span style={{ fontSize: 13, fontWeight: 600, color: t.text }}>{ME.xp} / {ME.xpToNext} XP</span>
                <span style={{ fontSize: 13, fontWeight: 700, color: t.p2 }}>Lv.{ME.level + 1}</span>
              </div>
              <ProgressBar value={ME.xp} max={ME.xpToNext} height={8}/>
              <div style={{ fontSize: 11, color: t.textDim, marginTop: 6 }}>
                <Icon name="spark" size={10} color={t.p2}/> {ME.totalXp.toLocaleString()} {lang === 'ru' ? 'всего XP' : 'total XP'}
              </div>
            </div>
          </div>
        </Card>
      </div>

      {/* Widgets grid */}
      <div style={{ padding: '0 18px' }}>
        <div style={{
          display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 10,
        }}>
          {widgets.map(w => (
            <button key={w.id} onClick={() => onNav(w.nav)} style={{
              background: t.surface, border: `1px solid ${t.border}`,
              borderRadius: 14, padding: 14, cursor: 'pointer',
              display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8,
              fontFamily: 'inherit',
            }}>
              <div style={{
                width: 44, height: 44, borderRadius: 12,
                background: `linear-gradient(135deg, ${w.color}, ${w.color}99)`,
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                boxShadow: `0 6px 14px ${w.color}33`,
              }}>
                <Icon name={w.icon} size={22} color="#fff"/>
              </div>
              <div style={{ fontSize: 12, fontWeight: 700, color: t.text }}>{w.label}</div>
            </button>
          ))}
        </div>
      </div>

      {/* Calendar mini */}
      <SectionHeader title={tr('calendar')} action={lang === 'ru' ? 'Все →' : 'View all →'} onAction={() => onNav('calendar')} theme={t}/>
      <div style={{ padding: '0 18px' }}>
        <Card padding={14}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: 4, marginBottom: 6 }}>
            {(lang === 'ru' ? ['Пн','Вт','Ср','Чт','Пт','Сб','Вс'] : ['Mo','Tu','We','Th','Fr','Sa','Su']).map((d, i) => (
              <div key={i} style={{ textAlign: 'center', fontSize: 10, color: t.textDim, fontWeight: 700 }}>{d}</div>
            ))}
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: 4 }}>
            {[18, 19, 20, 21, 22, 23, 24].map(d => {
              const w = WORKOUT_HISTORY[d];
              const today = d === 24;
              return (
                <div key={d} style={{
                  aspectRatio: '1', borderRadius: 8,
                  background: w ? t.gradSoft : 'transparent',
                  border: today ? `2px solid ${t.p2}` : `1px solid ${t.border}`,
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  position: 'relative',
                }}>
                  <span style={{ fontSize: 12, color: today ? t.p2 : (w ? t.text : t.textDim), fontWeight: today ? 800 : 600 }}>{d}</span>
                  {w && <div style={{ position: 'absolute', bottom: 3, width: 4, height: 4, borderRadius: 2, background: t.p2 }}/>}
                </div>
              );
            })}
          </div>
        </Card>
      </div>
    </div>
  );
}

// ── Settings
function SettingsScreen({ theme, onBack, onNav, onToggleTheme, onChangeLang }) {
  const t = theme;
  const { t: tr, lang } = useI18n();

  const Row = ({ icon, label, value, danger, color, onClick, last }) => (
    <button onClick={onClick} style={{
      display: 'flex', alignItems: 'center', gap: 14, padding: '14px 16px',
      background: 'transparent', border: 'none', width: '100%',
      borderBottom: last ? 'none' : `1px solid ${t.border}`,
      cursor: 'pointer', fontFamily: 'inherit', textAlign: 'left',
    }}>
      <div style={{
        width: 32, height: 32, borderRadius: 8, background: (color || t.p2) + '22',
        color: color || t.p2, display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
      }}>
        <Icon name={icon} size={18}/>
      </div>
      <div style={{ flex: 1, fontSize: 15, fontWeight: 600, color: danger ? t.danger : t.text }}>{label}</div>
      {value && <div style={{ fontSize: 13, color: t.textMuted, fontWeight: 600 }}>{value}</div>}
      {!danger && <Icon name="chevron-right" size={16} color={t.textDim}/>}
    </button>
  );

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 30 }}>
      <ScreenHeader
        title={tr('settings')}
        left={<IconBtn name="arrow-left" onClick={onBack}/>}
        large theme={t}
      />

      {/* Pro banner */}
      <div style={{ padding: '0 18px 16px' }}>
        <div style={{
          padding: 16, borderRadius: 16, background: t.grad, color: '#fff',
          display: 'flex', alignItems: 'center', gap: 12, position: 'relative', overflow: 'hidden',
        }}>
          <div style={{
            position: 'absolute', right: -20, top: -20, width: 100, height: 100,
            borderRadius: '50%', background: 'rgba(255,255,255,0.1)',
          }}/>
          <div style={{ width: 44, height: 44, borderRadius: 12, background: 'rgba(255,255,255,0.25)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Icon name="spark" size={22} color="#fff"/>
          </div>
          <div style={{ flex: 1 }}>
            <div style={{ fontSize: 15, fontWeight: 800 }}>GymBuddy Pro</div>
            <div style={{ fontSize: 12, opacity: 0.9 }}>{lang === 'ru' ? 'Безлимит свайпов, бусты, аналитика' : 'Unlimited swipes, boosts, analytics'}</div>
          </div>
          <Icon name="chevron-right" size={18} color="#fff"/>
        </div>
      </div>

      {/* Section: Preferences */}
      <div style={{ padding: '0 18px 6px', fontSize: 11, fontWeight: 700, color: t.textDim, letterSpacing: 1, textTransform: 'uppercase' }}>
        {lang === 'ru' ? 'Настройки' : 'Preferences'}
      </div>
      <div style={{ padding: '0 18px' }}>
        <div style={{ background: t.surface, borderRadius: 14, border: `1px solid ${t.border}`, overflow: 'hidden' }}>
          {/* Theme toggle row */}
          <div style={{
            display: 'flex', alignItems: 'center', gap: 14, padding: '14px 16px',
            borderBottom: `1px solid ${t.border}`,
          }}>
            <div style={{ width: 32, height: 32, borderRadius: 8, background: t.warn + '22', color: t.warn, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Icon name={t.mode === 'dark' ? 'moon' : 'sun'} size={18}/>
            </div>
            <div style={{ flex: 1, fontSize: 15, fontWeight: 600, color: t.text }}>{tr('appearance')}</div>
            <Switch value={t.mode === 'dark'} onChange={onToggleTheme}/>
          </div>
          {/* Lang */}
          <Row icon="language" label={tr('language')} value={lang === 'ru' ? 'Русский' : 'English'} color={t.p2} onClick={onChangeLang}/>
          <Row icon="dumbbell" label={tr('units')} value="kg / cm" color={t.p1}/>
          <Row icon="bell" label={tr('notifications')} value={lang === 'ru' ? 'Включены' : 'On'} color={t.warn}/>
          <Row icon="chart" label={lang === 'ru' ? 'Аналитика' : 'Analytics'} color={t.p3}/>
          <Row icon="calendar" label={tr('calendar')} color={t.p2}/>
          <Row icon="cog" label={lang === 'ru' ? 'Другие настройки' : 'Other'} color={t.textMuted} last/>
        </div>
      </div>

      {/* Section: Account */}
      <div style={{ padding: '20px 18px 6px', fontSize: 11, fontWeight: 700, color: t.textDim, letterSpacing: 1, textTransform: 'uppercase' }}>
        {lang === 'ru' ? 'Аккаунт' : 'Account'}
      </div>
      <div style={{ padding: '0 18px' }}>
        <div style={{ background: t.surface, borderRadius: 14, border: `1px solid ${t.border}`, overflow: 'hidden' }}>
          <Row icon="user" label={lang === 'ru' ? 'Профиль' : 'Profile'} color={t.p2} onClick={() => onNav('editProfile')}/>
          <Row icon="medal" label={tr('achievements')} color={t.warn}/>
          <Row icon="users" label={lang === 'ru' ? 'Реферальная программа' : 'Referrals'} color={t.p1}/>
          <Row icon="trophy" label={tr('stats')} color={t.danger} last/>
        </div>
      </div>

      {/* Section: Legal */}
      <div style={{ padding: '20px 18px 6px', fontSize: 11, fontWeight: 700, color: t.textDim, letterSpacing: 1, textTransform: 'uppercase' }}>
        {lang === 'ru' ? 'Документы' : 'Legal'}
      </div>
      <div style={{ padding: '0 18px' }}>
        <div style={{ background: t.surface, borderRadius: 14, border: `1px solid ${t.border}`, overflow: 'hidden' }}>
          <Row icon="list" label={tr('privacy')} color={t.textMuted}/>
          <Row icon="list" label={tr('terms')} color={t.textMuted} last/>
        </div>
      </div>

      {/* Logout */}
      <div style={{ padding: '24px 18px 0', display: 'flex', justifyContent: 'center' }}>
        <button style={{
          background: 'transparent', border: 'none', color: t.danger,
          fontSize: 15, fontWeight: 700, cursor: 'pointer', fontFamily: 'inherit',
          padding: '10px 20px',
        }}>{tr('logout')}</button>
      </div>
    </div>
  );
}

// ── Edit profile
function EditProfileScreen({ theme, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const [name, setName] = React.useState(ME.name);
  const [age, setAge] = React.useState(ME.age);
  const [height, setHeight] = React.useState(ME.height);
  const [weight, setWeight] = React.useState(ME.weight);
  const [bio, setBio] = React.useState(lang === 'ru' ? ME.bioRu : ME.bio);

  const Field = ({ label, children }) => (
    <div>
      <div style={{ fontSize: 12, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginBottom: 6, paddingLeft: 4 }}>{label}</div>
      {children}
    </div>
  );

  const inputStyle = {
    width: '100%', padding: '14px 16px', borderRadius: 12,
    border: `1px solid ${t.border}`, background: t.surface,
    color: t.text, fontSize: 15, fontFamily: 'inherit', outline: 'none', boxSizing: 'border-box',
  };

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      <ScreenHeader
        title={tr('editProfile')}
        left={<button onClick={onBack} style={{ border: 'none', background: 'transparent', color: t.p2, fontWeight: 600, cursor: 'pointer', fontFamily: 'inherit', fontSize: 15 }}>{tr('cancel')}</button>}
        right={<button onClick={onBack} style={{ border: 'none', background: 'transparent', color: t.p2, fontWeight: 700, cursor: 'pointer', fontFamily: 'inherit', fontSize: 15 }}>{tr('save')}</button>}
        large theme={t}
      />

      {/* Photo grid */}
      <div style={{ padding: '0 18px 18px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 8 }}>
          {[0, 1, 2, 3, 4, 5].map(i => (
            <div key={i} style={{
              aspectRatio: '3/4', borderRadius: 12, overflow: 'hidden', position: 'relative',
              border: `1px solid ${t.border}`,
            }}>
              {i < 2 ? (
                <PhotoSlot color={t.p3} color2={t.p2} style={{ width: '100%', height: '100%' }} label={`photo ${i+1}`}/>
              ) : (
                <div style={{
                  width: '100%', height: '100%', background: t.surface,
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  border: `1px dashed ${t.borderStrong}`,
                }}>
                  <Icon name="plus" size={20} color={t.textDim}/>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 14 }}>
        <Field label={tr('name')}>
          <input value={name} onChange={e => setName(e.target.value)} style={inputStyle}/>
        </Field>

        <div style={{ display: 'flex', gap: 10 }}>
          <Field label={tr('age')}>
            <input value={age} onChange={e => setAge(e.target.value)} style={inputStyle} type="number"/>
          </Field>
          <Field label={`${tr('height')} (cm)`}>
            <input value={height} onChange={e => setHeight(e.target.value)} style={inputStyle} type="number"/>
          </Field>
          <Field label={`${tr('weightLabel')} (kg)`}>
            <input value={weight} onChange={e => setWeight(e.target.value)} style={inputStyle} type="number"/>
          </Field>
        </div>

        <Field label={tr('bio')}>
          <textarea value={bio} onChange={e => setBio(e.target.value)} style={{ ...inputStyle, minHeight: 80, resize: 'none' }}/>
        </Field>

        <Field label={tr('goal')}>
          <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
            {['Strength','Hypertrophy','Mobility','Calisthenics','CrossFit'].map(g => (
              <Pill key={g} active={g === 'Hypertrophy'} color={t.p2}>{g}</Pill>
            ))}
          </div>
        </Field>

        <Field label={lang === 'ru' ? 'Интересы' : 'Interests'}>
          <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
            {['Powerlifting','Bodybuilding','Calisthenics','Running','Yoga','CrossFit','HIIT'].map((it, i) => (
              <Pill key={it} active={i < 3} color={t.p3}>{it}</Pill>
            ))}
          </div>
        </Field>
      </div>
    </div>
  );
}

// ── Calendar (full screen)
function CalendarScreen({ theme, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const days = lang === 'ru' ? ['Пн','Вт','Ср','Чт','Пт','Сб','Вс'] : ['Mo','Tu','We','Th','Fr','Sa','Su'];

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      <ScreenHeader
        title={tr('calendar')}
        left={<IconBtn name="arrow-left" onClick={onBack}/>}
        right={<IconBtn name="plus" bg={t.grad} color="#fff"/>}
        large theme={t}
      />

      {/* Month switcher */}
      <div style={{ padding: '0 18px 12px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ fontSize: 17, fontWeight: 700, color: t.text }}>{lang === 'ru' ? 'Май 2026' : 'May 2026'}</div>
        <div style={{ display: 'flex', gap: 6 }}>
          <IconBtn name="chevron-left" size={32} iconSize={16}/>
          <IconBtn name="chevron-right" size={32} iconSize={16}/>
        </div>
      </div>

      <div style={{ padding: '0 18px' }}>
        <Card padding={12}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: 4, marginBottom: 6 }}>
            {days.map((d, i) => (
              <div key={i} style={{ textAlign: 'center', fontSize: 10, color: t.textDim, fontWeight: 700, padding: '4px 0' }}>{d}</div>
            ))}
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: 4 }}>
            {Array.from({ length: 35 }).map((_, idx) => {
              const day = idx - 3; // Start offset
              if (day < 1 || day > 31) return <div key={idx} style={{ aspectRatio: '1' }}/>;
              const w = WORKOUT_HISTORY[day];
              const today = day === 24;
              return (
                <div key={idx} style={{
                  aspectRatio: '1', borderRadius: 8,
                  background: w ? t.gradSoft : 'transparent',
                  border: today ? `2px solid ${t.p2}` : `1px solid ${t.border}`,
                  display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
                  position: 'relative', gap: 2,
                }}>
                  <span style={{ fontSize: 13, color: today ? t.p2 : (w ? t.text : t.textDim), fontWeight: today ? 800 : 600 }}>{day}</span>
                  {w && (
                    <div style={{ display: 'flex', gap: 2 }}>
                      {w.muscles.slice(0, 3).map((m, i) => (
                        <div key={i} style={{ width: 4, height: 4, borderRadius: 2, background: t.p2 }}/>
                      ))}
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </Card>
      </div>

      {/* Upcoming workouts */}
      <SectionHeader title={lang === 'ru' ? 'Предстоящие' : 'Upcoming'} theme={t}/>
      <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 10 }}>
        {[
          { date: lang === 'ru' ? 'Сегодня · 18:00' : 'Today · 6 PM', name: lang === 'ru' ? 'Верх тела' : 'Upper body', color: t.p3 },
          { date: lang === 'ru' ? 'Завтра · 07:00' : 'Tomorrow · 7 AM', name: lang === 'ru' ? 'Ноги (с Alina)' : 'Legs (with Alina)', color: t.danger },
          { date: lang === 'ru' ? 'Чт · 19:00' : 'Thu · 7 PM', name: lang === 'ru' ? 'Тяги' : 'Pull day', color: t.p2 },
        ].map((w, i) => (
          <Card key={i} padding={14}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
              <div style={{ width: 6, height: 36, borderRadius: 3, background: w.color }}/>
              <div style={{ flex: 1 }}>
                <div style={{ fontSize: 11, color: t.textMuted, fontWeight: 600 }}>{w.date}</div>
                <div style={{ fontSize: 15, fontWeight: 700, color: t.text, marginTop: 2 }}>{w.name}</div>
              </div>
              <IconBtn name="play" size={32} iconSize={14} bg={w.color} color="#fff"/>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
}

// ── Nutrition
function NutritionScreen({ theme, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const macros = [
    { key: 'protein', label: tr('protein'), val: 142, max: 180, color: t.danger },
    { key: 'carbs', label: tr('carbs'), val: 220, max: 280, color: t.warn },
    { key: 'fats', label: tr('fats'), val: 58, max: 72, color: t.p2 },
  ];

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      <ScreenHeader
        title={tr('nutrition')}
        left={<IconBtn name="arrow-left" onClick={onBack}/>}
        right={<IconBtn name="plus" bg={t.grad} color="#fff"/>}
        large theme={t}
      />

      {/* Calories ring */}
      <div style={{ padding: '0 18px 16px' }}>
        <Card padding={20} style={{ display: 'flex', alignItems: 'center', gap: 20 }}>
          <div style={{ width: 100, height: 100, position: 'relative' }}>
            <svg viewBox="0 0 100 100" style={{ position: 'absolute', inset: 0 }}>
              <circle cx="50" cy="50" r="42" fill="none" stroke={t.chip} strokeWidth="10"/>
              <circle cx="50" cy="50" r="42" fill="none" stroke="url(#cgrad)" strokeWidth="10"
                strokeDasharray="264" strokeDashoffset="60" strokeLinecap="round" transform="rotate(-90 50 50)"/>
              <defs>
                <linearGradient id="cgrad" x1="0" y1="0" x2="1" y2="1">
                  <stop offset="0" stopColor={t.p3}/>
                  <stop offset="1" stopColor={t.p1}/>
                </linearGradient>
              </defs>
            </svg>
            <div style={{ position: 'absolute', inset: 0, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
              <div style={{ fontSize: 22, fontWeight: 800, color: t.text }}>1820</div>
              <div style={{ fontSize: 10, color: t.textDim, fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.5 }}>kcal</div>
            </div>
          </div>
          <div style={{ flex: 1 }}>
            <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.5, marginBottom: 4 }}>{tr('calories')}</div>
            <div style={{ fontSize: 24, fontWeight: 800, color: t.text }}>1820 / 2400</div>
            <div style={{ fontSize: 12, color: t.textMuted, marginTop: 2 }}>
              580 {lang === 'ru' ? 'осталось' : 'remaining'}
            </div>
          </div>
        </Card>
      </div>

      {/* Macros */}
      <div style={{ padding: '0 18px 16px', display: 'flex', gap: 10 }}>
        {macros.map(m => (
          <div key={m.key} style={{
            flex: 1, padding: 12, borderRadius: 14,
            background: t.surface, border: `1px solid ${t.border}`,
          }}>
            <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.3 }}>{m.label}</div>
            <div style={{ fontSize: 18, fontWeight: 800, color: t.text, marginTop: 4 }}>{m.val}<span style={{ fontSize: 11, color: t.textMuted, fontWeight: 600 }}>g</span></div>
            <ProgressBar value={m.val} max={m.max} height={4} color={m.color} style={{ marginTop: 6 }}/>
            <div style={{ fontSize: 10, color: t.textDim, marginTop: 4 }}>/{m.max}g</div>
          </div>
        ))}
      </div>

      {/* Meals */}
      <SectionHeader title={lang === 'ru' ? 'Приёмы пищи' : 'Meals'} theme={t}/>
      <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 10 }}>
        {[
          { meal: tr('breakfast'), kcal: 520, items: ['Oats', '2× eggs', 'Banana'], time: '08:00', emoji: '🥣' },
          { meal: tr('lunch'), kcal: 680, items: ['Chicken', 'Rice', 'Salad'], time: '13:00', emoji: '🥗' },
          { meal: tr('snack'), kcal: 220, items: ['Protein shake', 'Apple'], time: '16:30', emoji: '🍎' },
          { meal: tr('dinner'), kcal: 400, items: lang === 'ru' ? ['—'] : ['—'], time: '19:30', empty: true, emoji: '🍽️' },
        ].map((m, i) => (
          <div key={i} style={{
            display: 'flex', alignItems: 'center', gap: 12, padding: 14,
            background: t.surface, border: `1px solid ${t.border}`, borderRadius: 14,
            opacity: m.empty ? 0.6 : 1,
          }}>
            <div style={{ fontSize: 28 }}>{m.emoji}</div>
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <div style={{ fontSize: 15, fontWeight: 700, color: t.text }}>{m.meal}</div>
                <div style={{ fontSize: 11, color: t.textDim }}>{m.time}</div>
              </div>
              <div style={{ fontSize: 12, color: t.textMuted, marginTop: 2 }}>
                {m.items.join(' · ')}
              </div>
            </div>
            <div style={{ fontSize: 14, fontWeight: 700, color: m.empty ? t.textDim : t.text }}>{m.kcal} kcal</div>
          </div>
        ))}
      </div>
    </div>
  );
}

// ── Friends
function FriendsScreen({ theme, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const [tab, setTab] = React.useState('friends');

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      <ScreenHeader
        title={tr('friends')}
        left={null}
        right={<IconBtn name="plus" bg={t.grad} color="#fff"/>}
        large theme={t}
      />

      {/* Tabs */}
      <div style={{ padding: '0 18px 14px' }}>
        <TabPills active={tab} onChange={setTab} theme={t} tabs={[
          { key: 'friends', label: tr('friends') },
          { key: 'requests', label: tr('requests') },
          { key: 'lb', label: tr('leaderboard') },
        ]}/>
      </div>

      {tab === 'friends' && (
        <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 8 }}>
          {/* Online section */}
          <div style={{ fontSize: 11, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginTop: 4, marginBottom: 4 }}>
            {tr('online')} · {FRIENDS.filter(f => f.online).length}
          </div>
          {FRIENDS.map(f => (
            <div key={f.id} style={{
              display: 'flex', alignItems: 'center', gap: 12, padding: 12,
              background: t.surface, border: `1px solid ${t.border}`, borderRadius: 14,
            }}>
              <div style={{ position: 'relative' }}>
                <Avatar name={f.name} size={48} color={f.color} color2={f.color}/>
                {f.online && <div style={{ position: 'absolute', bottom: 0, right: 0, width: 12, height: 12, borderRadius: 6, background: t.success, border: `2px solid ${t.surface}` }}/>}
              </div>
              <div style={{ flex: 1, minWidth: 0 }}>
                <div style={{ fontSize: 14, fontWeight: 700, color: t.text }}>{f.name}</div>
                <div style={{ fontSize: 11, color: t.textMuted, marginTop: 2, display: 'flex', alignItems: 'center', gap: 6 }}>
                  <span>Lv.{f.level}</span>
                  <span>·</span>
                  <Icon name="flame" size={10} color={t.danger}/>
                  <span>{f.streak}</span>
                  <span>·</span>
                  <span>{f.last}</span>
                </div>
              </div>
              <IconBtn name="send" size={32} iconSize={14}/>
            </div>
          ))}
        </div>
      )}

      {tab === 'lb' && (
        <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 8 }}>
          {/* Top 3 podium */}
          <div style={{ display: 'flex', alignItems: 'flex-end', justifyContent: 'center', gap: 10, padding: '14px 0 18px' }}>
            {[1, 0, 2].map(idx => {
              const u = LEADERBOARD[idx];
              const height = idx === 0 ? 110 : idx === 1 ? 90 : 80;
              return (
                <div key={u.rank} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8 }}>
                  <Avatar name={u.name} size={56} color={u.color} color2={u.color}/>
                  <div style={{ fontSize: 12, fontWeight: 700, color: t.text }}>{u.name}</div>
                  <div style={{
                    width: 70, height, borderRadius: '10px 10px 0 0',
                    background: `linear-gradient(180deg, ${u.color}, ${u.color}88)`,
                    display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'flex-start',
                    padding: 8, color: '#fff',
                  }}>
                    <div style={{ fontSize: 22, fontWeight: 900 }}>{u.rank}</div>
                    <div style={{ fontSize: 10, fontWeight: 700, opacity: 0.9 }}>{(u.xp / 1000).toFixed(0)}k XP</div>
                  </div>
                </div>
              );
            })}
          </div>
          {/* Rest */}
          {LEADERBOARD.slice(3).map(u => (
            <div key={u.rank} style={{
              display: 'flex', alignItems: 'center', gap: 12, padding: 12,
              background: u.isMe ? t.gradSoft : t.surface,
              border: `1px solid ${u.isMe ? t.p2 : t.border}`, borderRadius: 14,
            }}>
              <div style={{ width: 28, fontSize: 14, fontWeight: 800, color: t.textMuted, textAlign: 'center' }}>{u.rank}</div>
              <Avatar name={u.name} size={36} color={u.color} color2={u.color}/>
              <div style={{ flex: 1, fontSize: 14, fontWeight: 700, color: t.text }}>{u.name}</div>
              <div style={{ fontSize: 13, fontWeight: 700, color: t.p2 }}>{u.xp.toLocaleString()}</div>
            </div>
          ))}
        </div>
      )}

      {tab === 'requests' && (
        <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: 200, gap: 8 }}>
          <Icon name="users" size={42} color={t.textDim}/>
          <div style={{ color: t.textMuted, fontSize: 14, fontWeight: 600 }}>
            {lang === 'ru' ? 'Нет новых заявок' : 'No new requests'}
          </div>
        </div>
      )}
    </div>
  );
}

// ── Medals
function MedalsScreen({ theme, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      <ScreenHeader
        title={tr('medals')}
        left={<IconBtn name="arrow-left" onClick={onBack}/>}
        large theme={t}
      />

      <div style={{ padding: '0 18px 14px', display: 'flex', alignItems: 'center', gap: 14 }}>
        <div style={{ fontSize: 38, fontWeight: 900, color: t.text, lineHeight: 1 }}>{MEDALS.filter(m => m.unlocked).length}<span style={{ fontSize: 18, color: t.textDim }}>/{MEDALS.length}</span></div>
        <div style={{ flex: 1 }}>
          <ProgressBar value={MEDALS.filter(m => m.unlocked).length} max={MEDALS.length} height={8}/>
          <div style={{ fontSize: 11, color: t.textMuted, marginTop: 6 }}>
            {lang === 'ru' ? 'Разблокировано медалей' : 'Medals unlocked'}
          </div>
        </div>
      </div>

      <div style={{ padding: '0 18px', display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 10 }}>
        {MEDALS.map(m => (
          <Card key={m.id} padding={16}>
            <div style={{
              width: 60, height: 60, borderRadius: 30, margin: '0 auto 10px',
              background: m.unlocked ? t.grad : t.chip,
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              fontSize: 30, filter: m.unlocked ? 'none' : 'grayscale(1)',
              boxShadow: m.unlocked ? `0 8px 18px ${t.p2}33` : 'none',
            }}>{m.icon}</div>
            <div style={{ fontSize: 12, fontWeight: 700, color: m.unlocked ? t.text : t.textDim, textAlign: 'center', lineHeight: 1.3 }}>
              {lang === 'ru' ? m.nameRu : m.name}
            </div>
            {!m.unlocked && <div style={{ fontSize: 10, color: t.textDim, textAlign: 'center', marginTop: 4 }}>🔒</div>}
          </Card>
        ))}
      </div>
    </div>
  );
}

// ── Ranks (Leaderboards detail)
function RanksScreen({ theme, onBack }) {
  return <FriendsScreen theme={theme} onBack={onBack}/>; // share LB
}

Object.assign(window, { ProfileScreen, SettingsScreen, EditProfileScreen, CalendarScreen, NutritionScreen, FriendsScreen, MedalsScreen, RanksScreen });
