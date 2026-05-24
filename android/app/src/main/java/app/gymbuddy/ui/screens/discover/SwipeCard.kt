package app.gymbuddy.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gymbuddy.data.remote.dto.UserDto
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.PhotoSlot
import app.gymbuddy.l10n.LocalAppLocale
import app.gymbuddy.l10n.AppLocale

/**
 * Tinder-style swipe card for a single user.
 *
 * The card draws its own LIKE / NOPE badges based on the horizontal [offsetX]
 * (in px). The caller controls offset + rotation via [Modifier.offset] /
 * [Modifier.graphicsLayer] applied externally; here we only render content.
 */
@Composable
fun SwipeCard(
    user: UserDto,
    offsetX: Float = 0f,
    isTop: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val tokens = GymTheme.tokens
    val locale = LocalAppLocale.current
    val showLike = isTop && offsetX > 60f
    val showNope = isTop && offsetX < -60f

    // Deterministic photo color from user id so each card has a stable accent.
    val seed = (user.id.hashCode() and 0xFFFF)
    val photoColors = pickPhotoColors(seed, tokens.accent.p1, tokens.accent.p2, tokens.accent.p3)

    Box(
        modifier = modifier
            .shadow(20.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF111111)),
    ) {
        PhotoSlot(
            color1 = photoColors.first,
            color2 = photoColors.second,
            modifier = Modifier.fillMaxSize(),
        )

        // Photo dot indicators
        val photoCount = (user.photos.size.takeIf { it > 0 } ?: 4).coerceIn(1, 6)
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            repeat(photoCount) { i ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp))
                        .background(if (i == 0) Color.White else Color.White.copy(alpha = 0.35f)),
                )
            }
        }

        // LIKE badge
        if (showLike) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp, top = 60.dp)
                    .rotate(-18f)
                    .border(4.dp, tokens.accent.like, RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Text(
                    "LIKE",
                    color = tokens.accent.like,
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp),
                )
            }
        }
        if (showNope) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 20.dp, top = 60.dp)
                    .rotate(18f)
                    .border(4.dp, tokens.accent.nope, RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Text(
                    "NOPE",
                    color = tokens.accent.nope,
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp),
                )
            }
        }

        // Bottom gradient for legible text
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                    )
                ),
        )

        // Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 18.dp, end = 18.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = user.name,
                    color = Color.White,
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp),
                )
                Text(
                    text = user.age.toString(),
                    color = Color.White.copy(alpha = 0.95f),
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal),
                )
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF3DDC97))
                    )
                    Text(
                        text = "online",
                        color = Color.White,
                        style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ChipOverlay(icon = "dumbbell", text = user.goal)
                ChipOverlay(icon = "location", text = "${user.gymName ?: "Nearby"} ${if (locale == AppLocale.Ru) "км" else "km"}")
                ChipOverlay(text = user.level)
            }

            if (user.bio.isNotBlank()) {
                Text(
                    text = user.bio,
                    color = Color.White.copy(alpha = 0.95f),
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                )
            }
        }
    }
}

@Composable
private fun ChipOverlay(text: String, icon: String? = null) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White.copy(alpha = 0.18f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (icon != null) AppIcon(icon, size = 11.dp, tint = Color.White)
        Text(text, color = Color.White, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold))
    }
}

private fun pickPhotoColors(seed: Int, p1: Color, p2: Color, p3: Color): Pair<Color, Color> {
    val palette = listOf(
        p3 to p2,
        p2 to p1,
        p1 to p3,
        Color(0xFFFFB020) to Color(0xFFFF4D6D),
        Color(0xFF7C5CFF) to Color(0xFF00C2FF),
    )
    return palette[(seed % palette.size + palette.size) % palette.size]
}
