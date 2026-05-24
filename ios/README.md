# GymBuddy iOS

Native SwiftUI iOS app — a fitness tracker combined with a Tinder-style gym-buddy matcher.

## Requirements

- macOS 14+ with Xcode 15+ (the project targets iOS 17 and uses APIs introduced in iOS 17 such as the two-parameter `.onChange`).
- An iPhone 15 / iPhone 15 Pro simulator (or any iPhone simulator on iOS 17+).

## Open the project

```bash
open ios/GymBuddy/GymBuddy.xcodeproj
```

Pick the **GymBuddy** scheme and any iPhone simulator, then Run.

The Bundle Identifier is `app.gymbuddy.GymBuddy`.

## Backend dependency

The app expects a JSON backend at `http://localhost:3000/api/v1` (configurable via the `API_BASE_URL` key in `Info.plist`). The contract is documented in `docs/API.md`.

When the backend is unreachable, every screen falls back to in-memory sample data (mirroring `project/data.jsx`) so the app remains fully interactive in the simulator without spinning up a server. This is implemented by `SampleData` in `ViewModels/SampleData.swift`.

To point at a different host, edit the `API_BASE_URL` entry in `GymBuddy/Info.plist`.

## Project layout

```
GymBuddy/
  GymBuddyApp.swift          # @main
  Info.plist
  Assets.xcassets/
  Theme/                     # AppTheme + ThemeManager (Aurora/Sunset/Neon × light/dark)
  Localization/              # L10n.swift + en/ru .strings
  Networking/                # APIClient (async/await URLSession), Endpoints, models, WebSocket, Keychain
  Components/                # AppIcon, PhotoSlot, GradientButton, Pill/Card, StatTile, BottomNavBar, ScreenHeader, IconButton, AppProgressBar
  Screens/
    Auth/                    # Login, Register
    Home/                    # HomeView
    Workout/                 # WorkoutTracker, ActiveWorkout, ExercisesList, CreateExercise, RoutineDetail
    Discover/                # Discover (swipe), SwipeCardView, MatchModal, MatchesList, Chat, Filters, InviteWorkout
    Friends/                 # FriendsView (tabs), LeaderboardView
    Profile/                 # Profile, Settings (theme/accent/lang), EditProfile, Calendar, Nutrition, Medals
  ViewModels/                # Auth, Discover, Matches/Chat, Workout, ActiveWorkout, Exercises, SampleData
  Root/                      # RootView (auth gate), TabBarView
```

The project itself (`GymBuddy.xcodeproj/project.pbxproj`) is hand-written but follows the standard Xcode 15 SwiftUI App template; all 52 Swift files plus the Localization variant group and Assets catalog are referenced in the Sources / Resources build phases. The scheme `GymBuddy.xcscheme` is shared.

If you want to regenerate the pbxproj (e.g. after adding files), use:

```bash
python3 ios/GymBuddy/Scripts/gen_pbxproj.py
```

(Script lives in this repo under `/tmp/gen_pbxproj.py` during the build — copy it into `ios/GymBuddy/Scripts/` if you want it tracked. Or, recommended, install `xcodegen` and switch to `project.yml`.)

## Theming

- `ThemeManager` is a `@StateObject` injected at the app root.
- Light / Dark follow the system by default but can be overridden in Settings.
- Accent palette is one of: **Aurora** (default), **Sunset**, **Neon**. Pickable in Settings → Accent. Persisted via `UserDefaults`.

All colors come from `Theme.swift` and exactly match `docs/DESIGN_TOKENS.md`.

## Localization

- English and Russian, keyed by strings found in `project/i18n.jsx`.
- Active language is `ThemeManager.language`, persisted via `UserDefaults`.
- Looked up through `L10n.tr(...)` / convenience `L(key, lang)`.
- A real `.strings` file is also bundled for each locale (`en.lproj`, `ru.lproj`) so `NSLocalizedString` works too if you prefer it.

## Discover swipe gesture

Implemented in `Screens/Discover/DiscoverView.swift`:

- `DragGesture` updates `dragOffset` and computes `rotation = dx/20`.
- Threshold is **100pt** (commit) — anything below snaps back via a spring animation.
- On commit, the card flies to ±600pt and the next card in `vm.stack` is advanced.
- "LIKE"/"NOPE" stamps appear once `|dx| > 30`.
- The API call is `POST /discover/swipe`; if it returns `matched: true`, `MatchModalView` is shown.

The five tap-action buttons (rewind / pass / super-like / like / boost) wire into the same swipe pipeline.

## What's polished vs stubbed

**Polished (closely matching the prototype layout):**

- LoginView, RegisterView
- HomeView (header chips, hero "today's plan" gradient card, streak strip, quests, weekly stats, muscle activity card with custom silhouette canvas, horizontal routines scroll)
- DiscoverView (swipe stack, LIKE/NOPE badges, action buttons, match modal)
- ChatView (bubbles with shaped corners, gradient header chip, suggested-workout card)
- MatchesListView (likes-you tile, new-matches strip, message list)
- WorkoutTrackerView (stats chip row, segmented tabs, dashed create-plan card, routine cards)
- ActiveWorkoutView (timer, rest banner, per-set table, set-completion toggling)
- ProfileView (avatar with camera button, hexagon level badge, widgets grid, mini calendar)
- SettingsView (Pro banner, theme toggle, accent picker, language)
- MedalsView (gradient circle on unlocked, lock icon on locked)
- DiscoverFiltersView, MatchModalView, InviteWorkoutView, MedalsView, LeaderboardView

**Skeletons (functional but layout not fully polished — marked with `// TODO: polish layout to match prototype`):**

- CreateExerciseView — has all fields and pills, image picker is a placeholder button.
- CalendarView — month grid drawn manually with a fixed offset for May 2026; doesn't navigate months yet.
- NutritionView — calorie ring + macros + meals; SF Symbols used in place of emoji icons.
- EditProfileView — fields & photo placeholders; photo picker not yet wired.
- FriendsView (Requests tab is empty-state only).
- RoutineDetailView — basic; doesn't yet support editing the routine.

All 15+ screens from the prototype are reachable.

## Networking

`APIClient` exposes async functions:

```swift
let auth = try await APIClient.shared.login(email: "...", password: "...")
let feed = try await APIClient.shared.discoverFeed()
let res  = try await APIClient.shared.swipe(userId: id, direction: .like)
```

JWT tokens are stored in Keychain via `TokenStore`. Token is automatically attached to every authenticated request.

WebSocket chat is in `WebSocketClient` (uses `URLSessionWebSocketTask`). It's wired into `ChatViewModel.connect()` and decodes `{ type: "message", data: Message }` events.

## Known caveats

- The custom muscle-silhouette canvas (`MuscleSilhouette`) is a simplified path; the prototype uses an SVG outline + circles, which we approximated.
- We rely on SF Symbols for icons (the prototype uses inline SVGs). The mapping lives in `Components/AppIcon.swift`.
- The Tab bar uses our `BottomNavBar` (custom-drawn) instead of `TabView`, to match the prototype's pill background on active tabs and the larger heart icon.
- The handwritten `project.pbxproj` may need to be re-saved by Xcode on first open ("upgrade settings"). Click "Perform Changes" if prompted.
