package app.gymbuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Circular avatar that displays the user's initial over a gradient.
 *
 * `ring=true` adds a colored outer ring (used on Discover matches strip).
 */
@Composable
fun Avatar(
    name: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    color1: Color = Color(0xFF7C5CFF),
    color2: Color = Color(0xFF00C2FF),
    ring: Boolean = false,
    ringColor: Color = Color.White,
) {
    val initial = (name ?: "?").firstOrNull()?.uppercase() ?: "?"
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(color1, color2),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                )
            )
            .let { m -> if (ring) m.border(2.dp, ringColor, CircleShape) else m },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initial,
            color = Color.White,
            style = TextStyle(
                fontSize = (size.value * 0.42f).sp,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}
