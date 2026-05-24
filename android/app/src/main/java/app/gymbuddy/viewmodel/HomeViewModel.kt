package app.gymbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gymbuddy.data.remote.dto.QuestDto
import app.gymbuddy.data.remote.dto.RoutineDto
import app.gymbuddy.data.remote.dto.UserDto
import app.gymbuddy.data.remote.dto.WorkoutStatsDto
import app.gymbuddy.data.repository.ProfileRepository
import app.gymbuddy.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val user: UserDto? = null,
    val routines: List<RoutineDto> = emptyList(),
    val quests: List<QuestDto> = emptyList(),
    val stats: WorkoutStatsDto = WorkoutStatsDto(),
    val loading: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepo: ProfileRepository,
    private val workoutRepo: WorkoutRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    init { load() }

    fun load() {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
            val user = profileRepo.me().getOrNull()
            val routines = workoutRepo.routines().getOrDefault(emptyList())
            val quests = profileRepo.quests().getOrDefault(emptyList())
            val stats = workoutRepo.stats().getOrDefault(WorkoutStatsDto())
            _state.value = HomeUiState(
                user = user,
                routines = routines,
                quests = quests,
                stats = stats,
                loading = false,
            )
        }
    }
}
