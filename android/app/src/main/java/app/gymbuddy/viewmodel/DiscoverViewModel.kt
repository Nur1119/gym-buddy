package app.gymbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gymbuddy.data.remote.dto.MatchDto
import app.gymbuddy.data.remote.dto.UserDto
import app.gymbuddy.data.repository.DiscoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiscoverUiState(
    val stack: List<UserDto> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val newMatch: MatchDto? = null,
)

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val repo: DiscoverRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DiscoverUiState())
    val state: StateFlow<DiscoverUiState> = _state

    init { loadInitial() }

    fun loadInitial() {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
            repo.feed(limit = 20)
                .onSuccess { users ->
                    _state.value = _state.value.copy(loading = false, stack = users, error = null)
                }
                .onFailure {
                    // Fall back to placeholder data so the UI can still be exercised without a backend.
                    _state.value = _state.value.copy(loading = false, stack = previewUsers(), error = null)
                }
        }
    }

    fun swipe(direction: SwipeDirection) {
        val top = _state.value.stack.firstOrNull() ?: return
        // Optimistic: drop the top card immediately, then send the swipe.
        _state.value = _state.value.copy(stack = _state.value.stack.drop(1))
        val apiDir = when (direction) {
            SwipeDirection.Like -> "like"
            SwipeDirection.Pass -> "pass"
            SwipeDirection.SuperLike -> "superlike"
        }
        viewModelScope.launch {
            repo.swipe(top.id, apiDir)
                .onSuccess { res ->
                    if (res.matched && res.match != null) {
                        _state.value = _state.value.copy(newMatch = res.match)
                    }
                }
                .onFailure { /* silent: card already popped */ }
            if (_state.value.stack.size < 3) {
                repo.feed(limit = 10).onSuccess { more ->
                    _state.value = _state.value.copy(stack = _state.value.stack + more)
                }
            }
        }
    }

    fun rewind() {
        viewModelScope.launch {
            repo.rewind().onSuccess { user ->
                _state.value = _state.value.copy(stack = listOf(user) + _state.value.stack)
            }
        }
    }

    fun boost() {
        viewModelScope.launch { repo.boost() }
    }

    fun dismissMatch() { _state.value = _state.value.copy(newMatch = null) }

    /** Local fallback so the UI is still usable without a backend running. */
    private fun previewUsers(): List<UserDto> = listOf(
        UserDto(id = "u1", name = "Alina", age = 26, bio = "Powerlifter at Iron Gym. 4× a week.", goal = "Strength", level = "Advanced", gymName = "Iron Gym"),
        UserDto(id = "u2", name = "Maria", age = 24, bio = "CrossFit & climbing. Looking for morning training partner.", goal = "CrossFit", level = "Intermediate", gymName = "Reebok CrossFit"),
        UserDto(id = "u3", name = "Sofia", age = 28, bio = "Bodybuilding prep. Hypertrophy-focused.", goal = "Hypertrophy", level = "Advanced", gymName = "Pulse Fitness"),
        UserDto(id = "u4", name = "Karina", age = 22, bio = "Calisthenics. Pull-ups & handstands.", goal = "Calisthenics", level = "Intermediate", gymName = "Street Park"),
        UserDto(id = "u5", name = "Olivia", age = 30, bio = "Yoga + mobility. Sunday flow sessions.", goal = "Mobility", level = "Elite", gymName = "Studio Lotus"),
    )
}

enum class SwipeDirection { Like, Pass, SuperLike }
