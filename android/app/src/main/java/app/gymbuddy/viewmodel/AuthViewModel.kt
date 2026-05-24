package app.gymbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gymbuddy.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val age: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun setEmail(v: String) { _state.value = _state.value.copy(email = v, error = null) }
    fun setPassword(v: String) { _state.value = _state.value.copy(password = v, error = null) }
    fun setName(v: String) { _state.value = _state.value.copy(name = v, error = null) }
    fun setAge(v: String) { _state.value = _state.value.copy(age = v, error = null) }

    fun login() {
        val s = _state.value
        if (s.email.isBlank() || s.password.isBlank()) {
            _state.value = s.copy(error = "Email and password required")
            return
        }
        _state.value = s.copy(loading = true)
        viewModelScope.launch {
            repo.login(s.email.trim(), s.password)
                .onSuccess { _state.value = _state.value.copy(loading = false, success = true) }
                .onFailure { _state.value = _state.value.copy(loading = false, error = it.message ?: "Login failed") }
        }
    }

    fun register() {
        val s = _state.value
        val age = s.age.toIntOrNull() ?: 18
        if (s.email.isBlank() || s.password.isBlank() || s.name.isBlank()) {
            _state.value = s.copy(error = "All fields required")
            return
        }
        _state.value = s.copy(loading = true)
        viewModelScope.launch {
            repo.register(s.email.trim(), s.password, s.name.trim(), age)
                .onSuccess { _state.value = _state.value.copy(loading = false, success = true) }
                .onFailure { _state.value = _state.value.copy(loading = false, error = it.message ?: "Registration failed") }
        }
    }
}
