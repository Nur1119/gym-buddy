package app.gymbuddy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RoutineExerciseDto(
    val exerciseId: String,
    val sets: Int,
    val targetReps: Int,
    val restSec: Int = 90,
)

@Serializable
data class RoutineDto(
    val id: String,
    val name: String,
    val exercises: List<RoutineExerciseDto> = emptyList(),
    val totalSets: Int = 0,
    val estimatedDurationMin: Int = 0,
    val color: String = "#7C5CFF",
    val ownerId: String = "",
    val createdAt: String = "",
)

@Serializable
data class RoutineListResponse(
    val items: List<RoutineDto>,
    val nextCursor: String? = null,
)

@Serializable
data class CreateRoutineRequest(
    val name: String,
    val exercises: List<RoutineExerciseDto>,
)
