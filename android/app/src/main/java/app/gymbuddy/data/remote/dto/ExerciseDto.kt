package app.gymbuddy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    val id: String,
    val name: String,
    val muscle: String,
    val equipment: String,
    val icon: String = "",
    val notes: String? = null,
    val isCustom: Boolean = false,
    val ownerId: String? = null,
)

@Serializable
data class ExerciseListResponse(
    val items: List<ExerciseDto>,
    val nextCursor: String? = null,
)

@Serializable
data class CreateExerciseRequest(
    val name: String,
    val muscle: String,
    val equipment: String,
    val notes: String? = null,
)
