# GymBuddy — Android (Kotlin / Jetpack Compose)

Native Android client for **GymBuddy**, a fitness app that combines workout
tracking with a Tinder-style gym-buddy matching feature.

## Stack

- **Kotlin 1.9.24** + **Jetpack Compose** (BOM 2024.08), **Material 3**
- **Hilt** for DI, **Retrofit 2 + OkHttp + Kotlinx Serialization** for networking
- **OkHttp WebSocket** for live chat (`/matches/:id/stream`) with exponential-backoff reconnect
- **DataStore (Preferences)** for the JWT token and user prefs (accent, theme mode, locale)
- **Navigation Compose** with type-safe route helpers
- **minSdk 26, targetSdk 34, compileSdk 34, JDK 17**

## Project layout

```
android/
  build.gradle.kts              # root (plugin declarations only)
  settings.gradle.kts
  gradle/libs.versions.toml     # version catalog
  app/
    build.gradle.kts            # module config + dependencies
    src/main/AndroidManifest.xml
    src/main/java/app/gymbuddy/
      GymBuddyApp.kt            # @HiltAndroidApp
      MainActivity.kt           # Compose host, wires settings + theme + locale
      theme/                    # Color.kt, Theme.kt, Type.kt
      l10n/                     # LocalAppLocale + tr(R.string.x) helper
      data/
        remote/                 # ApiClient, ApiService, AuthInterceptor, WebSocketClient
          dto/                  # UserDto, MatchDto, MessageDto, ExerciseDto, RoutineDto, WorkoutDto
        local/                  # TokenStore (DataStore)
        repository/             # Auth/Discover/Matches/Workout/Exercise/Profile
      ui/
        components/             # AppIcon, PhotoSlot, Avatar, GradientButton, Pill,
                                # StatTile, ProgressBar, ScreenHeader+IconButton+SectionHeader,
                                # Card (GymCard), BottomNavBar
        screens/
          auth/                 # LoginScreen, RegisterScreen
          home/                 # HomeScreen
          workout/              # WorkoutTracker, ActiveWorkout, ExercisesList, CreateExercise, RoutineDetail
          discover/             # DiscoverScreen + SwipeCard, MatchModal, MatchesList,
                                # ChatScreen, DiscoverFilters, InviteWorkout
          friends/              # FriendsScreen, LeaderboardScreen
          profile/              # ProfileScreen, EditProfile, Settings, Calendar, Nutrition, Medals
        viewmodel/              # one VM per screen (HiltViewModel)
      nav/                      # Routes + NavGraph
      di/                       # NetworkModule, RepositoryModule
    src/main/res/
      values/strings.xml        # English (default)
      values-ru/strings.xml     # Russian
      drawable/                 # vector icons + adaptive icon
      mipmap-anydpi-v26/        # launcher icons
      xml/network_security_config.xml
```

## How to open

1. Open `android/` in Android Studio (Hedgehog or later).
2. Let Gradle sync (the version catalog + Compose BOM pin everything).
3. Run on an Android 8.0+ emulator or device.

## Backend

The Retrofit client points at the API server by default at:

- **debug:** `http://10.0.2.2:3000/api/v1/` — that's the Android emulator's
  mapping for `localhost:3000` on the host machine. Start the backend in
  `/backend` (or anywhere) and it will be reachable.
- **release:** `https://api.gymbuddy.app/api/v1/` (placeholder).

Both values are set via `buildConfigField` in `app/build.gradle.kts`.

`network_security_config.xml` already whitelists `10.0.2.2`, `localhost`, and
`127.0.0.1` for cleartext.

## Localization

Two locales: English (default) and Russian. The active locale is held in
`LocalAppLocale` (a CompositionLocal). Every screen calls `tr(R.string.x)`,
which resolves through a localized `Resources` instance so resource lookups
honor the in-app override (not just the system locale). Swap via Settings →
Language.

The key set matches the `STRINGS` keys in `project/i18n.jsx`.

## Theming

Three accent palettes — **Aurora** (default), **Sunset**, **Neon** — defined
in `theme/Color.kt`. Each exposes p1/p2/p3 plus like/nope/superLike/boost
colors and a `gradient()` Brush. The active palette is held in DataStore and
selectable from Settings. Light/Dark/System theme mode is also persisted.

Material 3 `ColorScheme` is composed from these tokens and the active mode.
Surface/text tokens are exposed at `GymTheme.tokens.surface.*` and accent
tokens at `GymTheme.tokens.accent.*` so calling code is short.

## Discover (flagship)

`DiscoverScreen` is fully polished:

- **Card stack:** top + 2 ghost cards behind it, with scale + translateY +
  alpha falloff to mimic the web prototype's depth.
- **Drag gesture:** `Modifier.pointerInput { detectDragGestures(...) }`
  drives an `Animatable<Float>` for offset. The top card rotates at
  `offsetX / 20` deg.
- **LIKE / NOPE badges** appear past ±60 px and rotate ±18°.
- **Threshold:** ±25% of screen width triggers a programmatic
  `animateTo(±1.2 × screenWidth)` and calls `vm.swipe(direction)`.
- **Action row:** Rewind / Pass / Super-like / Like / Boost — each wired
  to the matching repository call. Pass and Like also animate the card off.
- **Match modal:** full-screen `Dialog` with the gradient "It's a match!"
  headline, two tilted photos, Send-message CTA, and Keep-swiping button.

## Chat / WebSocket

`ChatScreen` is wired to `ChatViewModel.bind(matchId)`. The view model:

1. Fetches the initial page via `GET /matches/:id/messages`.
2. Opens the WebSocket via `WebSocketClient.connect(matchId, scope)`.
3. Subscribes to the shared `events` flow and appends inbound `message`
   events to the UI list.
4. On send, hits `POST /matches/:id/messages` and optimistically appends.
5. Disconnects in `onCleared()`.

`WebSocketClient` reconnects on failure with exponential backoff
(1s → 30s cap, max 6 attempts of doubling) and attaches the Bearer token.

## Polished vs skeleton

| Screen | Status |
|---|---|
| Discover (swipe stack, match modal, action row) | **Polished** |
| Matches list (new-matches strip + conversation list) | **Polished** |
| Chat (header, list, input, WS wiring) | **Polished** |
| Home (greeting, hero card, streak, quests, stats, routines strip) | **Polished** |
| Workout tracker (tabs, action cards, routine list) | **Polished** |
| Active workout (timer, rest banner, set tables) | **Polished** |
| Exercises list (search, filter pills, list) | **Polished** |
| Create custom exercise | **Polished** |
| Profile (hero, level/XP card, widget grid) | **Polished** |
| Settings (Pro banner, theme/accent/locale, sections) | **Polished** |
| Edit profile (photo grid, fields, goal pills) | **Polished** |
| Discover filters (sliders, goal/level/schedule pills) | **Polished** |
| Invite-to-workout | **Polished** |
| Login / Register | **Polished** |
| Bottom nav (5 tabs with active gradient pill) | **Polished** |
| Calendar | **Skeleton** (month grid w/ workout dots; `// TODO match prototype layout` for upcoming list & month switcher logic) |
| Nutrition | **Skeleton** (calorie ring + macro tiles + meals; works with backend day if available) |
| Medals | **Skeleton** (2-column grid; uses live data from `/medals`) |
| Friends + Leaderboard | **Skeleton** (list + simple podium; full leaderboard screen separate) |
| Routine detail | **Skeleton** (`// TODO match prototype layout`) |

Every "skeleton" screen still calls the backend, displays its data, and is
fully navigable.

## Build

The Android SDK was not available in the build environment, so a full
`./gradlew assembleDebug` could not be run here. Open the project in
Android Studio with a recent SDK (compileSdk 34) and the version catalog
will pull the right Compose + Hilt versions. The Gradle wrapper
(`gradlew` / `gradle/wrapper/`) is committed for convenience.

## Notes

- The token is stored in `DataStore<Preferences>` under the key `auth_token`.
- All repositories return `Result<T>` so the UI layer can show errors without
  exceptions propagating into Compose.
- `LocalAppLocale` is what lets the language switch work without restarting
  the activity — the localized context is rebuilt per `tr(...)` call.
- The icon set is mapped to Material Icons in `AppIcon.kt`; if a design
  calls for a one-off SVG we can drop a vector drawable into `res/drawable/`.
