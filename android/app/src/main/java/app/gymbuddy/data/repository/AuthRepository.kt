package app.gymbuddy.data.repository

import app.gymbuddy.data.local.TokenStore
import app.gymbuddy.data.remote.ApiService
import app.gymbuddy.data.remote.dto.AuthRequestGoogle
import app.gymbuddy.data.remote.dto.AuthRequestLogin
import app.gymbuddy.data.remote.dto.AuthRequestRegister
import app.gymbuddy.data.remote.dto.AuthResponse
import app.gymbuddy.data.remote.dto.UserDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val tokenStore: TokenStore,
) {
    val tokenFlow get() = tokenStore.tokenFlow

    suspend fun login(email: String, password: String): Result<AuthResponse> = runCatching {
        val res = api.login(AuthRequestLogin(email, password))
        tokenStore.setToken(res.token)
        res
    }

    suspend fun register(email: String, password: String, name: String, age: Int): Result<AuthResponse> = runCatching {
        val res = api.register(AuthRequestRegister(email, password, name, age))
        tokenStore.setToken(res.token)
        res
    }

    suspend fun loginWithGoogle(idToken: String): Result<AuthResponse> = runCatching {
        val res = api.loginWithGoogle(AuthRequestGoogle(idToken))
        tokenStore.setToken(res.token)
        res
    }

    suspend fun me(): Result<UserDto> = runCatching { api.me() }

    suspend fun logout() {
        tokenStore.setToken(null)
    }
}
