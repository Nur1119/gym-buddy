package app.gymbuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gymbuddy.theme.GymTheme

/**
 * Compact rounded pill / chip used for filters, goals, etc.
 */
@Composable
fun Pill(
    text: String,
    active: Boolean = false,
    onClick: () -> Unit = {},
    color: Color? = null,
    modifier: Modifier = Modifier,
) {
    val tokens = GymTheme.tokens
    val activeColor = color ?: tokens.accent.p2
    val bg = if (active) activeColor else tokens.surface.chip
    val fg = if (active) Color.White else tokens.surface.textMuted
    Text(
        text = text,
        color = fg,
        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    )
}
