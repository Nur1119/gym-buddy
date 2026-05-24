// ui.jsx — shared UI primitives

// ── Icon system (custom SVG, NOT emoji — clean line icons)
function Icon({ name, size = 22, color = 'currentColor', strokeWidth = 2 }) {
  const s = { width: size, height: size, display: 'block' };
  const sp = { fill: 'none', stroke: color, strokeWidth, strokeLinecap: 'round', strokeLinejoin: 'round' };
  switch (name) {
    case 'home': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M3 11l9-8 9 8v10a1 1 0 01-1 1h-5v-7H9v7H4a1 1 0 01-1-1V11z"/></svg>);
    case 'dumbbell': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M6.5 6.5l-2 2m13 9l-2 2M2 13l2-2m11 11l2-2M14.5 4.5L19.5 9.5l-10 10L4.5 14.5z"/><path {...sp} d="M6 6l4 4M14 14l4 4"/></svg>);
    case 'flame': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M12 2c1 4 5 5 5 10a5 5 0 11-10 0c0-2 1-3 1-5 2 1 3 0 4-5z"/></svg>);
    case 'spark': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M12 3l2 6 6 2-6 2-2 6-2-6-6-2 6-2 2-6z"/></svg>);
    case 'heart': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M20.8 6.6a5.5 5.5 0 00-9.3-2.5l-.5.5-.5-.5A5.5 5.5 0 002.2 9.5C2.2 14 7 17.5 12 21c5-3.5 9.8-7 9.8-11.5 0-1-.3-2-1-2.9z"/></svg>);
    case 'heart-fill': return (<svg style={s} viewBox="0 0 24 24" fill={color}><path d="M20.8 6.6a5.5 5.5 0 00-9.3-2.5l-.5.5-.5-.5A5.5 5.5 0 002.2 9.5C2.2 14 7 17.5 12 21c5-3.5 9.8-7 9.8-11.5 0-1-.3-2-1-2.9z"/></svg>);
    case 'user': return (<svg style={s} viewBox="0 0 24 24"><circle {...sp} cx="12" cy="8" r="4"/><path {...sp} d="M4 21c0-4 4-6 8-6s8 2 8 6"/></svg>);
    case 'users': return (<svg style={s} viewBox="0 0 24 24"><circle {...sp} cx="9" cy="8" r="3.5"/><circle {...sp} cx="17" cy="9" r="2.5"/><path {...sp} d="M3 19c0-3.3 2.7-5 6-5s6 1.7 6 5M15 19c0-2 1.5-3.5 4-3.5s4 1.5 4 3.5"/></svg>);
    case 'cog': return (<svg style={s} viewBox="0 0 24 24"><circle {...sp} cx="12" cy="12" r="3"/><path {...sp} d="M19.4 15a1.7 1.7 0 00.4 1.9l.1.1a2 2 0 11-2.9 2.9l-.1-.1a1.7 1.7 0 00-1.9-.4 1.7 1.7 0 00-1 1.6V21a2 2 0 01-4 0v-.1a1.7 1.7 0 00-1.1-1.5 1.7 1.7 0 00-1.9.4l-.1.1a2 2 0 11-2.9-2.9l.1-.1a1.7 1.7 0 00.4-1.9 1.7 1.7 0 00-1.5-1H3a2 2 0 010-4h.1a1.7 1.7 0 001.5-1.1 1.7 1.7 0 00-.4-1.9l-.1-.1a2 2 0 112.9-2.9l.1.1a1.7 1.7 0 001.9.4H9a1.7 1.7 0 001-1.5V3a2 2 0 014 0v.1a1.7 1.7 0 001 1.5 1.7 1.7 0 001.9-.4l.1-.1a2 2 0 112.9 2.9l-.1.1a1.7 1.7 0 00-.4 1.9V9a1.7 1.7 0 001.5 1H21a2 2 0 010 4h-.1a1.7 1.7 0 00-1.5 1z"/></svg>);
    case 'plus': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M12 5v14M5 12h14"/></svg>);
    case 'check': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M5 12l5 5L20 7"/></svg>);
    case 'close': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M6 6l12 12M18 6L6 18"/></svg>);
    case 'chevron-right': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M9 6l6 6-6 6"/></svg>);
    case 'chevron-left': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M15 6l-6 6 6 6"/></svg>);
    case 'chevron-down': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M6 9l6 6 6-6"/></svg>);
    case 'arrow-right': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M5 12h14M13 6l6 6-6 6"/></svg>);
    case 'arrow-left': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M19 12H5M11 6l-6 6 6 6"/></svg>);
    case 'search': return (<svg style={s} viewBox="0 0 24 24"><circle {...sp} cx="11" cy="11" r="7"/><path {...sp} d="M21 21l-4.3-4.3"/></svg>);
    case 'calendar': return (<svg style={s} viewBox="0 0 24 24"><rect {...sp} x="3" y="5" width="18" height="16" rx="2"/><path {...sp} d="M3 10h18M8 3v4M16 3v4"/></svg>);
    case 'bell': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M6 8a6 6 0 1112 0c0 7 3 8 3 9H3c0-1 3-2 3-9z"/><path {...sp} d="M10 21a2 2 0 004 0"/></svg>);
    case 'filter': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M3 5h18l-7 9v6l-4-2v-4z"/></svg>);
    case 'bolt': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M13 2L4 14h7l-1 8 9-12h-7z"/></svg>);
    case 'rewind': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M3 12a9 9 0 1015-6.7"/><path {...sp} d="M3 4v6h6"/></svg>);
    case 'star': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M12 2l3.1 6.4 7 1-5 4.9 1.2 7L12 18l-6.3 3.3 1.2-7-5-4.9 7-1z"/></svg>);
    case 'send': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M22 2L11 13M22 2l-7 20-4-9-9-4z"/></svg>);
    case 'apple': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M12 6c0-2 1-3 3-3M8 10c-3 0-5 2-5 6 0 4 3 8 5 8 1 0 2-1 4-1s3 1 4 1c2 0 5-4 5-8 0-4-2-6-5-6-2 0-3 1-4 1s-2-1-4-1z"/></svg>);
    case 'medal': return (<svg style={s} viewBox="0 0 24 24"><circle {...sp} cx="12" cy="15" r="6"/><path {...sp} d="M8 3l4 8 4-8M9 3h6"/></svg>);
    case 'trophy': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M8 4h8v6a4 4 0 01-8 0V4zM6 4H4v3a3 3 0 003 3M18 4h2v3a3 3 0 01-3 3M10 18h4l-1 3h-2z"/></svg>);
    case 'chart': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M3 20h18M6 16v-4M11 16V8M16 16v-6M21 16v-2"/></svg>);
    case 'play': return (<svg style={s} viewBox="0 0 24 24" fill={color}><path d="M7 4l14 8-14 8V4z"/></svg>);
    case 'pause': return (<svg style={s} viewBox="0 0 24 24" fill={color}><rect x="6" y="4" width="4" height="16" rx="1"/><rect x="14" y="4" width="4" height="16" rx="1"/></svg>);
    case 'image': return (<svg style={s} viewBox="0 0 24 24"><rect {...sp} x="3" y="5" width="18" height="14" rx="2"/><circle {...sp} cx="9" cy="11" r="2"/><path {...sp} d="M21 17l-5-5-9 7"/></svg>);
    case 'camera': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M3 8h3l2-3h8l2 3h3v12H3z"/><circle {...sp} cx="12" cy="13" r="4"/></svg>);
    case 'edit': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M4 20h4L20 8l-4-4L4 16zM14 6l4 4"/></svg>);
    case 'language': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M3 6h12M9 3v3c0 6-3 11-7 12M13 6c0 6 3 11 7 12M14 18h7M17 14l3 8M20 14l-3 8"/></svg>);
    case 'moon': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M21 13A9 9 0 1111 3a7 7 0 0010 10z"/></svg>);
    case 'sun': return (<svg style={s} viewBox="0 0 24 24"><circle {...sp} cx="12" cy="12" r="4"/><path {...sp} d="M12 2v2M12 20v2M2 12h2M20 12h2M5 5l1.5 1.5M17.5 17.5L19 19M5 19l1.5-1.5M17.5 6.5L19 5"/></svg>);
    case 'location': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M12 22s8-7 8-13a8 8 0 10-16 0c0 6 8 13 8 13z"/><circle {...sp} cx="12" cy="9" r="3"/></svg>);
    case 'clock': return (<svg style={s} viewBox="0 0 24 24"><circle {...sp} cx="12" cy="12" r="9"/><path {...sp} d="M12 7v5l3 2"/></svg>);
    case 'list': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M8 6h13M8 12h13M8 18h13M3 6h.01M3 12h.01M3 18h.01"/></svg>);
    case 'grid': return (<svg style={s} viewBox="0 0 24 24"><rect {...sp} x="3" y="3" width="7" height="7" rx="1"/><rect {...sp} x="14" y="3" width="7" height="7" rx="1"/><rect {...sp} x="3" y="14" width="7" height="7" rx="1"/><rect {...sp} x="14" y="14" width="7" height="7" rx="1"/></svg>);
    case 'more': return (<svg style={s} viewBox="0 0 24 24"><circle cx="5" cy="12" r="1.5" fill={color}/><circle cx="12" cy="12" r="1.5" fill={color}/><circle cx="19" cy="12" r="1.5" fill={color}/></svg>);
    case 'globe': return (<svg style={s} viewBox="0 0 24 24"><circle {...sp} cx="12" cy="12" r="9"/><path {...sp} d="M3 12h18M12 3a14 14 0 010 18M12 3a14 14 0 000 18"/></svg>);
    case 'sliders': return (<svg style={s} viewBox="0 0 24 24"><path {...sp} d="M4 6h12M4 12h6M4 18h14M18 6h2M14 12h6M20 18h0"/><circle {...sp} cx="17" cy="6" r="2"/><circle {...sp} cx="12" cy="12" r="2"/><circle {...sp} cx="7" cy="18" r="2"/></svg>);
    default: return null;
  }
}

// ── Avatar with gradient initial fallback
function Avatar({ name, size = 40, color = '#7C5CFF', color2 = '#00C2FF', ring = false, ringColor }) {
  const initial = (name || '?').charAt(0).toUpperCase();
  return (
    <div style={{
      width: size, height: size, borderRadius: size / 2,
      background: `linear-gradient(135deg, ${color}, ${color2})`,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      color: '#fff', fontWeight: 700, fontSize: size * 0.42,
      flexShrink: 0, position: 'relative',
      boxShadow: ring ? `0 0 0 2px ${ringColor || '#fff'}, 0 0 0 4px ${color}` : undefined,
    }}>{initial}</div>
  );
}

// ── Photo placeholder (subtly striped, monospace label)
function PhotoSlot({ color = '#7C5CFF', color2 = '#00C2FF', label, style }) {
  return (
    <div style={{
      background: `linear-gradient(135deg, ${color} 0%, ${color2} 100%)`,
      position: 'relative', overflow: 'hidden',
      display: 'flex', alignItems: 'flex-end', justifyContent: 'flex-start',
      ...style,
    }}>
      {/* Stripe pattern */}
      <div style={{
        position: 'absolute', inset: 0,
        background: 'repeating-linear-gradient(45deg, transparent 0 14px, rgba(255,255,255,0.06) 14px 28px)',
      }} />
      {/* Soft vignette */}
      <div style={{
        position: 'absolute', inset: 0,
        background: 'radial-gradient(60% 60% at 50% 30%, rgba(255,255,255,0.18), transparent 70%)',
      }} />
      {label && (
        <div style={{
          position: 'relative', padding: '6px 10px', margin: 10,
          background: 'rgba(0,0,0,0.35)', borderRadius: 6,
          color: 'rgba(255,255,255,0.85)', fontSize: 10, fontFamily: 'ui-monospace, "SF Mono", monospace',
          letterSpacing: 0.3,
        }}>{label}</div>
      )}
    </div>
  );
}

// ── Card surface
function Card({ children, style, padding = 16, onClick, theme }) {
  const t = theme || useTheme();
  return (
    <div onClick={onClick} style={{
      background: t.surface,
      border: `1px solid ${t.border}`,
      borderRadius: 18,
      padding,
      cursor: onClick ? 'pointer' : 'default',
      transition: 'transform .12s ease',
      ...style,
    }}>{children}</div>
  );
}

// ── Pill button
function Pill({ children, active, color, onClick, style }) {
  const t = useTheme();
  return (
    <button onClick={onClick} style={{
      border: 'none', cursor: 'pointer',
      padding: '8px 14px', borderRadius: 999,
      background: active ? (color || t.p2) : t.chip,
      color: active ? '#fff' : t.textMuted,
      fontSize: 13, fontWeight: 600, fontFamily: 'inherit',
      transition: 'all .15s', whiteSpace: 'nowrap',
      ...style,
    }}>{children}</button>
  );
}

// ── Primary gradient button
function GradButton({ children, onClick, style, icon, grad }) {
  const t = useTheme();
  return (
    <button onClick={onClick} style={{
      border: 'none', cursor: 'pointer',
      padding: '14px 18px', borderRadius: 14,
      background: grad || t.grad,
      color: '#fff', fontWeight: 700, fontSize: 15, fontFamily: 'inherit',
      display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 8,
      boxShadow: '0 8px 24px rgba(0,194,255,0.25)',
      ...style,
    }}>
      {icon && <Icon name={icon} size={18}/>}
      {children}
    </button>
  );
}

// ── Stat tile
function StatTile({ value, label, icon, color, style }) {
  const t = useTheme();
  return (
    <div style={{
      flex: 1, background: t.surface, border: `1px solid ${t.border}`,
      borderRadius: 14, padding: '12px 14px', minWidth: 0, ...style,
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 4 }}>
        {icon && <div style={{ color: color || t.p2 }}><Icon name={icon} size={14}/></div>}
        <div style={{ fontSize: 11, color: t.textDim, fontWeight: 600, letterSpacing: 0.3, textTransform: 'uppercase' }}>{label}</div>
      </div>
      <div style={{ fontSize: 22, fontWeight: 800, color: t.text, lineHeight: 1 }}>{value}</div>
    </div>
  );
}

// ── Top header (used on every screen)
function ScreenHeader({ title, left, right, theme, large = false }) {
  const t = theme || useTheme();
  return (
    <div style={{
      display: 'flex', alignItems: 'center', justifyContent: 'space-between',
      padding: large ? '4px 18px 12px' : '4px 18px 10px',
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 10, minWidth: 32 }}>{left}</div>
      <div style={{
        fontSize: large ? 22 : 17, fontWeight: 700, color: t.text,
        flex: 1, textAlign: large ? 'left' : 'center', marginLeft: large ? 0 : 0,
      }}>{title}</div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8, minWidth: 32, justifyContent: 'flex-end' }}>{right}</div>
    </div>
  );
}

// ── Icon button
function IconBtn({ name, onClick, size = 36, iconSize = 20, color, bg, badge }) {
  const t = useTheme();
  return (
    <button onClick={onClick} style={{
      width: size, height: size, borderRadius: size / 2,
      border: 'none', background: bg || t.chip, color: color || t.text,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      cursor: 'pointer', position: 'relative', flexShrink: 0,
    }}>
      <Icon name={name} size={iconSize}/>
      {badge != null && (
        <div style={{
          position: 'absolute', top: -2, right: -2,
          minWidth: 18, height: 18, padding: '0 4px', borderRadius: 9,
          background: t.danger, color: '#fff', fontSize: 10, fontWeight: 700,
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          border: `2px solid ${t.surface}`,
        }}>{badge}</div>
      )}
    </button>
  );
}

// ── Bottom navigation
function BottomNav({ tab, onTab, theme, mode = 'ios' }) {
  const t = theme || useTheme();
  const { t: tr } = useI18n();
  const tabs = [
    { key: 'home', label: tr('home'), icon: 'home' },
    { key: 'workout', label: tr('workout'), icon: 'dumbbell' },
    { key: 'discover', label: tr('discover'), icon: 'heart' },
    { key: 'friends', label: tr('friends'), icon: 'users' },
    { key: 'profile', label: tr('profile'), icon: 'user' },
  ];
  return (
    <div style={{
      display: 'flex', justifyContent: 'space-around', alignItems: 'flex-end',
      background: t.surface,
      borderTop: `1px solid ${t.border}`,
      paddingTop: 8, paddingBottom: mode === 'ios' ? 26 : 14,
      paddingLeft: 6, paddingRight: 6,
      flexShrink: 0,
    }}>
      {tabs.map(x => {
        const active = tab === x.key;
        return (
          <button key={x.key} onClick={() => onTab(x.key)} style={{
            border: 'none', background: 'transparent', cursor: 'pointer',
            display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4,
            padding: '4px 6px', minWidth: 56, fontFamily: 'inherit',
          }}>
            <div style={{
              width: 44, height: 28, borderRadius: 14,
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              background: active ? t.gradSoft : 'transparent',
              color: active ? t.p2 : t.textDim, transition: 'all .2s',
              position: 'relative',
            }}>
              {x.key === 'discover' && active
                ? <Icon name="heart-fill" size={22} color={t.p2}/>
                : <Icon name={x.icon} size={22}/>}
            </div>
            <div style={{
              fontSize: 10, fontWeight: 600,
              color: active ? t.p2 : t.textDim,
            }}>{x.label}</div>
          </button>
        );
      })}
    </div>
  );
}

// ── Section header
function SectionHeader({ title, action, onAction, theme }) {
  const t = theme || useTheme();
  return (
    <div style={{
      display: 'flex', alignItems: 'baseline', justifyContent: 'space-between',
      padding: '0 18px', marginBottom: 10, marginTop: 18,
    }}>
      <div style={{ fontSize: 17, fontWeight: 700, color: t.text }}>{title}</div>
      {action && (
        <button onClick={onAction} style={{
          border: 'none', background: 'transparent', cursor: 'pointer',
          color: t.p2, fontSize: 13, fontWeight: 600, padding: 0, fontFamily: 'inherit',
        }}>{action}</button>
      )}
    </div>
  );
}

// ── Progress bar
function ProgressBar({ value, max = 100, color, height = 8, style }) {
  const t = useTheme();
  const pct = Math.max(0, Math.min(100, (value / max) * 100));
  return (
    <div style={{
      width: '100%', height, background: t.chip, borderRadius: height / 2,
      overflow: 'hidden', ...style,
    }}>
      <div style={{
        width: `${pct}%`, height: '100%',
        background: color || t.grad, borderRadius: height / 2,
        transition: 'width .3s ease',
      }}/>
    </div>
  );
}

// ── Switch
function Switch({ value, onChange, color }) {
  const t = useTheme();
  return (
    <button onClick={() => onChange(!value)} style={{
      width: 48, height: 28, borderRadius: 14, padding: 2,
      background: value ? (color || t.p2) : t.chip,
      border: 'none', cursor: 'pointer', transition: 'background .2s',
      display: 'flex', alignItems: 'center', justifyContent: value ? 'flex-end' : 'flex-start',
    }}>
      <div style={{
        width: 24, height: 24, borderRadius: 12, background: '#fff',
        boxShadow: '0 2px 4px rgba(0,0,0,0.2)', transition: 'transform .2s',
      }}/>
    </button>
  );
}

Object.assign(window, { Icon, Avatar, PhotoSlot, Card, Pill, GradButton, StatTile, ScreenHeader, IconBtn, BottomNav, SectionHeader, ProgressBar, Switch });
