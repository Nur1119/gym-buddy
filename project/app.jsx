// app.jsx — App router + dual-frame canvas

// Global state — shared between iOS and Android frames
const AppCtx = React.createContext(null);

function AppProvider({ children }) {
  const [tab, setTab] = React.useState('discover'); // start on the headline feature
  const [stack, setStack] = React.useState([]); // route stack
  const [chatUser, setChatUser] = React.useState(null);

  const nav = React.useCallback((route, data) => {
    if (route === 'chat') {
      setChatUser(data);
      setStack(s => [...s, { route: 'chat', data }]);
    } else if (route === 'inviteWorkout') {
      setStack(s => [...s, { route: 'inviteWorkout', data }]);
    } else {
      setStack(s => [...s, { route, data }]);
    }
  }, []);

  const back = React.useCallback(() => setStack(s => s.slice(0, -1)), []);

  const value = { tab, setTab, stack, nav, back, chatUser };
  return <AppCtx.Provider value={value}>{children}</AppCtx.Provider>;
}

const useApp = () => React.useContext(AppCtx);

// ── Phone content (renders the current tab + stacked modals)
function PhoneContent({ mode, hideBottomNav }) {
  const theme = useTheme();
  const app = useApp();
  const current = app.stack[app.stack.length - 1];

  // Render bottom-most stack screen if there is one
  let content;
  if (current) {
    const { route, data } = current;
    switch (route) {
      case 'settings': content = <SettingsScreen theme={theme} onBack={app.back} onNav={app.nav}/>; break;
      case 'editProfile': content = <EditProfileScreen theme={theme} onBack={app.back}/>; break;
      case 'calendar': content = <CalendarScreen theme={theme} onBack={app.back}/>; break;
      case 'nutrition': content = <NutritionScreen theme={theme} onBack={app.back}/>; break;
      case 'medals': content = <MedalsScreen theme={theme} onBack={app.back}/>; break;
      case 'ranks': content = <RanksScreen theme={theme} onBack={app.back}/>; break;
      case 'stats': content = <RanksScreen theme={theme} onBack={app.back}/>; break;
      case 'matches': content = <MatchesScreen theme={theme} onBack={app.back} onNav={app.nav}/>; break;
      case 'chat': content = <ChatScreen user={data} theme={theme} onBack={app.back} onNav={app.nav}/>; break;
      case 'discoverFilters': content = <DiscoverFiltersScreen theme={theme} onBack={app.back}/>; break;
      case 'inviteWorkout': content = <InviteWorkoutScreen user={data} theme={theme} onBack={app.back}/>; break;
      case 'exercises': content = <ExercisesScreen theme={theme} onBack={app.back} onNav={app.nav}/>; break;
      case 'createExercise': content = <CreateExerciseScreen theme={theme} onBack={app.back}/>; break;
      case 'activeWorkout': content = <ActiveWorkoutScreen theme={theme} onBack={app.back}/>; break;
      default: content = null;
    }
  }

  if (!content) {
    switch (app.tab) {
      case 'home': content = <HomeScreen theme={theme} onNav={app.nav}/>; break;
      case 'workout': content = <WorkoutTrackerScreen theme={theme} onNav={app.nav}/>; break;
      case 'discover': content = <DiscoverScreen theme={theme} onNav={app.nav}/>; break;
      case 'friends': content = <FriendsScreen theme={theme}/>; break;
      case 'profile': content = <ProfileScreen theme={theme} onNav={app.nav}/>; break;
      default: content = <HomeScreen theme={theme} onNav={app.nav}/>;
    }
  }

  // Tabs that should hide bottom nav
  const inStack = app.stack.length > 0;
  const hide = hideBottomNav || inStack;

  return (
    <div style={{
      width: '100%', height: '100%', display: 'flex', flexDirection: 'column',
      background: theme.bg, color: theme.text, fontFamily: '-apple-system, BlinkMacSystemFont, "SF Pro Display", "Segoe UI", Roboto, system-ui, sans-serif',
      WebkitFontSmoothing: 'antialiased',
      paddingTop: mode === 'ios' ? 54 : 32, // safe area for status bar
    }}>
      <div style={{ flex: 1, overflowY: 'auto', overflowX: 'hidden', minHeight: 0 }}>
        {content}
      </div>
      {!hide && <BottomNav tab={app.tab} onTab={app.setTab} theme={theme} mode={mode}/>}
    </div>
  );
}

// ── Status bar overlay slot for phone shells
function PhoneShell({ kind, children, dark }) {
  // Place status bar manually; we want our own paddingTop
  const statusBarH = kind === 'ios' ? 50 : 36;
  return (
    <div style={{
      position: 'relative', width: '100%', height: '100%',
      paddingTop: statusBarH, paddingBottom: kind === 'ios' ? 34 : 18,
      boxSizing: 'border-box',
      display: 'flex', flexDirection: 'column',
    }}>
      {children}
    </div>
  );
}

// ── Top toolbar — theme/lang/tweaks
function Toolbar({ mode, setMode, lang, setLang, accent, setAccent }) {
  return (
    <div style={{
      position: 'fixed', top: 16, left: '50%', transform: 'translateX(-50%)',
      display: 'flex', alignItems: 'center', gap: 8, zIndex: 100,
      padding: '8px 12px', borderRadius: 999,
      background: mode === 'dark' ? 'rgba(20,26,43,0.85)' : 'rgba(255,255,255,0.9)',
      backdropFilter: 'blur(20px)',
      border: `1px solid ${mode === 'dark' ? '#262E47' : '#E4E8F0'}`,
      boxShadow: '0 8px 30px rgba(0,0,0,0.12)',
    }}>
      {/* Title */}
      <div style={{
        fontSize: 13, fontWeight: 800, padding: '4px 10px',
        background: ACCENT_PALETTES[accent].grad,
        WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent',
        fontFamily: '-apple-system, system-ui, sans-serif', letterSpacing: -0.3,
      }}>GymBuddy</div>

      <div style={{ width: 1, height: 18, background: mode === 'dark' ? '#39426A' : '#D2D8E3' }}/>

      {/* Theme toggle */}
      <button onClick={() => setMode(mode === 'dark' ? 'light' : 'dark')} style={{
        display: 'flex', alignItems: 'center', gap: 6,
        padding: '6px 10px', borderRadius: 999, border: 'none', cursor: 'pointer',
        background: 'transparent', color: mode === 'dark' ? '#fff' : '#0B1020',
        fontWeight: 600, fontSize: 12, fontFamily: 'inherit',
      }}>
        <Icon name={mode === 'dark' ? 'moon' : 'sun'} size={14}/>
        {mode === 'dark' ? 'Dark' : 'Light'}
      </button>

      {/* Lang toggle */}
      <button onClick={() => setLang(lang === 'ru' ? 'en' : 'ru')} style={{
        display: 'flex', alignItems: 'center', gap: 6,
        padding: '6px 10px', borderRadius: 999, border: 'none', cursor: 'pointer',
        background: 'transparent', color: mode === 'dark' ? '#fff' : '#0B1020',
        fontWeight: 600, fontSize: 12, fontFamily: 'inherit',
      }}>
        <Icon name="globe" size={14}/>
        {lang === 'ru' ? 'RU' : 'EN'}
      </button>
    </div>
  );
}

// ── Root component: dual frames side-by-side
const TWEAK_DEFAULTS = /*EDITMODE-BEGIN*/{
  "mode": "dark",
  "accent": "aurora",
  "lang": "en"
}/*EDITMODE-END*/;

function Root() {
  const [tk, setTweak] = useTweaks(TWEAK_DEFAULTS);
  const { mode, accent, lang } = tk;

  return (
    <ThemeProvider mode={mode} accent={accent}>
      <I18nProvider lang={lang}>
        <AppProvider>
          <div style={{
            minHeight: '100vh', width: '100%',
            background: mode === 'dark'
              ? 'radial-gradient(120% 80% at 50% -10%, #1a1f36 0%, #04060d 60%)'
              : 'linear-gradient(180deg, #F0F3FA 0%, #DCE3F0 100%)',
            display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
            padding: '90px 24px 60px', gap: 30, boxSizing: 'border-box',
          }}>
            <Toolbar mode={mode} setMode={(v) => setTweak('mode', v)} lang={lang} setLang={(v) => setTweak('lang', v)} accent={accent} setAccent={(v) => setTweak('accent', v)}/>

            {/* Frames */}
            <div style={{ display: 'flex', gap: 50, alignItems: 'flex-start', flexWrap: 'wrap', justifyContent: 'center' }}>
              {/* iOS */}
              <div data-screen-label="iOS Frame" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 14 }}>
                <div style={{ fontSize: 12, fontWeight: 700, color: mode === 'dark' ? '#A2ABC5' : '#5B6478', letterSpacing: 2, textTransform: 'uppercase' }}>iOS</div>
                <IOSDevice width={380} height={780} dark={mode === 'dark'}>
                  <PhoneContent mode="ios"/>
                </IOSDevice>
              </div>

              {/* Android */}
              <div data-screen-label="Android Frame" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 14 }}>
                <div style={{ fontSize: 12, fontWeight: 700, color: mode === 'dark' ? '#A2ABC5' : '#5B6478', letterSpacing: 2, textTransform: 'uppercase' }}>Android</div>
                <AndroidDevice width={380} height={780} dark={mode === 'dark'}>
                  <PhoneContent mode="android"/>
                </AndroidDevice>
              </div>
            </div>

            <TweaksPanel title="Tweaks">
              <TweakSection label="Theme">
                <TweakRadio
                  label="Mode" value={mode}
                  onChange={(v) => setTweak('mode', v)}
                  options={[{value:'light',label:'Light'},{value:'dark',label:'Dark'}]}
                />
              </TweakSection>
              <TweakSection label="Accent">
                <TweakRadio
                  label="Palette" value={accent}
                  onChange={(v) => setTweak('accent', v)}
                  options={[
                    {value:'aurora',label:'Aurora'},
                    {value:'sunset',label:'Sunset'},
                    {value:'electric',label:'Neon'},
                  ]}
                />
              </TweakSection>
              <TweakSection label="Language">
                <TweakRadio
                  label="Lang" value={lang}
                  onChange={(v) => setTweak('lang', v)}
                  options={[{value:'en',label:'EN'},{value:'ru',label:'RU'}]}
                />
              </TweakSection>
            </TweaksPanel>
          </div>
        </AppProvider>
      </I18nProvider>
    </ThemeProvider>
  );
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<Root/>);
