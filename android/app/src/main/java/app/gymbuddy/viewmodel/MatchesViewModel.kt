package app.gymbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gymbuddy.data.remote.dto.MatchDto
import app.gymbuddy.data.repository.MatchesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchesUiState(
    val matches: List<MatchDto> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val repo: MatchesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MatchesUiState())
    val state: StateFlow<MatchesUiState> = _state

    init { refresh() }

    fun refresh() {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
            repo.matches()
                .onSuccess { _state.value = _state.value.copy(loading = false, matches = it, error = null) }
                .onFailure { _state.value = _state.value.copy(loading = false, error = it.message) }
        }
    }
}
