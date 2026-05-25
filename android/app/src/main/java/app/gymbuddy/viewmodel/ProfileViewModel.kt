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
    val searchResult: UserDto? = null,
    val searchLoading: Boolean = false,
    val searchError: String? = null,
    val friendRequestSent: Boolean = false,
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

    fun searchByHandle(handle: String) {
        val h = handle.trim().removePrefix("@")
        if (h.isEmpty()) return
        _state.value = _state.value.copy(searchLoading = true, searchResult = null, searchError = null, friendRequestSent = false)
        viewModelScope.launch {
            repo.searchByHandle(h).fold(
                onSuccess = { _state.value = _state.value.copy(searchLoading = false, searchResult = it) },
                onFailure = { _state.value = _state.value.copy(searchLoading = false, searchError = "User not found") },
            )
        }
    }

    fun sendFriendRequest(userId: String) {
        viewModelScope.launch {
            repo.sendFriendRequest(userId).onSuccess {
                _state.value = _state.value.copy(friendRequestSent = true)
            }
        }
    }

    fun clearSearch() {
        _state.value = _state.value.copy(searchResult = null, searchError = null, searchLoading = false, friendRequestSent = false)
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
