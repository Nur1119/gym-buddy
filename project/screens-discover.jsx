// screens-discover.jsx — Tinder-style swipe + matches + chat + filters + boost

// ── Swipe Card (front view of a user)
function SwipeCard({ user, offset, rotation, theme, language, top = false }) {
  const t = theme;
  const lang = language;
  const opa = top ? 1 : 0.7;

  // Show "LIKE" / "NOPE" badge based on offset
  const showLike = offset > 30;
  const showNope = offset < -30;

  return (
    <div style={{
      position: 'absolute', inset: 0,
      borderRadius: 20, overflow: 'hidden',
      transform: `translateX(${offset}px) rotate(${rotation}deg)`,
      transformOrigin: 'bottom center',
      transition: top ? 'none' : 'transform .2s',
      boxShadow: '0 20px 50px rgba(0,0,0,0.25), 0 0 0 1px rgba(0,0,0,0.05)',
      opacity: opa,
      background: '#111',
    }}>
      {/* Photo */}
      <PhotoSlot color={user.color} color2={user.color2} style={{ width: '100%', height: '100%', position: 'absolute', inset: 0 }} label="user photo"/>

      {/* Photo indicator (top dots) */}
      <div style={{
        position: 'absolute', top: 12, left: 12, right: 12, display: 'flex', gap: 4, zIndex: 3,
      }}>
        {Array.from({ length: user.photos }).map((_, i) => (
          <div key={i} style={{
            flex: 1, height: 3, borderRadius: 1.5,
            background: i === 0 ? '#fff' : 'rgba(255,255,255,0.35)',
          }}/>
        ))}
      </div>

      {/* LIKE / NOPE badges */}
      {showLike && (
        <div style={{
          position: 'absolute', top: 36, left: 20, zIndex: 4,
          border: `4px solid ${t.like}`, borderRadius: 10,
          padding: '4px 12px', transform: 'rotate(-18deg)',
          color: t.like, fontWeight: 900, fontSize: 28, letterSpacing: 2,
          background: 'rgba(0,0,0,0.2)',
        }}>LIKE</div>
      )}
      {showNope && (
        <div style={{
          position: 'absolute', top: 36, right: 20, zIndex: 4,
          border: `4px solid ${t.nope}`, borderRadius: 10,
          padding: '4px 12px', transform: 'rotate(18deg)',
          color: t.nope, fontWeight: 900, fontSize: 28, letterSpacing: 2,
          background: 'rgba(0,0,0,0.2)',
        }}>NOPE</div>
      )}

      {/* Gradient overlay for text */}
      <div style={{
        position: 'absolute', left: 0, right: 0, bottom: 0, height: '55%',
        background: 'linear-gradient(180deg, transparent 0%, rgba(0,0,0,0.85) 100%)',
        zIndex: 2,
      }}/>

      {/* Info */}
      <div style={{
        position: 'absolute', left: 18, right: 18, bottom: 18, zIndex: 3, color: '#fff',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 8 }}>
          <span style={{ fontSize: 28, fontWeight: 800, letterSpacing: -0.5 }}>{user.name}</span>
          <span style={{ fontSize: 24, fontWeight: 400, opacity: 0.95 }}>{user.age}</span>
          <div style={{
            marginLeft: 'auto',
            display: 'flex', alignItems: 'center', gap: 4, padding: '4px 8px',
            borderRadius: 999, background: 'rgba(0,0,0,0.4)', backdropFilter: 'blur(8px)',
            fontSize: 11, fontWeight: 600,
          }}>
            <div style={{ width: 6, height: 6, borderRadius: 3, background: '#3DDC97' }}/>
            online
          </div>
        </div>

        {/* Goal & gym chips */}
        <div style={{ display: 'flex', gap: 6, marginBottom: 10, flexWrap: 'wrap' }}>
          <div style={{
            padding: '5px 10px', borderRadius: 999,
            background: 'rgba(255,255,255,0.18)', backdropFilter: 'blur(8px)',
            fontSize: 11, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 4,
          }}>
            <Icon name="dumbbell" size={11} color="#fff"/>
            {lang === 'ru' ? user.goalRu : user.goal}
          </div>
          <div style={{
            padding: '5px 10px', borderRadius: 999,
            background: 'rgba(255,255,255,0.18)', backdropFilter: 'blur(8px)',
            fontSize: 11, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 4,
          }}>
            <Icon name="location" size={11} color="#fff"/>
            {user.distance} {lang === 'ru' ? 'км' : 'km'}
          </div>
          <div style={{
            padding: '5px 10px', borderRadius: 999,
            background: 'rgba(255,255,255,0.18)', backdropFilter: 'blur(8px)',
            fontSize: 11, fontWeight: 600,
          }}>
            {lang === 'ru' ? user.levelRu : user.level}
          </div>
        </div>

        {/* Bio preview */}
        <div style={{ fontSize: 13, lineHeight: 1.4, opacity: 0.95 }}>
          {lang === 'ru' ? user.bioRu : user.bio}
        </div>
      </div>
    </div>
  );
}

// ── Action button (large round buttons under cards)
function ActionBtn({ icon, color, size = 56, onClick, glow }) {
  return (
    <button onClick={onClick} style={{
      width: size, height: size, borderRadius: size / 2,
      border: 'none', cursor: 'pointer',
      background: '#fff', color,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      boxShadow: `0 6px 20px rgba(0,0,0,0.15), 0 0 0 2px ${color}22${glow ? `, 0 0 30px ${color}88` : ''}`,
      transition: 'transform .12s',
    }}>
      <Icon name={icon} size={size * 0.45} color={color}/>
    </button>
  );
}

// ── Main Discover screen (swipe cards)
function DiscoverScreen({ theme, onNav }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const [stack, setStack] = React.useState(DISCOVER_USERS);
  const [offset, setOffset] = React.useState(0);
  const [matchedUser, setMatchedUser] = React.useState(null);
  const startX = React.useRef(0);
  const dragging = React.useRef(false);

  const handlePointerDown = (e) => {
    dragging.current = true;
    startX.current = e.clientX;
  };
  const handlePointerMove = (e) => {
    if (!dragging.current) return;
    setOffset(e.clientX - startX.current);
  };
  const handlePointerUp = () => {
    if (!dragging.current) return;
    dragging.current = false;
    if (Math.abs(offset) > 80) {
      const direction = offset > 0 ? 'right' : 'left';
      doSwipe(direction);
    } else {
      setOffset(0);
    }
  };

  const doSwipe = (dir) => {
    const top = stack[0];
    // Animate out
    setOffset(dir === 'right' ? 500 : -500);
    setTimeout(() => {
      setStack(prev => prev.slice(1));
      setOffset(0);
      // Random match on right swipe
      if (dir === 'right' && Math.random() > 0.4) {
        setMatchedUser(top);
      }
    }, 250);
  };

  const rotation = offset / 20;

  return (
    <div style={{
      background: t.bg, minHeight: '100%', display: 'flex', flexDirection: 'column',
    }}>
      {/* Header */}
      <div style={{ padding: '4px 18px 8px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <IconBtn name="sliders" onClick={() => onNav('discoverFilters')}/>
        <div style={{
          fontSize: 22, fontWeight: 800,
          background: t.grad, WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent',
          letterSpacing: -0.5,
        }}>GymBuddy</div>
        <IconBtn name="bell" badge={3} onClick={() => onNav('matches')}/>
      </div>

      {/* Card stack */}
      <div style={{ flex: 1, position: 'relative', padding: '4px 16px 0', minHeight: 0 }}>
        <div
          style={{ position: 'relative', width: '100%', height: '100%', minHeight: 380 }}
          onPointerDown={handlePointerDown}
          onPointerMove={handlePointerMove}
          onPointerUp={handlePointerUp}
          onPointerLeave={handlePointerUp}
        >
          {stack.length === 0 ? (
            <div style={{
              position: 'absolute', inset: 0, display: 'flex', flexDirection: 'column',
              alignItems: 'center', justifyContent: 'center', gap: 10, padding: 20,
            }}>
              <Icon name="heart" size={48} color={t.textDim}/>
              <div style={{ color: t.textMuted, textAlign: 'center', fontSize: 14, fontWeight: 600 }}>
                {lang === 'ru' ? 'Все пересмотрели! Загляни позже.' : "You're all caught up! Check back later."}
              </div>
            </div>
          ) : (
            <>
              {stack.slice(0, 3).reverse().map((u, idx) => {
                const isTop = idx === stack.slice(0, 3).length - 1;
                const stackDepth = stack.slice(0, 3).length - 1 - idx;
                return (
                  <div key={u.id} style={{
                    position: 'absolute', inset: 0,
                    transform: !isTop ? `scale(${1 - stackDepth * 0.04}) translateY(${stackDepth * 6}px)` : 'none',
                    opacity: !isTop ? 0.6 : 1,
                  }}>
                    <SwipeCard user={u} offset={isTop ? offset : 0} rotation={isTop ? rotation : 0} theme={t} language={lang} top={isTop}/>
                  </div>
                );
              })}
            </>
          )}
        </div>
      </div>

      {/* Action buttons */}
      <div style={{
        display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 14,
        padding: '14px 18px 14px',
      }}>
        <ActionBtn icon="rewind" color={t.warn} size={44}/>
        <ActionBtn icon="close" color={t.nope} size={56} onClick={() => doSwipe('left')}/>
        <ActionBtn icon="star" color={t.superLike} size={48} glow/>
        <ActionBtn icon="heart-fill" color={t.like} size={56} onClick={() => doSwipe('right')}/>
        <ActionBtn icon="bolt" color={t.boost} size={44} glow/>
      </div>

      {/* Match modal */}
      {matchedUser && <MatchModal user={matchedUser} theme={t} onClose={() => setMatchedUser(null)} onChat={() => { setMatchedUser(null); onNav('chat', matchedUser); }}/>}
    </div>
  );
}

// ── Match modal
function MatchModal({ user, theme, onClose, onChat }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  return (
    <div style={{
      position: 'absolute', inset: 0, zIndex: 100,
      background: 'rgba(0,0,0,0.85)', backdropFilter: 'blur(12px)',
      display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
      padding: 30,
    }}>
      {/* Sparkle bg */}
      <div style={{
        position: 'absolute', top: '30%', left: '50%', transform: 'translate(-50%, -50%)',
        width: 300, height: 300, borderRadius: '50%',
        background: t.grad, opacity: 0.3, filter: 'blur(60px)',
      }}/>

      <div style={{
        fontSize: 38, fontWeight: 900, letterSpacing: -1, marginBottom: 6,
        background: t.grad, WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent',
        position: 'relative',
      }}>
        {tr('itsAMatch')}
      </div>
      <div style={{ color: 'rgba(255,255,255,0.75)', fontSize: 14, marginBottom: 28, position: 'relative' }}>
        {lang === 'ru' ? `Вы с ${user.name} понравились друг другу` : `You and ${user.name} liked each other`}
      </div>

      {/* Two avatars */}
      <div style={{ display: 'flex', gap: 14, marginBottom: 32, position: 'relative' }}>
        <div style={{
          width: 110, height: 140, borderRadius: 16, overflow: 'hidden',
          transform: 'rotate(-8deg)', border: '3px solid #fff',
        }}>
          <PhotoSlot color={t.p3} color2={t.p2} style={{ width: '100%', height: '100%' }}/>
        </div>
        <div style={{
          width: 110, height: 140, borderRadius: 16, overflow: 'hidden',
          transform: 'rotate(8deg)', border: '3px solid #fff',
        }}>
          <PhotoSlot color={user.color} color2={user.color2} style={{ width: '100%', height: '100%' }}/>
        </div>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 10, width: '100%', maxWidth: 280, position: 'relative' }}>
        <GradButton icon="send" onClick={onChat}>{tr('sendMessage')}</GradButton>
        <button onClick={onClose} style={{
          padding: 14, borderRadius: 14, border: 'none',
          background: 'rgba(255,255,255,0.15)', color: '#fff', fontWeight: 700, fontSize: 14,
          cursor: 'pointer', fontFamily: 'inherit',
        }}>{lang === 'ru' ? 'Продолжить свайпы' : 'Keep swiping'}</button>
      </div>
    </div>
  );
}

// ── Matches list
function MatchesScreen({ theme, onNav, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  return (
    <div style={{ background: t.bg, minHeight: '100%' }}>
      <ScreenHeader
        title={tr('matches')}
        left={<IconBtn name="arrow-left" onClick={onBack}/>}
        large theme={t}
      />

      {/* New matches strip */}
      <div style={{ padding: '0 18px 16px' }}>
        <div style={{ fontSize: 12, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginBottom: 10 }}>
          {lang === 'ru' ? 'Новые совпадения' : 'New matches'}
        </div>
        <div style={{ display: 'flex', gap: 10, overflowX: 'auto' }}>
          {/* Likes you (premium) */}
          <div style={{
            width: 72, height: 96, borderRadius: 14, flexShrink: 0,
            background: t.grad, display: 'flex', flexDirection: 'column',
            alignItems: 'center', justifyContent: 'center', gap: 4,
            position: 'relative', overflow: 'hidden',
          }}>
            <Icon name="bolt" size={20} color="#fff"/>
            <div style={{ color: '#fff', fontSize: 22, fontWeight: 800 }}>14</div>
            <div style={{ color: '#fff', fontSize: 9, fontWeight: 700, letterSpacing: 0.5, textTransform: 'uppercase', opacity: 0.9 }}>
              {lang === 'ru' ? 'Лайков' : 'Likes'}
            </div>
          </div>
          {MATCHES.map(m => {
            const u = DISCOVER_USERS.find(x => x.id === m.userId);
            if (!u) return null;
            return (
              <div key={m.userId} onClick={() => onNav('chat', u)} style={{
                width: 72, height: 96, borderRadius: 14, flexShrink: 0,
                position: 'relative', overflow: 'hidden', cursor: 'pointer',
                border: m.unread ? `2px solid ${t.p2}` : 'none',
              }}>
                <PhotoSlot color={u.color} color2={u.color2} style={{ width: '100%', height: '100%' }}/>
                <div style={{
                  position: 'absolute', bottom: 0, left: 0, right: 0,
                  background: 'linear-gradient(180deg, transparent, rgba(0,0,0,0.7))',
                  padding: '12px 6px 6px',
                  color: '#fff', fontSize: 11, fontWeight: 700,
                }}>{u.name}</div>
                {m.unread > 0 && (
                  <div style={{
                    position: 'absolute', top: 4, right: 4, minWidth: 18, height: 18,
                    padding: '0 4px', borderRadius: 9, background: t.p2, color: '#fff',
                    fontSize: 10, fontWeight: 700, display: 'flex', alignItems: 'center', justifyContent: 'center',
                  }}>{m.unread}</div>
                )}
              </div>
            );
          })}
        </div>
      </div>

      {/* Messages list */}
      <div style={{ padding: '0 18px' }}>
        <div style={{ fontSize: 12, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginBottom: 10 }}>
          {lang === 'ru' ? 'Сообщения' : 'Messages'}
        </div>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
          {MATCHES.map(m => {
            const u = DISCOVER_USERS.find(x => x.id === m.userId);
            if (!u) return null;
            return (
              <div key={m.userId} onClick={() => onNav('chat', u)} style={{
                display: 'flex', alignItems: 'center', gap: 12,
                padding: 10, borderRadius: 12, cursor: 'pointer',
              }}>
                <div style={{ position: 'relative', flexShrink: 0 }}>
                  <div style={{ width: 54, height: 54, borderRadius: 27, overflow: 'hidden' }}>
                    <PhotoSlot color={u.color} color2={u.color2} style={{ width: '100%', height: '100%' }}/>
                  </div>
                  {m.online && (
                    <div style={{
                      position: 'absolute', bottom: 0, right: 0, width: 14, height: 14,
                      borderRadius: 7, background: '#3DDC97', border: `2px solid ${t.bg}`,
                    }}/>
                  )}
                </div>
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <div style={{ fontSize: 15, fontWeight: 700, color: t.text }}>{u.name}</div>
                    <div style={{ fontSize: 11, color: t.textDim }}>{lang === 'ru' ? m.matchedAtRu : m.matchedAt}</div>
                  </div>
                  <div style={{ fontSize: 13, color: m.unread ? t.text : t.textMuted, fontWeight: m.unread ? 600 : 500, marginTop: 2, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {lang === 'ru' ? m.lastMsgRu : m.lastMsg}
                  </div>
                </div>
                {m.unread > 0 && (
                  <div style={{
                    minWidth: 22, height: 22, padding: '0 6px', borderRadius: 11, background: t.p2,
                    color: '#fff', fontSize: 11, fontWeight: 700, display: 'flex', alignItems: 'center', justifyContent: 'center',
                  }}>{m.unread}</div>
                )}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}

// ── Chat screen
function ChatScreen({ user, theme, onBack, onNav }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const [msgs, setMsgs] = React.useState(CHAT_THREADS[user.id] || []);
  const [draft, setDraft] = React.useState('');

  const send = () => {
    if (!draft.trim()) return;
    setMsgs(prev => [...prev, { from: 'me', text: draft, textRu: draft, time: 'now' }]);
    setDraft('');
  };

  return (
    <div style={{ background: t.bg, height: '100%', display: 'flex', flexDirection: 'column' }}>
      {/* Chat header */}
      <div style={{
        display: 'flex', alignItems: 'center', gap: 10, padding: '4px 14px 12px',
        borderBottom: `1px solid ${t.border}`, background: t.surface,
      }}>
        <IconBtn name="arrow-left" onClick={onBack}/>
        <div style={{ width: 36, height: 36, borderRadius: 18, overflow: 'hidden' }}>
          <PhotoSlot color={user.color} color2={user.color2} style={{ width: '100%', height: '100%' }}/>
        </div>
        <div style={{ flex: 1, minWidth: 0 }}>
          <div style={{ fontSize: 15, fontWeight: 700, color: t.text }}>{user.name}</div>
          <div style={{ fontSize: 11, color: t.success, fontWeight: 600 }}>{tr('online')}</div>
        </div>
        <IconBtn name="dumbbell" color={t.p2} onClick={() => onNav('inviteWorkout', user)}/>
        <IconBtn name="more"/>
      </div>

      {/* Messages */}
      <div style={{ flex: 1, overflowY: 'auto', padding: '14px 14px 8px', display: 'flex', flexDirection: 'column', gap: 8 }}>
        <div style={{
          textAlign: 'center', fontSize: 11, color: t.textDim, padding: '8px 0',
          fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.5,
        }}>
          {tr('matchedOn')} {lang === 'ru' ? 'сегодня' : 'today'} · {user.distance} {lang === 'ru' ? 'км' : 'km'}
        </div>
        {msgs.map((m, i) => {
          const mine = m.from === 'me';
          return (
            <div key={i} style={{
              display: 'flex', justifyContent: mine ? 'flex-end' : 'flex-start',
            }}>
              <div style={{
                maxWidth: '72%', padding: '10px 14px', borderRadius: 18,
                background: mine ? t.grad : t.surface,
                color: mine ? '#fff' : t.text,
                border: mine ? 'none' : `1px solid ${t.border}`,
                fontSize: 14, lineHeight: 1.4,
                borderBottomRightRadius: mine ? 4 : 18,
                borderBottomLeftRadius: mine ? 18 : 4,
              }}>
                {lang === 'ru' ? m.textRu : m.text}
                <div style={{ fontSize: 10, opacity: 0.7, marginTop: 4, textAlign: 'right' }}>{m.time}</div>
              </div>
            </div>
          );
        })}

        {/* Suggested action */}
        <div style={{
          margin: '8px 0', padding: 12, borderRadius: 14,
          background: t.gradSoft, border: `1px solid ${t.p2}44`,
          display: 'flex', alignItems: 'center', gap: 10,
        }}>
          <Icon name="dumbbell" size={20} color={t.p2}/>
          <div style={{ flex: 1 }}>
            <div style={{ fontSize: 13, fontWeight: 700, color: t.text }}>{tr('suggestWorkout')}</div>
            <div style={{ fontSize: 11, color: t.textMuted, marginTop: 2 }}>
              {lang === 'ru' ? 'Назначь совместную тренировку' : 'Schedule a workout together'}
            </div>
          </div>
          <button onClick={() => onNav('inviteWorkout', user)} style={{
            padding: '6px 12px', borderRadius: 10, border: 'none', background: t.p2,
            color: '#fff', fontWeight: 700, fontSize: 12, cursor: 'pointer', fontFamily: 'inherit',
          }}>{tr('add')}</button>
        </div>
      </div>

      {/* Input */}
      <div style={{ padding: '8px 14px 14px', background: t.surface, borderTop: `1px solid ${t.border}`, display: 'flex', alignItems: 'center', gap: 8 }}>
        <IconBtn name="image" bg={t.chip}/>
        <input
          value={draft}
          onChange={e => setDraft(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && send()}
          placeholder={tr('typeMessage')}
          style={{
            flex: 1, padding: '12px 16px', borderRadius: 22,
            border: `1px solid ${t.border}`, background: t.bg,
            color: t.text, fontSize: 14, fontFamily: 'inherit', outline: 'none',
          }}
        />
        <IconBtn name="send" bg={t.grad} color="#fff" onClick={send}/>
      </div>
    </div>
  );
}

// ── Discover filters screen
function DiscoverFiltersScreen({ theme, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  const [ageRange, setAgeRange] = React.useState([21, 35]);
  const [maxDistance, setMaxDistance] = React.useState(15);
  const [goal, setGoal] = React.useState('All');
  const [level, setLevel] = React.useState('All');

  return (
    <div style={{ background: t.bgGrad, minHeight: '100%', paddingBottom: 24 }}>
      <ScreenHeader
        title={tr('filters')}
        left={<IconBtn name="arrow-left" onClick={onBack}/>}
        right={<button style={{ border: 'none', background: 'transparent', color: t.p2, fontWeight: 700, cursor: 'pointer', fontFamily: 'inherit', fontSize: 14 }}>Reset</button>}
        large theme={t}
      />

      <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 18 }}>
        {/* Distance */}
        <Card padding={16}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 10 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <Icon name="location" size={18} color={t.p2}/>
              <span style={{ fontSize: 14, fontWeight: 700, color: t.text }}>
                {lang === 'ru' ? 'Расстояние' : 'Distance'}
              </span>
            </div>
            <span style={{ fontSize: 14, fontWeight: 700, color: t.p2 }}>{maxDistance} km</span>
          </div>
          <input type="range" min="1" max="50" value={maxDistance} onChange={e => setMaxDistance(+e.target.value)} style={{ width: '100%', accentColor: t.p2 }}/>
        </Card>

        {/* Age range */}
        <Card padding={16}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 10 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <Icon name="user" size={18} color={t.p3}/>
              <span style={{ fontSize: 14, fontWeight: 700, color: t.text }}>{tr('age')}</span>
            </div>
            <span style={{ fontSize: 14, fontWeight: 700, color: t.p3 }}>{ageRange[0]} – {ageRange[1]}</span>
          </div>
          <input type="range" min="18" max="65" value={ageRange[1]} onChange={e => setAgeRange([ageRange[0], +e.target.value])} style={{ width: '100%', accentColor: t.p3 }}/>
        </Card>

        {/* Goal */}
        <div>
          <div style={{ fontSize: 12, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginBottom: 8, paddingLeft: 4 }}>
            {tr('goal')}
          </div>
          <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
            {['All','Strength','Hypertrophy','Mobility','Calisthenics','CrossFit','Cardio'].map(g => (
              <Pill key={g} active={goal === g} onClick={() => setGoal(g)} color={t.p1}>{g}</Pill>
            ))}
          </div>
        </div>

        {/* Level */}
        <div>
          <div style={{ fontSize: 12, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginBottom: 8, paddingLeft: 4 }}>
            {tr('level')}
          </div>
          <div style={{ display: 'flex', gap: 6 }}>
            {['All','Beginner','Intermediate','Advanced','Elite'].map(l => (
              <Pill key={l} active={level === l} onClick={() => setLevel(l)} color={t.p2}>{l}</Pill>
            ))}
          </div>
        </div>

        {/* Schedule overlap */}
        <div>
          <div style={{ fontSize: 12, fontWeight: 700, color: t.textDim, letterSpacing: 0.5, textTransform: 'uppercase', marginBottom: 8, paddingLeft: 4 }}>
            {lang === 'ru' ? 'График' : 'Schedule overlap'}
          </div>
          <div style={{ display: 'flex', gap: 6 }}>
            {(lang === 'ru' ? ['Пн','Вт','Ср','Чт','Пт','Сб','Вс'] : ['Mo','Tu','We','Th','Fr','Sa','Su']).map((d, i) => (
              <Pill key={i} active={i < 5} color={t.p2}>{d}</Pill>
            ))}
          </div>
        </div>

        <GradButton icon="check">{lang === 'ru' ? 'Применить фильтры' : 'Apply filters'}</GradButton>
      </div>
    </div>
  );
}

// ── Invite to Workout screen
function InviteWorkoutScreen({ user, theme, onBack }) {
  const t = theme;
  const { t: tr, lang } = useI18n();
  return (
    <div style={{ background: t.bgGrad, minHeight: '100%' }}>
      <ScreenHeader
        title={tr('inviteToGym')}
        left={<button onClick={onBack} style={{ border: 'none', background: 'transparent', color: t.p2, fontWeight: 600, cursor: 'pointer', fontFamily: 'inherit', fontSize: 15 }}>{tr('cancel')}</button>}
        right={<button style={{ border: 'none', background: 'transparent', color: t.p2, fontWeight: 700, cursor: 'pointer', fontFamily: 'inherit', fontSize: 15 }}>{lang === 'ru' ? 'Отправить' : 'Send'}</button>}
        large theme={t}
      />

      <div style={{ padding: '0 18px', display: 'flex', flexDirection: 'column', gap: 14 }}>
        {/* To */}
        <Card padding={14}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <div style={{ width: 44, height: 44, borderRadius: 22, overflow: 'hidden' }}>
              <PhotoSlot color={user.color} color2={user.color2} style={{ width: '100%', height: '100%' }}/>
            </div>
            <div style={{ flex: 1 }}>
              <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600 }}>{lang === 'ru' ? 'Кому' : 'To'}</div>
              <div style={{ fontSize: 15, fontWeight: 700, color: t.text }}>{user.name}, {user.age}</div>
            </div>
          </div>
        </Card>

        {/* Date + Time */}
        <div style={{ display: 'flex', gap: 10 }}>
          <Card padding={14} style={{ flex: 1 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 6 }}>
              <Icon name="calendar" size={14} color={t.p2}/>
              <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600, textTransform: 'uppercase' }}>{lang === 'ru' ? 'Дата' : 'Date'}</div>
            </div>
            <div style={{ fontSize: 16, fontWeight: 700, color: t.text }}>{lang === 'ru' ? 'Завтра' : 'Tomorrow'}</div>
            <div style={{ fontSize: 11, color: t.textMuted, marginTop: 2 }}>Tue, May 26</div>
          </Card>
          <Card padding={14} style={{ flex: 1 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 6 }}>
              <Icon name="clock" size={14} color={t.p1}/>
              <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600, textTransform: 'uppercase' }}>{lang === 'ru' ? 'Время' : 'Time'}</div>
            </div>
            <div style={{ fontSize: 16, fontWeight: 700, color: t.text }}>07:00</div>
            <div style={{ fontSize: 11, color: t.textMuted, marginTop: 2 }}>1h 30m</div>
          </Card>
        </div>

        {/* Gym */}
        <Card padding={14}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 6 }}>
            <Icon name="location" size={14} color={t.p3}/>
            <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600, textTransform: 'uppercase' }}>{lang === 'ru' ? 'Зал' : 'Gym'}</div>
          </div>
          <div style={{ fontSize: 16, fontWeight: 700, color: t.text }}>{user.gym}</div>
          <div style={{ fontSize: 11, color: t.textMuted, marginTop: 2 }}>{user.distance} km · 4.8 ★</div>
        </Card>

        {/* Routine */}
        <Card padding={14}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 10 }}>
            <Icon name="dumbbell" size={14} color={t.p2}/>
            <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600, textTransform: 'uppercase' }}>{tr('sharedWorkout')}</div>
          </div>
          {ROUTINES.slice(0, 2).map(r => (
            <div key={r.id} style={{
              display: 'flex', alignItems: 'center', gap: 10, padding: '10px 0',
              borderTop: `1px solid ${t.border}`,
            }}>
              <div style={{ width: 6, height: 24, borderRadius: 3, background: r.color }}/>
              <div style={{ flex: 1 }}>
                <div style={{ fontSize: 14, fontWeight: 600, color: t.text }}>
                  {lang === 'ru' ? r.nameRu : r.name}
                </div>
                <div style={{ fontSize: 11, color: t.textMuted }}>{r.sets} sets · {r.duration}m</div>
              </div>
              <div style={{ width: 22, height: 22, borderRadius: 11, border: `2px solid ${r.id === 'r1' ? t.p2 : t.border}`, display: 'flex', alignItems: 'center', justifyContent: 'center', background: r.id === 'r1' ? t.p2 : 'transparent' }}>
                {r.id === 'r1' && <Icon name="check" size={12} color="#fff"/>}
              </div>
            </div>
          ))}
        </Card>

        <GradButton icon="send">{lang === 'ru' ? 'Отправить приглашение' : 'Send invite'}</GradButton>
      </div>
    </div>
  );
}

Object.assign(window, { DiscoverScreen, MatchesScreen, ChatScreen, DiscoverFiltersScreen, InviteWorkoutScreen, MatchModal });
