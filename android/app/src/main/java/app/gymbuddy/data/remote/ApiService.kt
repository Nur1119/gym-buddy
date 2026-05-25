package app.gymbuddy.data.remote

import app.gymbuddy.data.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit binding of the contract in /docs/API.md.
 *
 * All endpoints assume JSON in/out. Auth is handled by [AuthInterceptor], so
 * methods here do not require an Authorization header parameter.
 */
interface ApiService {

    // ── Auth ──────────────────────────────────────────────────────────────
    @POST("auth/register")
    suspend fun register(@Body req: AuthRequestRegister): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body req: AuthRequestLogin): AuthResponse

    @POST("auth/google")
    suspend fun loginWithGoogle(@Body req: AuthRequestGoogle): AuthResponse

    @POST("auth/refresh")
    suspend fun refresh(): TokenResponse

    @GET("auth/me")
    suspend fun me(): UserDto

    // ── Users / Profile ───────────────────────────────────────────────────
    @GET("users/me")
    suspend fun getMe(): UserDto

    @POST("users/me/fcm-token")
    suspend fun updateFcmToken(@Body req: app.gymbuddy.fcm.FcmTokenRequest)

    @PATCH("users/me")
    suspend fun updateMe(@Body req: UpdateProfileRequest): UserDto

    @Multipart
    @POST("users/me/photos")
    suspend fun uploadPhoto(@Part part: MultipartBody.Part): PhotoUploadResponse

    @DELETE("users/me/photos/{photoId}")
    suspend fun deletePhoto(@Path("photoId") photoId: String)

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserDto

    // ── Discover ──────────────────────────────────────────────────────────
    @GET("discover/feed")
    suspend fun feed(@Query("limit") limit: Int = 10): FeedResponse

    @GET("discover/filters")
    suspend fun getFilters(): FiltersDto

    @PUT("discover/filters")
    suspend fun setFilters(@Body filters: FiltersDto): FiltersDto

    @POST("discover/swipe")
    suspend fun swipe(@Body req: SwipeRequest): SwipeResponse

    @POST("discover/rewind")
    suspend fun rewind(): UserDto

    @POST("discover/boost")
    suspend fun boost(): BoostResponse

    // ── Matches & Chat ────────────────────────────────────────────────────
    @GET("matches")
    suspend fun matches(): MatchListResponse

    @DELETE("matches/{id}")
    suspend fun unmatch(@Path("id") id: String)

    @GET("matches/{id}/messages")
    suspend fun messages(
        @Path("id") matchId: String,
        @Query("limit") limit: Int = 50,
        @Query("before") before: String? = null,
    ): MessageListResponse

    @POST("matches/{id}/messages")
    suspend fun sendMessage(
        @Path("id") matchId: String,
        @Body req: SendMessageRequest,
    ): MessageDto

    // ── Workouts ──────────────────────────────────────────────────────────
    @GET("workouts")
    suspend fun workouts(): WorkoutListResponse

    @POST("workouts")
    suspend fun createWorkout(@Body req: CreateWorkoutRequest): WorkoutDto

    @GET("workouts/{id}")
    suspend fun getWorkout(@Path("id") id: String): WorkoutDto

    @PATCH("workouts/{id}")
    suspend fun updateWorkout(@Path("id") id: String, @Body workout: WorkoutDto): WorkoutDto

    @POST("workouts/{id}/finish")
    suspend fun finishWorkout(@Path("id") id: String): WorkoutDto

    @GET("workouts/stats")
    suspend fun workoutStats(): WorkoutStatsDto

    // ── Exercises ─────────────────────────────────────────────────────────
    @GET("exercises")
    suspend fun exercises(
        @Query("muscle") muscle: String? = null,
        @Query("search") search: String? = null,
    ): ExerciseListResponse

    @POST("exercises")
    suspend fun createExercise(@Body req: CreateExerciseRequest): ExerciseDto

    @GET("exercises/{id}")
    suspend fun getExercise(@Path("id") id: String): ExerciseDto

    // ── Routines ──────────────────────────────────────────────────────────
    @GET("routines")
    suspend fun routines(): RoutineListResponse

    @POST("routines")
    suspend fun createRoutine(@Body req: CreateRoutineRequest): RoutineDto

    @GET("routines/{id}")
    suspend fun getRoutine(@Path("id") id: String): RoutineDto

    @PATCH("routines/{id}")
    suspend fun updateRoutine(@Path("id") id: String, @Body req: CreateRoutineRequest): RoutineDto

    @DELETE("routines/{id}")
    suspend fun deleteRoutine(@Path("id") id: String)

    // ── Friends ───────────────────────────────────────────────────────────
    @GET("friends")
    suspend fun friends(): FriendListResponse

    @POST("friends/requests")
    suspend fun sendFriendRequest(@Body req: CreateFriendRequest)

    @GET("friends/requests")
    suspend fun friendRequests(): FriendRequestListResponse

    @POST("friends/requests/{id}/accept")
    suspend fun acceptFriendRequest(@Path("id") id: String)

    @POST("friends/requests/{id}/decline")
    suspend fun declineFriendRequest(@Path("id") id: String)

    @DELETE("friends/{id}")
    suspend fun removeFriend(@Path("id") id: String)

    // ── Leaderboard ───────────────────────────────────────────────────────
    @GET("leaderboard")
    suspend fun leaderboard(
        @Query("scope") scope: String = "global",
        @Query("period") period: String = "week",
    ): LeaderboardResponse

    // ── Nutrition ─────────────────────────────────────────────────────────
    @GET("nutrition/day")
    suspend fun nutritionDay(@Query("date") date: String): NutritionDayDto

    @POST("nutrition/meals")
    suspend fun addMeal(@Body req: MealDto): MealDto

    @DELETE("nutrition/meals/{id}")
    suspend fun deleteMeal(@Path("id") id: String)

    // ── User search ───────────────────────────────────────────────────────
    @GET("users/search")
    suspend fun searchUserByHandle(@Query("handle") handle: String): UserDto

    // ── Medals / Quests ───────────────────────────────────────────────────
    @GET("medals")
    suspend fun medals(): MedalListResponse

    @GET("quests")
    suspend fun quests(): QuestListResponse

    // ── Calendar ──────────────────────────────────────────────────────────
    @GET("calendar")
    suspend fun calendar(@Query("month") month: String): CalendarResponse
}
