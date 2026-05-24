package app.gymbuddy.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset

/**
 * GymBuddy palette and color tokens. Matches /docs/DESIGN_TOKENS.md.
 *
 * The app supports a [ThemeMode] (light/dark) and an [AccentPalette]
 * (Aurora/Sunset/Neon) — both are switchable at runtime and persisted.
 */

// ── Light surface tokens
object LightColors {
    val bg = Color(0xFFF4F6FB)
    val surface = Color(0xFFFFFFFF)
    val surface2 = Color(0xFFF1F4FA)
    val surfaceElevated = Color(0xFFFFFFFF)
    val border = Color(0xFFE4E8F0)
    val borderStrong = Color(0xFFD2D8E3)
    val text = Color(0xFF0B1020)
    val textMuted = Color(0xFF5B6478)
    val textDim = Color(0xFF8B92A8)
    val overlay = Color(0x73000000)
    val chip = Color(0xFFEEF1F8)
    val danger = Color(0xFFFF3B5C)
    val success = Color(0xFF19C37D)
    val warn = Color(0xFFFFB020)
}

object DarkColors {
    val bg = Color(0xFF0A0E1A)
    val surface = Color(0xFF141A2B)
    val surface2 = Color(0xFF1B2238)
    val surfaceElevated = Color(0xFF1F2740)
    val border = Color(0xFF262E47)
    val borderStrong = Color(0xFF39426A)
    val text = Color(0xFFFFFFFF)
    val textMuted = Color(0xFFA2ABC5)
    val textDim = Color(0xFF6A7390)
    val overlay = Color(0x99000000)
    val chip = Color(0xFF1F2740)
    val danger = Color(0xFFFF4D6D)
    val success = Color(0xFF3DDC97)
    val warn = Color(0xFFFFC857)
}

/** Surface/text tokens for a given mode. */
data class SurfaceTokens(
    val bg: Color,
    val surface: Color,
    val surface2: Color,
    val surfaceElevated: Color,
    val border: Color,
    val borderStrong: Color,
    val text: Color,
    val textMuted: Color,
    val textDim: Color,
    val overlay: Color,
    val chip: Color,
    val danger: Color,
    val success: Color,
    val warn: Color,
)

val LightSurfaceTokens = SurfaceTokens(
    bg = LightColors.bg,
    surface = LightColors.surface,
    surface2 = LightColors.surface2,
    surfaceElevated = LightColors.surfaceElevated,
    border = LightColors.border,
    borderStrong = LightColors.borderStrong,
    text = LightColors.text,
    textMuted = LightColors.textMuted,
    textDim = LightColors.textDim,
    overlay = LightColors.overlay,
    chip = LightColors.chip,
    danger = LightColors.danger,
    success = LightColors.success,
    warn = LightColors.warn,
)

val DarkSurfaceTokens = SurfaceTokens(
    bg = DarkColors.bg,
    surface = DarkColors.surface,
    surface2 = DarkColors.surface2,
    surfaceElevated = DarkColors.surfaceElevated,
    border = DarkColors.border,
    borderStrong = DarkColors.borderStrong,
    text = DarkColors.text,
    textMuted = DarkColors.textMuted,
    textDim = DarkColors.textDim,
    overlay = DarkColors.overlay,
    chip = DarkColors.chip,
    danger = DarkColors.danger,
    success = DarkColors.success,
    warn = DarkColors.warn,
)

/** Accent colors — shared chroma, swappable hues. */
data class AccentTokens(
    val p1: Color,
    val p2: Color,
    val p3: Color,
    val like: Color,
    val nope: Color,
    val superLike: Color,
    val boost: Color,
) {
    /** 135-degree gradient p3 → p2 → p1, suitable for backgrounds/text fill. */
    fun gradient(): Brush =
        Brush.linearGradient(
            colorStops = arrayOf(
                0f to p3,
                0.55f to p2,
                1f to p1,
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

    /** Soft version: each stop at 18% alpha. */
    fun gradientSoft(): Brush =
        Brush.linearGradient(
            colorStops = arrayOf(
                0f to p3.copy(alpha = 0.18f),
                0.55f to p2.copy(alpha = 0.18f),
                1f to p1.copy(alpha = 0.18f),
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )
}

enum class AccentPalette(val key: String) {
    Aurora("aurora"), Sunset("sunset"), Neon("neon");

    fun tokens(): AccentTokens = when (this) {
        Aurora -> AccentTokens(
            p1 = Color(0xFF3DDC97),
            p2 = Color(0xFF00C2FF),
            p3 = Color(0xFF7C5CFF),
            like = Color(0xFF3DDC97),
            nope = Color(0xFFFF4D6D),
            superLike = Color(0xFF00C2FF),
            boost = Color(0xFFB967FF),
        )
        Sunset -> AccentTokens(
            p1 = Color(0xFFFFB020),
            p2 = Color(0xFFFF4D6D),
            p3 = Color(0xFFB967FF),
            like = Color(0xFF19C37D),
            nope = Color(0xFFFF4D6D),
            superLike = Color(0xFF00C2FF),
            boost = Color(0xFFFFB020),
        )
        Neon -> AccentTokens(
            p1 = Color(0xFF00E5FF),
            p2 = Color(0xFF39FF14),
            p3 = Color(0xFFFFEB3B),
            like = Color(0xFF39FF14),
            nope = Color(0xFFFF1744),
            superLike = Color(0xFF00E5FF),
            boost = Color(0xFFFFEB3B),
        )
    }

    companion object {
        fun fromKey(key: String?): AccentPalette = entries.firstOrNull { it.key == key } ?: Aurora
    }
}

enum class ThemeMode(val key: String) {
    Light("light"), Dark("dark"), System("system");

    companion object {
        fun fromKey(key: String?): ThemeMode = entries.firstOrNull { it.key == key } ?: System
    }
}
