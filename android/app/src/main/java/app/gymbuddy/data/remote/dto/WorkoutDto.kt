package app.gymbuddy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSetDto(
    val weight: Double,
    val reps: Int,
    val completed: Boolean = false,
    val restSec: Int = 90,
)

@Serializable
data class WorkoutExerciseDto(
    val exerciseId: String,
    val sets: List<WorkoutSetDto> = emptyList(),
)

@Serializable
data class WorkoutDto(
    val id: String,
    val name: String,
    val startedAt: String? = null,
    val finishedAt: String? = null,
    val plannedFor: String? = null,
    val routineId: String? = null,
    val exercises: List<WorkoutExerciseDto> = emptyList(),
    val totalVolumeKg: Double = 0.0,
    val xpAwarded: Int = 0,
)

@Serializable
data class WorkoutListResponse(
    val items: List<WorkoutDto>,
    val nextCursor: String? = null,
)

@Serializable
data class CreateWorkoutRequest(
    val name: String,
    val plannedFor: String? = null,
    val routineId: String? = null,
    val exercises: List<WorkoutExerciseDto> = emptyList(),
)

@Serializable
data class WorkoutStatsDto(
    val weeklyVolume: Double = 0.0,
    val workoutsThisWeek: Int = 0,
    val muscleHeatmap: Map<String, Double> = emptyMap(),
)

@Serializable
data class LeaderboardResponse(
    val items: List<LeaderboardEntry>,
)

@Serializable
data class LeaderboardEntry(
    val rank: Int,
    val user: UserDto,
    val xp: Int,
)

@Serializable
data class MedalDto(
    val id: String,
    val name: String,
    val description: String = "",
    val icon: String = "",
    val unlocked: Boolean = false,
    val unlockedAt: String? = null,
)

@Serializable
data class MedalListResponse(val items: List<MedalDto>)

@Serializable
data class QuestDto(
    val id: String,
    val text: String,
    val progress: Int = 0,
    val total: Int = 1,
    val xp: Int = 0,
    val kind: String = "daily",
)

@Serializable
data class QuestListResponse(val items: List<QuestDto>)

@Serializable
data class MealItemDto(
    val name: String,
    val kcal: Int,
    val protein: Double,
    val carbs: Double,
    val fats: Double,
)

@Serializable
data class MealDto(
    val id: String = "",
    val kind: String,
    val items: List<MealItemDto>,
    val time: String,
)

@Serializable
data class NutritionDayDto(
    val date: String,
    val meals: List<MealDto> = emptyList(),
    val totalKcal: Int = 0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fats: Double = 0.0,
)

@Serializable
data class CalendarDayDto(
    val date: String,
    val workouts: List<WorkoutDto> = emptyList(),
)

@Serializable
data class CalendarResponse(val days: List<CalendarDayDto>)

@Serializable
data class FriendDto(
    val id: String,
    val user: UserDto,
    val online: Boolean = false,
    val lastActive: String? = null,
)

@Serializable
data class FriendListResponse(val items: List<FriendDto>)

@Serializable
data class FriendRequestDto(
    val id: String,
    val fromUser: UserDto,
    val createdAt: String,
)

@Serializable
data class FriendRequestListResponse(val items: List<FriendRequestDto>)

@Serializable
data class CreateFriendRequest(val userId: String)
