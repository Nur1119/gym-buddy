package app.gymbuddy.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.BottomNavBar
import app.gymbuddy.ui.screens.auth.LoginScreen
import app.gymbuddy.ui.screens.auth.RegisterScreen
import app.gymbuddy.ui.screens.discover.ChatScreen
import app.gymbuddy.ui.screens.discover.DiscoverFiltersScreen
import app.gymbuddy.ui.screens.discover.DiscoverScreen
import app.gymbuddy.ui.screens.discover.InviteWorkoutScreen
import app.gymbuddy.ui.screens.discover.MatchesListScreen
import app.gymbuddy.ui.screens.friends.FriendsScreen
import app.gymbuddy.ui.screens.friends.LeaderboardScreen
import app.gymbuddy.ui.screens.home.HomeScreen
import app.gymbuddy.ui.screens.profile.CalendarScreen
import app.gymbuddy.ui.screens.profile.EditProfileScreen
import app.gymbuddy.ui.screens.profile.MedalsScreen
import app.gymbuddy.ui.screens.profile.NutritionScreen
import app.gymbuddy.ui.screens.profile.ProfileScreen
import app.gymbuddy.ui.screens.profile.SettingsScreen
import app.gymbuddy.ui.screens.workout.ActiveWorkoutScreen
import app.gymbuddy.ui.screens.workout.CreateExerciseScreen
import app.gymbuddy.ui.screens.workout.ExercisesListScreen
import app.gymbuddy.ui.screens.workout.RoutineDetailScreen
import app.gymbuddy.ui.screens.workout.WorkoutTrackerScreen
import app.gymbuddy.viewmodel.AppSettingsViewModel

/**
 * Root navigation graph.
 *
 * The "shell" (bottom nav bar + main tabs) is one NavHost; everything that
 * pushes on top (auth, workout flow, discover detail screens, profile detail
 * screens) is another set of destinations in the same host.
 */
@Composable
fun GymBuddyNavGraph(settingsVm: AppSettingsViewModel) {
    val navController = rememberNavController()
    val tokens = GymTheme.tokens
    val settings by settingsVm.state.collectAsStateWithLifecycle()
    val backEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backEntry?.destination?.route

    val rootTabs = setOf(Routes.Home, Routes.Workout, Routes.Discover, Routes.Friends, Routes.Profile)
    val showBottomBar = currentRoute in rootTabs

    // Auth gating: if not authenticated, force the auth stack.
    LaunchedEffect(settings.isAuthenticated, settings.tokenLoaded) {
        if (settings.tokenLoaded) {
            val start = if (settings.isAuthenticated) Routes.Home else Routes.Login
            if (currentRoute == null || (currentRoute == Routes.Login && settings.isAuthenticated)) {
                navController.navigate(start) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(tokens.surface.bg)) {
        NavHost(
            navController = navController,
            startDestination = Routes.Login,
            modifier = Modifier.fillMaxSize(),
        ) {
            // ── Auth ────────────────────────────────────────────────
            composable(Routes.Login) {
                LoginScreen(
                    onLoggedIn = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Login) { inclusive = true }
                        }
                    },
                    onGoRegister = { navController.navigate(Routes.Register) },
                )
            }
            composable(Routes.Register) {
                RegisterScreen(
                    onRegistered = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Login) { inclusive = true }
                        }
                    },
                    onGoLogin = { navController.popBackStack() },
                )
            }

            // ── Tabs ────────────────────────────────────────────────
            composable(Routes.Home) {
                ScreenWithBottomBar(showBottomBar = showBottomBar, currentRoute = Routes.Home, onSelect = { navigateToTab(navController, it) }) {
                    HomeScreen(onStartWorkout = { navController.navigate(Routes.ActiveWorkout) })
                }
            }
            composable(Routes.Workout) {
                ScreenWithBottomBar(showBottomBar = showBottomBar, currentRoute = Routes.Workout, onSelect = { navigateToTab(navController, it) }) {
                    WorkoutTrackerScreen(
                        onStartActive = { navController.navigate(Routes.ActiveWorkout) },
                        onOpenExercises = { navController.navigate(Routes.Exercises) },
                        onOpenRoutine = { r -> navController.navigate(Routes.routineDetail(r.id)) },
                    )
                }
            }
            composable(Routes.Discover) {
                ScreenWithBottomBar(showBottomBar = showBottomBar, currentRoute = Routes.Discover, onSelect = { navigateToTab(navController, it) }) {
                    DiscoverScreen(
                        onOpenFilters = { navController.navigate(Routes.DiscoverFilters) },
                        onOpenMatches = { navController.navigate(Routes.Matches) },
                        onMatch = { user ->
                            navController.navigate(Routes.chat(user.id))
                        },
                    )
                }
            }
            composable(Routes.Friends) {
                ScreenWithBottomBar(showBottomBar = showBottomBar, currentRoute = Routes.Friends, onSelect = { navigateToTab(navController, it) }) {
                    FriendsScreen(
                        onOpenLeaderboard = { navController.navigate(Routes.Leaderboard) },
                    )
                }
            }
            composable(Routes.Profile) {
                ScreenWithBottomBar(showBottomBar = showBottomBar, currentRoute = Routes.Profile, onSelect = { navigateToTab(navController, it) }) {
                    ProfileScreen(
                        onOpenSettings = { navController.navigate(Routes.Settings) },
                        onOpenEdit = { navController.navigate(Routes.EditProfile) },
                        onOpenCalendar = { navController.navigate(Routes.Calendar) },
                        onOpenNutrition = { navController.navigate(Routes.Nutrition) },
                        onOpenMedals = { navController.navigate(Routes.MedalsScreen) },
                        onOpenLeaderboard = { navController.navigate(Routes.Leaderboard) },
                    )
                }
            }

            // ── Workout sub-flow ────────────────────────────────────
            composable(Routes.ActiveWorkout) {
                ActiveWorkoutScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.Exercises) {
                ExercisesListScreen(
                    onBack = { navController.popBackStack() },
                    onCreate = { navController.navigate(Routes.CreateExercise) },
                )
            }
            composable(Routes.CreateExercise) {
                CreateExerciseScreen(onBack = { navController.popBackStack() }, onSave = { navController.popBackStack() })
            }
            composable(
                Routes.RoutineDetail,
                arguments = listOf(navArgument("routineId") { type = NavType.StringType }),
            ) { entry ->
                val id = entry.arguments?.getString("routineId").orEmpty()
                RoutineDetailScreen(
                    routineId = id,
                    onBack = { navController.popBackStack() },
                    onStart = { navController.navigate(Routes.ActiveWorkout) },
                )
            }

            // ── Discover sub-flow ───────────────────────────────────
            composable(Routes.Matches) {
                MatchesListScreen(
                    onBack = { navController.popBackStack() },
                    onOpenChat = { match -> navController.navigate(Routes.chat(match.id)) },
                )
            }
            composable(
                Routes.Chat,
                arguments = listOf(navArgument("matchId") { type = NavType.StringType }),
            ) { entry ->
                val matchId = entry.arguments?.getString("matchId").orEmpty()
                ChatScreen(
                    matchId = matchId,
                    peerName = "Match",
                    onBack = { navController.popBackStack() },
                    onInvite = { navController.navigate(Routes.inviteWorkout(matchId)) },
                )
            }
            composable(Routes.DiscoverFilters) {
                DiscoverFiltersScreen(onBack = { navController.popBackStack() }, onApply = { navController.popBackStack() })
            }
            composable(
                Routes.InviteWorkout,
                arguments = listOf(navArgument("matchId") { type = NavType.StringType }),
            ) { entry ->
                val matchId = entry.arguments?.getString("matchId").orEmpty()
                InviteWorkoutScreen(
                    matchId = matchId,
                    peerName = "Match",
                    onBack = { navController.popBackStack() },
                    onSend = { navController.popBackStack() },
                )
            }

            // ── Friends sub-flow ────────────────────────────────────
            composable(Routes.Leaderboard) {
                LeaderboardScreen(onBack = { navController.popBackStack() })
            }

            // ── Profile sub-flow ────────────────────────────────────
            composable(Routes.EditProfile) {
                EditProfileScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.Settings) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(Routes.Login) { popUpTo(0) { inclusive = true } }
                    },
                    settingsVm = settingsVm,
                )
            }
            composable(Routes.Calendar) {
                CalendarScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.Nutrition) {
                NutritionScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.MedalsScreen) {
                MedalsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

/** Helper composable that draws the [BottomNavBar] under the main tab content. */
@Composable
private fun ScreenWithBottomBar(
    showBottomBar: Boolean,
    currentRoute: String,
    onSelect: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f).fillMaxSize()) { content() }
        if (showBottomBar) {
            BottomNavBar(current = currentRoute, onSelect = onSelect)
        }
    }
}

private fun navigateToTab(navController: NavHostController, key: String) {
    val route = when (key) {
        "home" -> Routes.Home
        "workout" -> Routes.Workout
        "discover" -> Routes.Discover
        "friends" -> Routes.Friends
        "profile" -> Routes.Profile
        else -> Routes.Home
    }
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) {
            saveState = true
            inclusive = false
        }
        launchSingleTop = true
        restoreState = true
    }
}
