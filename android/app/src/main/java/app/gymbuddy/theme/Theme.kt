package app.gymbuddy.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import android.app.Activity

/** Surface + accent token bundle exposed via composition local. */
data class GymBuddyTokens(
    val surface: SurfaceTokens,
    val accent: AccentTokens,
    val isDark: Boolean,
)

val LocalGymBuddyTokens = staticCompositionLocalOf<GymBuddyTokens> {
    GymBuddyTokens(LightSurfaceTokens, AccentPalette.Aurora.tokens(), false)
}

/**
 * Convenience accessor: `GymTheme.tokens` from any composable.
 */
object GymTheme {
    val tokens: GymBuddyTokens
        @Composable get() = LocalGymBuddyTokens.current
}

@Composable
fun GymBuddyTheme(
    accent: AccentPalette,
    themeMode: ThemeMode,
    content: @Composable () -> Unit,
) {
    val isDark = when (themeMode) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }
    val surfaceTokens = if (isDark) DarkSurfaceTokens else LightSurfaceTokens
    val accentTokens = accent.tokens()

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = accentTokens.p2,
            onPrimary = Color.White,
            secondary = accentTokens.p3,
            onSecondary = Color.White,
            tertiary = accentTokens.p1,
            background = surfaceTokens.bg,
            onBackground = surfaceTokens.text,
            surface = surfaceTokens.surface,
            onSurface = surfaceTokens.text,
            surfaceVariant = surfaceTokens.surface2,
            onSurfaceVariant = surfaceTokens.textMuted,
            outline = surfaceTokens.border,
            outlineVariant = surfaceTokens.borderStrong,
            error = surfaceTokens.danger,
            onError = Color.White,
        )
    } else {
        lightColorScheme(
            primary = accentTokens.p2,
            onPrimary = Color.White,
            secondary = accentTokens.p3,
            onSecondary = Color.White,
            tertiary = accentTokens.p1,
            background = surfaceTokens.bg,
            onBackground = surfaceTokens.text,
            surface = surfaceTokens.surface,
            onSurface = surfaceTokens.text,
            surfaceVariant = surfaceTokens.surface2,
            onSurfaceVariant = surfaceTokens.textMuted,
            outline = surfaceTokens.border,
            outlineVariant = surfaceTokens.borderStrong,
            error = surfaceTokens.danger,
            onError = Color.White,
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
        }
    }

    CompositionLocalProvider(
        LocalGymBuddyTokens provides GymBuddyTokens(surfaceTokens, accentTokens, isDark)
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = GymBuddyTypography,
            content = content,
        )
    }
}
