package app.gymbuddy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MatchDto(
    val id: String,
    val user: UserDto,
    val matchedAt: String,
    val lastMessage: MessageDto? = null,
    val unreadCount: Int = 0,
    val isUnmatched: Boolean = false,
)

@Serializable
data class MatchListResponse(
    val items: List<MatchDto>,
    val nextCursor: String? = null,
)

@Serializable
data class FiltersDto(
    val ageMin: Int = 18,
    val ageMax: Int = 65,
    val maxDistanceKm: Int = 15,
    val goals: List<String> = emptyList(),
    val levels: List<String> = emptyList(),
    val scheduleDays: List<Int> = emptyList(),
)

@Serializable
data class SwipeRequest(
    val targetUserId: String,
    val direction: String, // "like", "pass", "superlike"
)

@Serializable
data class SwipeResponse(
    val matched: Boolean = false,
    val match: MatchDto? = null,
)

@Serializable
data class FeedResponse(
    val items: List<UserDto>,
)

@Serializable
data class BoostResponse(val expiresAt: String)
