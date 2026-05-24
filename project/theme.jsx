// theme.jsx — design tokens, theme context

const THEMES = {
  light: {
    bg: '#F4F6FB',
    bgGrad: 'linear-gradient(180deg, #F4F6FB 0%, #EAEEF6 100%)',
    surface: '#FFFFFF',
    surface2: '#F1F4FA',
    surfaceElevated: '#FFFFFF',
    border: '#E4E8F0',
    borderStrong: '#D2D8E3',
    text: '#0B1020',
    textMuted: '#5B6478',
    textDim: '#8B92A8',
    overlay: 'rgba(11,16,32,0.45)',
    chip: '#EEF1F8',
    danger: '#FF3B5C',
    success: '#19C37D',
    warn: '#FFB020',
    statusBarDark: false,
  },
  dark: {
    bg: '#0A0E1A',
    bgGrad: 'radial-gradient(120% 80% at 50% -10%, #1A1F36 0%, #0A0E1A 60%)',
    surface: '#141A2B',
    surface2: '#1B2238',
    surfaceElevated: '#1F2740',
    border: '#262E47',
    borderStrong: '#39426A',
    text: '#FFFFFF',
    textMuted: '#A2ABC5',
    textDim: '#6A7390',
    overlay: 'rgba(0,0,0,0.6)',
    chip: '#1F2740',
    danger: '#FF4D6D',
    success: '#3DDC97',
    warn: '#FFC857',
    statusBarDark: true,
  },
};

// Accent palettes — share chroma, vary hue
const ACCENT_PALETTES = {
  aurora: {
    p1: '#3DDC97', // green
    p2: '#00C2FF', // cyan
    p3: '#7C5CFF', // purple
    grad: 'linear-gradient(135deg, #7C5CFF 0%, #00C2FF 55%, #3DDC97 100%)',
    gradSoft: 'linear-gradient(135deg, rgba(124,92,255,0.18) 0%, rgba(0,194,255,0.18) 55%, rgba(61,220,151,0.18) 100%)',
    like: '#3DDC97',
    nope: '#FF4D6D',
    superLike: '#00C2FF',
    boost: '#B967FF',
  },
  sunset: {
    p1: '#FFB020',
    p2: '#FF4D6D',
    p3: '#B967FF',
    grad: 'linear-gradient(135deg, #FFB020 0%, #FF4D6D 50%, #B967FF 100%)',
    gradSoft: 'linear-gradient(135deg, rgba(255,176,32,0.18) 0%, rgba(255,77,109,0.18) 50%, rgba(185,103,255,0.18) 100%)',
    like: '#19C37D',
    nope: '#FF4D6D',
    superLike: '#00C2FF',
    boost: '#FFB020',
  },
  electric: {
    p1: '#00E5FF',
    p2: '#39FF14',
    p3: '#FFEB3B',
    grad: 'linear-gradient(135deg, #00E5FF 0%, #39FF14 50%, #FFEB3B 100%)',
    gradSoft: 'linear-gradient(135deg, rgba(0,229,255,0.18) 0%, rgba(57,255,20,0.18) 50%, rgba(255,235,59,0.18) 100%)',
    like: '#39FF14',
    nope: '#FF1744',
    superLike: '#00E5FF',
    boost: '#FFEB3B',
  },
};

const ThemeCtx = React.createContext(null);

function ThemeProvider({ mode, accent, children }) {
  const t = THEMES[mode];
  const a = ACCENT_PALETTES[accent] || ACCENT_PALETTES.aurora;
  const value = React.useMemo(() => ({ ...t, ...a, mode, accentName: accent }), [mode, accent]);
  return <ThemeCtx.Provider value={value}>{children}</ThemeCtx.Provider>;
}

function useTheme() {
  return React.useContext(ThemeCtx);
}

window.ThemeProvider = ThemeProvider;
window.useTheme = useTheme;
window.THEMES = THEMES;
window.ACCENT_PALETTES = ACCENT_PALETTES;
