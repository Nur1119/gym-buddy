package app.gymbuddy.data.repository

import app.gymbuddy.data.remote.ApiService
import app.gymbuddy.data.remote.dto.FiltersDto
import app.gymbuddy.data.remote.dto.SwipeRequest
import app.gymbuddy.data.remote.dto.SwipeResponse
import app.gymbuddy.data.remote.dto.UserDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun feed(limit: Int = 10): Result<List<UserDto>> = runCatching {
        api.feed(limit).items
    }

    suspend fun swipe(targetUserId: String, direction: String): Result<SwipeResponse> = runCatching {
        api.swipe(SwipeRequest(targetUserId, direction))
    }

    suspend fun rewind(): Result<UserDto> = runCatching { api.rewind() }

    suspend fun boost(): Result<String> = runCatching { api.boost().expiresAt }

    suspend fun filters(): Result<FiltersDto> = runCatching { api.getFilters() }

    suspend fun updateFilters(filters: FiltersDto): Result<FiltersDto> = runCatching {
        api.setFilters(filters)
    }
}
