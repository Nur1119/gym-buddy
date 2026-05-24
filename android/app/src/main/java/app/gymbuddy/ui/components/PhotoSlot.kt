package app.gymbuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Stylized photo placeholder used wherever the prototype renders user photos.
 *
 * Produces a gradient block with a 45-degree diagonal stripe pattern overlay
 * and a soft radial vignette, plus an optional monospace label in the corner.
 *
 * Used in: SwipeCard, MatchModal, MatchesScreen, ChatScreen header avatars,
 * EditProfileScreen photo grid.
 */
@Composable
fun PhotoSlot(
    color1: Color,
    color2: Color,
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(color1, color2),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                ),
            )
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    // Diagonal stripe pattern at 45 deg
                    val stripeColor = Color.White.copy(alpha = 0.06f)
                    val stripeWidth = 14.dp.toPx()
                    val phase = stripeWidth * 2
                    val diag = (size.width + size.height) * 1.4f
                    var d = -diag
                    while (d < diag) {
                        val cos45 = cos(Math.toRadians(45.0)).toFloat()
                        val sin45 = sin(Math.toRadians(45.0)).toFloat()
                        drawLine(
                            color = stripeColor,
                            start = Offset(d, -diag),
                            end = Offset(d + diag * cos45, diag * sin45),
                            strokeWidth = stripeWidth,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(stripeWidth, stripeWidth), 0f),
                        )
                        d += phase
                    }
                    // Soft vignette top-center
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White.copy(alpha = 0.18f), Color.Transparent),
                            center = Offset(size.width / 2f, size.height * 0.3f),
                            radius = size.minDimension * 0.6f,
                        ),
                        size = Size(size.width, size.height),
                    )
                }
            },
    ) {
        if (label != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0x59000000))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
            ) {
                Text(
                    text = label,
                    color = Color.White.copy(alpha = 0.85f),
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.3.sp,
                    ),
                )
            }
        }
    }
}
