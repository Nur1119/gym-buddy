package app.gymbuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gymbuddy.R
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme

/**
 * Five-tab bottom navigation: Home / Workout / Discover / Friends / Profile.
 * The Discover icon switches to filled heart when active to match the prototype.
 */
@Composable
fun BottomNavBar(
    current: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = GymTheme.tokens
    val tabs = listOf(
        BottomNavItem("home", tr(R.string.home), "home"),
        BottomNavItem("workout", tr(R.string.workout), "dumbbell"),
        BottomNavItem("discover", tr(R.string.discover), "heart"),
        BottomNavItem("friends", tr(R.string.friends), "users"),
        BottomNavItem("profile", tr(R.string.profile), "user"),
    )
    val sysBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val bottomInset = sysBarPadding.calculateBottomPadding()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(tokens.surface.surface)
            .border(1.dp, tokens.surface.border, RoundedCornerShape(0.dp))
            .padding(start = 6.dp, end = 6.dp, top = 8.dp, bottom = bottomInset + 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top,
    ) {
        tabs.forEach { item ->
            val active = current == item.key
            BottomNavCell(item = item, active = active, onClick = { onSelect(item.key) })
        }
    }
}

private data class BottomNavItem(val key: String, val label: String, val icon: String)

@Composable
private fun BottomNavCell(item: BottomNavItem, active: Boolean, onClick: () -> Unit) {
    val tokens = GymTheme.tokens
    val color = if (active) tokens.accent.p2 else tokens.surface.textDim
    val iconName = if (item.key == "discover" && active) "heart-fill" else item.icon
    Column(
        modifier = Modifier
            .width(64.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(28.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (active) tokens.accent.gradientSoft() else SolidColor(Color.Transparent)),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(iconName, size = 22.dp, tint = color)
        }
        Text(
            text = item.label,
            color = color,
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}
