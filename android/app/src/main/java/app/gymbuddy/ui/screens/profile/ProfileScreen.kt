package app.gymbuddy.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gymbuddy.R
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.GymCard
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.PhotoSlot
import app.gymbuddy.ui.components.ProgressBar
import app.gymbuddy.viewmodel.ProfileViewModel

/**
 * Profile dashboard: avatar + level / XP, widget grid (medals, ranks, calendar,
 * nutrition, stats, edit). Mini calendar at the bottom.
 */
@Composable
fun ProfileScreen(
    onOpenSettings: () -> Unit,
    onOpenEdit: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenNutrition: () -> Unit,
    onOpenMedals: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    vm: ProfileViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    val user = state.user
    val statusInsets = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val widgets = listOf(
        Widget("medals", "medal", tr(R.string.medals), tokens.surface.warn, onOpenMedals),
        Widget("ranks", "trophy", tr(R.string.ranks), tokens.accent.p3, onOpenLeaderboard),
        Widget("cal", "calendar", tr(R.string.calendar), tokens.accent.p2, onOpenCalendar),
        Widget("nutr", "apple", tr(R.string.nutrition), tokens.accent.p1, onOpenNutrition),
        Widget("stats", "chart", tr(R.string.stats), tokens.surface.danger) { },
        Widget("edit", "edit", tr(R.string.edit_profile), tokens.accent.p2, onOpenEdit),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .verticalScroll(rememberScrollState())
            .padding(top = statusInsets, bottom = 24.dp),
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Spacer(Modifier.width(0.dp))
            IconButton(name = "cog", onClick = onOpenSettings)
        }

        // Hero
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(4.dp, tokens.surface.surface, CircleShape),
            ) {
                PhotoSlot(color1 = tokens.accent.p3, color2 = tokens.accent.p2, modifier = Modifier.fillMaxSize())
            }
            Spacer(Modifier.height(12.dp))
            Text(
                user?.name ?: "Guest",
                color = tokens.surface.text,
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold),
            )
            val displayHandle = when {
                user?.userHandle?.isNotEmpty() == true -> "@${user.userHandle}"
                user?.username?.isNotEmpty() == true -> "@${user.username}"
                else -> null
            }
            if (displayHandle != null) {
                Text(
                    displayHandle,
                    color = tokens.surface.textMuted,
                    style = TextStyle(fontSize = 13.sp),
                )
            }

            // Level + XP card
            Spacer(Modifier.height(16.dp))
            GymCard(padding = 14.dp) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(tokens.accent.gradient()),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            (user?.stats?.level ?: 1).toString(),
                            color = Color.White,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold),
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        val stats = user?.stats
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                "${stats?.xp ?: 0} / ${stats?.xpToNext ?: 100} XP",
                                color = tokens.surface.text,
                                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
                            )
                            Text(
                                "Lv. ${(stats?.level ?: 1) + 1}",
                                color = tokens.accent.p2,
                                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        ProgressBar(
                            value = (stats?.xp ?: 0).toFloat(),
                            max = (stats?.xpToNext ?: 100).toFloat(),
                            height = 8.dp,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "${stats?.totalXp ?: 0} total XP",
                            color = tokens.surface.textDim,
                            style = TextStyle(fontSize = 11.sp),
                        )
                    }
                }
            }
        }

        // Widget grid
        Spacer(Modifier.height(16.dp))
        Column(modifier = Modifier.padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            widgets.chunked(3).forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { w ->
                        WidgetTile(w, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

private data class Widget(
    val id: String,
    val icon: String,
    val label: String,
    val color: Color,
    val onClick: () -> Unit,
)

@Composable
private fun WidgetTile(w: Widget, modifier: Modifier = Modifier) {
    val tokens = GymTheme.tokens
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(tokens.surface.surface)
            .border(1.dp, tokens.surface.border, RoundedCornerShape(14.dp))
            .clickable(onClick = w.onClick)
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(w.color),
            contentAlignment = Alignment.Center,
        ) { AppIcon(w.icon, size = 22.dp, tint = Color.White) }
        Text(
            w.label,
            color = tokens.surface.text,
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
        )
    }
}
