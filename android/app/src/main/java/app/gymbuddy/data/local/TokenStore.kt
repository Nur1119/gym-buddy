package app.gymbuddy.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists the JWT token and app-level preferences (accent palette, theme mode, locale).
 */
private val Context.dataStore by preferencesDataStore("gymbuddy_prefs")

@Singleton
class TokenStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val TOKEN = stringPreferencesKey("auth_token")
        val ACCENT = stringPreferencesKey("accent")
        val THEME = stringPreferencesKey("theme_mode")
        val LOCALE = stringPreferencesKey("locale")
    }

    val tokenFlow: Flow<String?> =
        context.dataStore.data.map { it[Keys.TOKEN] }

    val accentFlow: Flow<String?> =
        context.dataStore.data.map { it[Keys.ACCENT] }

    val themeModeFlow: Flow<String?> =
        context.dataStore.data.map { it[Keys.THEME] }

    val localeFlow: Flow<String?> =
        context.dataStore.data.map { it[Keys.LOCALE] }

    suspend fun setToken(token: String?) {
        context.dataStore.edit { prefs ->
            if (token.isNullOrBlank()) prefs.remove(Keys.TOKEN)
            else prefs[Keys.TOKEN] = token
        }
    }

    suspend fun setAccent(accent: String) {
        context.dataStore.edit { it[Keys.ACCENT] = accent }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { it[Keys.THEME] = mode }
    }

    suspend fun setLocale(locale: String) {
        context.dataStore.edit { it[Keys.LOCALE] = locale }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
