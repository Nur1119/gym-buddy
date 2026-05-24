package app.gymbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gymbuddy.data.local.TokenStore
import app.gymbuddy.l10n.AppLocale
import app.gymbuddy.theme.AccentPalette
import app.gymbuddy.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppSettingsState(
    val themeMode: ThemeMode = ThemeMode.System,
    val accent: AccentPalette = AccentPalette.Aurora,
    val locale: AppLocale = AppLocale.En,
    val isAuthenticated: Boolean = false,
    val tokenLoaded: Boolean = false,
)

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val tokenStore: TokenStore,
) : ViewModel() {

    private val _state = MutableStateFlow(AppSettingsState())
    val state: StateFlow<AppSettingsState> = _state

    init {
        combine(
            tokenStore.tokenFlow,
            tokenStore.accentFlow,
            tokenStore.themeModeFlow,
            tokenStore.localeFlow,
        ) { token, accent, theme, locale ->
            AppSettingsState(
                themeMode = ThemeMode.fromKey(theme),
                accent = AccentPalette.fromKey(accent),
                locale = AppLocale.fromTag(locale),
                isAuthenticated = !token.isNullOrBlank(),
                tokenLoaded = true,
            )
        }.onEach { _state.value = it }.launchIn(viewModelScope)
    }

    fun setAccent(accent: AccentPalette) {
        viewModelScope.launch { tokenStore.setAccent(accent.key) }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { tokenStore.setThemeMode(mode.key) }
    }

    fun toggleDarkMode() {
        val current = _state.value.themeMode
        val next = when (current) {
            ThemeMode.Light -> ThemeMode.Dark
            ThemeMode.Dark -> ThemeMode.Light
            ThemeMode.System -> ThemeMode.Dark
        }
        setThemeMode(next)
    }

    fun setLocale(locale: AppLocale) {
        viewModelScope.launch { tokenStore.setLocale(locale.tag) }
    }

    fun toggleLocale() {
        val next = if (_state.value.locale == AppLocale.En) AppLocale.Ru else AppLocale.En
        setLocale(next)
    }

    fun logout() {
        viewModelScope.launch { tokenStore.setToken(null) }
    }
}
