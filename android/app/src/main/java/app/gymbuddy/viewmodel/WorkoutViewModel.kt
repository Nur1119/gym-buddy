package app.gymbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gymbuddy.data.remote.dto.CreateRoutineRequest
import app.gymbuddy.data.remote.dto.ExerciseDto
import app.gymbuddy.data.remote.dto.RoutineDto
import app.gymbuddy.data.remote.dto.RoutineExerciseDto
import app.gymbuddy.data.remote.dto.WorkoutDto
import app.gymbuddy.data.repository.ExerciseRepository
import app.gymbuddy.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutTrackerUiState(
    val routines: List<RoutineDto> = emptyList(),
    val recentWorkouts: List<WorkoutDto> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class WorkoutTrackerViewModel @Inject constructor(
    private val repo: WorkoutRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutTrackerUiState())
    val state: StateFlow<WorkoutTrackerUiState> = _state

    init { load() }

    fun load() {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
            val routines = repo.routines().getOrDefault(emptyList())
            val workouts = repo.listWorkouts().getOrDefault(emptyList())
            _state.value = WorkoutTrackerUiState(
                routines = routines,
                recentWorkouts = workouts,
                loading = false,
            )
        }
    }
}

data class ExercisesUiState(
    val items: List<ExerciseDto> = emptyList(),
    val muscle: String = "all",
    val query: String = "",
    val loading: Boolean = false,
)

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val repo: ExerciseRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ExercisesUiState())
    val state: StateFlow<ExercisesUiState> = _state

    init { reload() }

    fun setMuscle(m: String) {
        _state.value = _state.value.copy(muscle = m); reload()
    }

    fun setQuery(q: String) {
        _state.value = _state.value.copy(query = q); reload()
    }

    fun reload() {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
            val list = repo.list(
                muscle = _state.value.muscle.takeIf { it != "all" },
                search = _state.value.query.ifBlank { null },
            ).getOrDefault(emptyList())
            _state.value = _state.value.copy(loading = false, items = list)
        }
    }
}

data class RoutineExerciseEntry(
    val exercise: ExerciseDto,
    val sets: Int = 3,
    val reps: Int = 10,
    val restSec: Int = 90,
)

data class CreateRoutineUiState(
    val allExercises: List<ExerciseDto> = emptyList(),
    val added: List<RoutineExerciseEntry> = emptyList(),
    val query: String = "",
    val saving: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class CreateRoutineViewModel @Inject constructor(
    private val exerciseRepo: ExerciseRepository,
    private val workoutRepo: WorkoutRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CreateRoutineUiState())
    val state: StateFlow<CreateRoutineUiState> = _state

    init {
        viewModelScope.launch {
            val exercises = exerciseRepo.list(null, null).getOrDefault(emptyList())
            _state.value = _state.value.copy(allExercises = exercises)
        }
    }

    fun setQuery(q: String) { _state.value = _state.value.copy(query = q) }

    fun addExercise(ex: ExerciseDto) {
        if (_state.value.added.any { it.exercise.id == ex.id }) return
        _state.value = _state.value.copy(added = _state.value.added + RoutineExerciseEntry(ex))
    }

    fun removeExercise(exerciseId: String) {
        _state.value = _state.value.copy(added = _state.value.added.filter { it.exercise.id != exerciseId })
    }

    fun updateSets(exerciseId: String, sets: Int) {
        _state.value = _state.value.copy(added = _state.value.added.map {
            if (it.exercise.id == exerciseId) it.copy(sets = sets.coerceAtLeast(1)) else it
        })
    }

    fun updateReps(exerciseId: String, reps: Int) {
        _state.value = _state.value.copy(added = _state.value.added.map {
            if (it.exercise.id == exerciseId) it.copy(reps = reps.coerceAtLeast(1)) else it
        })
    }

    fun save(name: String, onSaved: () -> Unit) {
        if (name.isBlank()) { _state.value = _state.value.copy(error = "Name required"); return }
        if (_state.value.added.isEmpty()) { _state.value = _state.value.copy(error = "Add at least one exercise"); return }
        _state.value = _state.value.copy(saving = true, error = null)
        viewModelScope.launch {
            workoutRepo.createRoutine(
                CreateRoutineRequest(
                    name = name.trim(),
                    exercises = _state.value.added.map {
                        RoutineExerciseDto(
                            exerciseId = it.exercise.id,
                            sets = it.sets,
                            targetReps = it.reps,
                            restSec = it.restSec,
                        )
                    },
                ),
            ).fold(
                onSuccess = { _state.value = _state.value.copy(saving = false); onSaved() },
                onFailure = { _state.value = _state.value.copy(saving = false, error = it.message) },
            )
        }
    }
}
