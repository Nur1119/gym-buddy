package app.gymbuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.gymbuddy.theme.GymTheme

/**
 * Slim horizontal progress bar with rounded ends. Defaults to accent gradient.
 */
@Composable
fun ProgressBar(
    value: Float,
    max: Float = 1f,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    color: Color? = null,
) {
    val tokens = GymTheme.tokens
    val fraction = (value / max).coerceIn(0f, 1f)
    val fillBrush: Brush = color?.let { SolidColor(it) } ?: tokens.accent.gradient()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(tokens.surface.chip),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction)
                .clip(RoundedCornerShape(height / 2))
                .background(fillBrush),
        )
    }
}
