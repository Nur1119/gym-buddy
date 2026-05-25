package app.gymbuddy.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserStatsDto(
    val level: Int = 1,
    val xp: Int = 0,
    val xpToNext: Int = 100,
    val totalXp: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val coins: Int = 0,
    val totalWorkouts: Int = 0,
    val workoutsThisWeek: Int = 0,
)

@Serializable
data class PhotoDto(
    val id: String,
    val url: String,
    val position: Int,
)

@Serializable
data class UserDto(
    val id: String,
    val email: String = "",
    val name: String,
    val username: String = "",
    val age: Int? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val bio: String = "",
    val goal: String = "Hypertrophy",
    val level: String = "Intermediate",
    val gymName: String? = null,
    val gymLat: Double? = null,
    val gymLng: Double? = null,
    val schedule: List<Int> = emptyList(),
    val interests: List<String> = emptyList(),
    val photos: List<PhotoDto> = emptyList(),
    val stats: UserStatsDto = UserStatsDto(),
    val createdAt: String = "",
)

@Serializable
data class AuthRequestLogin(
    val email: String,
    val password: String,
)

@Serializable
data class AuthRequestRegister(
    val email: String,
    val password: String,
    val name: String,
    val age: Int,
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserDto,
)

@Serializable
data class TokenResponse(
    val token: String,
)

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val age: Int? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val bio: String? = null,
    val goal: String? = null,
    val interests: List<String>? = null,
)

@Serializable
data class PhotoUploadResponse(val url: String)

@Serializable
data class ErrorBody(val error: ErrorPayload)

@Serializable
data class ErrorPayload(val code: String, val message: String)

@Serializable
data class AuthRequestGoogle(
    val idToken: String,
)
