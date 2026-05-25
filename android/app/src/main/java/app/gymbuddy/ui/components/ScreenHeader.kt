package app.gymbuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gymbuddy.theme.GymTheme

/**
 * Top header shown on every screen.
 *
 * @param large when true uses the iOS "large title" style: left-aligned, 22sp.
 *              Otherwise center-aligned 17sp.
 */
@Composable
fun ScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
    left: (@Composable () -> Unit)? = null,
    right: (@Composable () -> Unit)? = null,
    large: Boolean = false,
) {
    val tokens = GymTheme.tokens
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = if (large) 56.dp else 48.dp)
            .padding(horizontal = 18.dp, vertical = if (large) 8.dp else 6.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            color = tokens.surface.text,
            style = TextStyle(
                fontSize = if (large) 22.sp else 17.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = if (large) (-0.5).sp else 0.sp,
            ),
        )
        if (left != null) {
            Box(
                modifier = Modifier.align(Alignment.CenterStart).heightIn(min = 44.dp),
                contentAlignment = Alignment.CenterStart,
            ) { left() }
        }
        if (right != null) {
            Box(
                modifier = Modifier.align(Alignment.CenterEnd).heightIn(min = 44.dp),
                contentAlignment = Alignment.CenterEnd,
            ) { right() }
        }
    }
}

/**
 * Circular icon button used in headers.
 */
@Composable
fun IconButton(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color? = null,
    tint: Color? = null,
    badge: Int? = null,
    size: androidx.compose.ui.unit.Dp = 36.dp,
    iconSize: androidx.compose.ui.unit.Dp = 20.dp,
) {
    val tokens = GymTheme.tokens
    val bg = background ?: tokens.surface.chip
    val fg = tint ?: tokens.surface.text
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        AppIcon(name, size = iconSize, tint = fg)
        if (badge != null && badge > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(tokens.surface.danger),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = badge.toString(),
                    color = Color.White,
                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    action: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val tokens = GymTheme.tokens
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            color = tokens.surface.text,
            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold),
        )
        if (action != null) {
            Text(
                text = action,
                color = tokens.accent.p2,
                modifier = Modifier.clickable(enabled = onAction != null) { onAction?.invoke() },
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
            )
        }
    }
}
