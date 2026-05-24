package app.gymbuddy.data.repository

import app.gymbuddy.data.remote.ApiService
import app.gymbuddy.data.remote.dto.CreateRoutineRequest
import app.gymbuddy.data.remote.dto.CreateWorkoutRequest
import app.gymbuddy.data.remote.dto.RoutineDto
import app.gymbuddy.data.remote.dto.WorkoutDto
import app.gymbuddy.data.remote.dto.WorkoutStatsDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun listWorkouts(): Result<List<WorkoutDto>> = runCatching {
        api.workouts().items
    }

    suspend fun createWorkout(req: CreateWorkoutRequest): Result<WorkoutDto> = runCatching {
        api.createWorkout(req)
    }

    suspend fun getWorkout(id: String): Result<WorkoutDto> = runCatching {
        api.getWorkout(id)
    }

    suspend fun updateWorkout(id: String, workout: WorkoutDto): Result<WorkoutDto> = runCatching {
        api.updateWorkout(id, workout)
    }

    suspend fun finishWorkout(id: String): Result<WorkoutDto> = runCatching {
        api.finishWorkout(id)
    }

    suspend fun stats(): Result<WorkoutStatsDto> = runCatching { api.workoutStats() }

    suspend fun routines(): Result<List<RoutineDto>> = runCatching { api.routines().items }

    suspend fun createRoutine(req: CreateRoutineRequest): Result<RoutineDto> = runCatching {
        api.createRoutine(req)
    }

    suspend fun getRoutine(id: String): Result<RoutineDto> = runCatching { api.getRoutine(id) }

    suspend fun deleteRoutine(id: String): Result<Unit> = runCatching { api.deleteRoutine(id) }
}
