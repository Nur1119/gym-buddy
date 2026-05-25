package app.gymbuddy.nav

/**
 * Centralized route keys for Compose Navigation. Kept as constants so that
 * `navController.navigate(Routes.X)` doesn't lose type-safety at the call site.
 */
object Routes {
    // ── Auth ────────────────────────────────────────────────────────────
    const val Login = "login"
    const val Register = "register"

    // ── Tab roots ───────────────────────────────────────────────────────
    const val Home = "home"
    const val Workout = "workout"
    const val Discover = "discover"
    const val Friends = "friends"
    const val Profile = "profile"

    // ── Workout flow ────────────────────────────────────────────────────
    const val ActiveWorkout = "workout/active"
    const val Exercises = "workout/exercises"
    const val CreateExercise = "workout/exercises/create"
    const val CreateRoutine = "workout/routine/create"
    const val RoutineDetail = "workout/routine/{routineId}"
    fun routineDetail(id: String) = "workout/routine/$id"

    // ── Discover flow ───────────────────────────────────────────────────
    const val Matches = "discover/matches"
    const val Chat = "discover/chat/{matchId}"
    fun chat(matchId: String) = "discover/chat/$matchId"
    const val DiscoverFilters = "discover/filters"
    const val InviteWorkout = "discover/invite/{matchId}"
    fun inviteWorkout(matchId: String) = "discover/invite/$matchId"

    // ── Friends ─────────────────────────────────────────────────────────
    const val Leaderboard = "friends/leaderboard"

    // ── Profile flow ────────────────────────────────────────────────────
    const val EditProfile = "profile/edit"
    const val Settings = "profile/settings"
    const val Calendar = "profile/calendar"
    const val Nutrition = "profile/nutrition"
    const val MedalsScreen = "profile/medals"
}
