package app.gymbuddy.data.repository

import app.gymbuddy.data.remote.ApiService
import app.gymbuddy.data.remote.dto.CreateExerciseRequest
import app.gymbuddy.data.remote.dto.ExerciseDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun list(muscle: String? = null, search: String? = null): Result<List<ExerciseDto>> = runCatching {
        api.exercises(muscle = muscle?.takeIf { it != "all" }, search = search).items
    }

    suspend fun create(req: CreateExerciseRequest): Result<ExerciseDto> = runCatching {
        api.createExercise(req)
    }
}
