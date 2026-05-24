package app.gymbuddy.l10n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import java.util.Locale

/**
 * App-level locale that can be switched independently of the system locale.
 * The harness wraps the entire app in [CompositionLocalProvider] with [LocalAppLocale].
 *
 * Calling [tr] reads from the appropriate Android Resources object so we still
 * benefit from standard `strings.xml`/`values-ru/strings.xml` lookups.
 */
enum class AppLocale(val tag: String) {
    En("en"), Ru("ru");

    fun toLocale(): Locale = Locale(tag)

    companion object {
        fun fromTag(tag: String?): AppLocale = entries.firstOrNull { it.tag == tag } ?: En
    }
}

val LocalAppLocale = staticCompositionLocalOf { AppLocale.En }

/** Resolve a string resource using the [LocalAppLocale]-overridden Resources. */
@Composable
fun tr(resId: Int): String {
    val ctx = LocalContext.current
    val locale = LocalAppLocale.current
    val config = LocalConfiguration.current
    // Build a localized context so resource lookups honor the override.
    val localizedConfig = android.content.res.Configuration(config).apply {
        setLocale(locale.toLocale())
    }
    val localizedCtx = ctx.createConfigurationContext(localizedConfig)
    return localizedCtx.resources.getString(resId)
}

/** Helper for string format args. */
@Composable
fun tr(resId: Int, vararg args: Any): String {
    val ctx = LocalContext.current
    val locale = LocalAppLocale.current
    val config = LocalConfiguration.current
    val localizedConfig = android.content.res.Configuration(config).apply {
        setLocale(locale.toLocale())
    }
    val localizedCtx = ctx.createConfigurationContext(localizedConfig)
    return localizedCtx.resources.getString(resId, *args)
}
