package app.gymbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gymbuddy.data.remote.dto.MessageDto
import app.gymbuddy.data.repository.MatchesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<MessageDto> = emptyList(),
    val draft: String = "",
    val loading: Boolean = false,
    val sending: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repo: MatchesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state

    private var matchId: String? = null

    fun bind(id: String) {
        if (matchId == id) return
        matchId = id
        load(id)
        repo.connectStream(id, viewModelScope)
        repo.webSocketEvents.onEach { ev ->
            if (ev.type == "message" && ev.data != null) {
                _state.value = _state.value.copy(
                    messages = _state.value.messages + ev.data,
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun load(id: String) {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
            repo.messages(id)
                .onSuccess { _state.value = _state.value.copy(loading = false, messages = it.reversed()) }
                .onFailure { _state.value = _state.value.copy(loading = false, error = it.message) }
        }
    }

    fun setDraft(v: String) { _state.value = _state.value.copy(draft = v) }

    fun send() {
        val id = matchId ?: return
        val text = _state.value.draft.trim()
        if (text.isEmpty()) return
        _state.value = _state.value.copy(sending = true, draft = "")
        viewModelScope.launch {
            repo.sendMessage(id, text)
                .onSuccess { msg ->
                    _state.value = _state.value.copy(
                        sending = false,
                        messages = _state.value.messages + msg,
                    )
                }
                .onFailure { _state.value = _state.value.copy(sending = false, error = it.message) }
        }
    }

    override fun onCleared() {
        repo.disconnectStream()
        super.onCleared()
    }
}
