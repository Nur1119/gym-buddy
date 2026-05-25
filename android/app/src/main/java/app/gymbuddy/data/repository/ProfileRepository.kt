package app.gymbuddy.data.repository

import app.gymbuddy.data.remote.ApiService
import app.gymbuddy.data.remote.dto.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun me(): Result<UserDto> = runCatching { api.getMe() }

    suspend fun update(req: UpdateProfileRequest): Result<UserDto> = runCatching {
        api.updateMe(req)
    }

    suspend fun friends(): Result<List<FriendDto>> = runCatching { api.friends().items }

    suspend fun friendRequests(): Result<List<FriendRequestDto>> = runCatching {
        api.friendRequests().items
    }

    suspend fun searchByHandle(handle: String): Result<UserDto> = runCatching {
        api.searchUserByHandle(handle)
    }

    suspend fun sendFriendRequest(userId: String): Result<Unit> = runCatching {
        api.sendFriendRequest(app.gymbuddy.data.remote.dto.CreateFriendRequest(userId))
    }

    suspend fun leaderboard(scope: String, period: String): Result<List<LeaderboardEntry>> = runCatching {
        api.leaderboard(scope, period).items
    }

    suspend fun medals(): Result<List<MedalDto>> = runCatching { api.medals().items }

    suspend fun quests(): Result<List<QuestDto>> = runCatching { api.quests().items }

    suspend fun nutritionDay(date: String): Result<NutritionDayDto> = runCatching {
        api.nutritionDay(date)
    }

    suspend fun calendar(month: String): Result<CalendarResponse> = runCatching {
        api.calendar(month)
    }
}
