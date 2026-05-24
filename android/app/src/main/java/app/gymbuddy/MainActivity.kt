package app.gymbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import app.gymbuddy.nav.GymBuddyNavGraph
import app.gymbuddy.theme.GymBuddyTheme
import app.gymbuddy.l10n.LocalAppLocale
import app.gymbuddy.viewmodel.AppSettingsViewModel
import androidx.compose.runtime.CompositionLocalProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val settingsVm: AppSettingsViewModel = hiltViewModel()
            val state by settingsVm.state.collectAsState()
            val accent = state.accent
            val mode = state.themeMode
            val locale = state.locale
            CompositionLocalProvider(LocalAppLocale provides locale) {
                GymBuddyTheme(
                    accent = accent,
                    themeMode = mode
                ) {
                    GymBuddyNavGraph(settingsVm = settingsVm)
                }
            }
        }
    }
}
