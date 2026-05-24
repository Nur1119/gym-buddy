package app.gymbuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * Three-up "this week" tile (value, label, optional accent icon).
 */
@Composable
fun StatTile(
    value: String,
    label: String,
    icon: String? = null,
    color: Color? = null,
    modifier: Modifier = Modifier,
) {
    val tokens = GymTheme.tokens
    val tintColor = color ?: tokens.accent.p2
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(tokens.surface.surface)
            .border(1.dp, tokens.surface.border, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (icon != null) AppIcon(icon, size = 14.dp, tint = tintColor)
            Text(
                text = label.uppercase(),
                color = tokens.surface.textDim,
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp),
            )
        }
        Text(
            text = value,
            color = tokens.surface.text,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold),
        )
    }
}
