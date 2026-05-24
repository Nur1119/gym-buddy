package app.gymbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gymbuddy.data.remote.dto.*
import app.gymbuddy.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: UserDto? = null,
    val medals: List<MedalDto> = emptyList(),
    val friends: List<FriendDto> = emptyList(),
    val leaderboard: List<LeaderboardEntry> = emptyList(),
    val loading: Boolean = false,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    init { load() }

    fun load() {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
            val user = repo.me().getOrNull()
            val medals = repo.medals().getOrDefault(emptyList())
            val friends = repo.friends().getOrDefault(emptyList())
            val lb = repo.leaderboard("friends", "week").getOrDefault(emptyList())
            _state.value = ProfileUiState(
                user = user,
                medals = medals,
                friends = friends,
                leaderboard = lb,
                loading = false,
            )
        }
    }

    fun updateProfile(req: UpdateProfileRequest, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repo.update(req).onSuccess { user ->
                _state.value = _state.value.copy(user = user)
                onDone()
            }
        }
    }
}

data class NutritionUiState(
    val day: NutritionDayDto? = null,
    val loading: Boolean = false,
)

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val repo: ProfileRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(NutritionUiState())
    val state: StateFlow<NutritionUiState> = _state

    init { load() }

    fun load(date: String = todayIso()) {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
            val day = repo.nutritionDay(date).getOrNull()
            _state.value = NutritionUiState(day = day, loading = false)
        }
    }

    private fun todayIso(): String {
        val now = java.time.LocalDate.now()
        return now.toString()
    }
}

data class CalendarUiState(
    val response: CalendarResponse? = null,
    val loading: Boolean = false,
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repo: ProfileRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarUiState())
    val state: StateFlow<CalendarUiState> = _state

    init { load() }

    fun load(month: String = currentMonth()) {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
            val res = repo.calendar(month).getOrNull()
            _state.value = CalendarUiState(response = res, loading = false)
        }
    }

    private fun currentMonth(): String {
        val now = java.time.YearMonth.now()
        return now.toString() // YYYY-MM
    }
}
