package app.gymbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gymbuddy.data.remote.dto.ExerciseDto
import app.gymbuddy.data.remote.dto.RoutineDto
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
