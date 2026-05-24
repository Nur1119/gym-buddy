package app.gymbuddy.ui.screens.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gymbuddy.R
import app.gymbuddy.data.remote.dto.RoutineDto
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.Avatar
import app.gymbuddy.ui.components.GymCard
import app.gymbuddy.ui.components.ProgressBar
import app.gymbuddy.ui.components.SectionHeader
import app.gymbuddy.ui.components.StatTile
import app.gymbuddy.viewmodel.HomeViewModel

/**
 * Home dashboard: greeting / streak / coin chips, "Today's plan" hero card,
 * weekly streak strip, active quests, this-week stat tiles, recent routines.
 */
@Composable
fun HomeScreen(
    onStartWorkout: () -> Unit,
    vm: HomeViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    val user = state.user
    val statusInsets = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

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
                .padding(start = 18.dp, end = 18.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Avatar(name = user?.name ?: "G", size = 42.dp, color1 = tokens.accent.p3, color2 = tokens.accent.p2)
                Column {
                    Text(tr(R.string.greeting), color = tokens.surface.textDim, style = TextStyle(fontSize = 12.sp))
                    Text(
                        user?.name ?: "Guest",
                        color = tokens.surface.text,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatChip(icon = "flame", value = (user?.stats?.streak ?: 0).toString(), color = tokens.surface.danger)
                StatChip(icon = "bolt", value = (user?.stats?.coins ?: 0).toString(), color = tokens.surface.warn)
            }
        }

        // Hero — today's plan
        Box(
            modifier = Modifier
                .padding(start = 18.dp, end = 18.dp, top = 16.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = tokens.accent.p2)
                .clip(RoundedCornerShape(20.dp))
                .background(tokens.accent.gradient())
                .fillMaxWidth()
                .padding(18.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    tr(R.string.today_plan).uppercase(),
                    color = Color.White.copy(alpha = 0.85f),
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                )
                Text(
                    "Upper body — Monday",
                    color = Color.White,
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp),
                )
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("65 min", color = Color.White.copy(alpha = 0.95f), style = TextStyle(fontSize = 13.sp))
                    Text("· 6 ex", color = Color.White.copy(alpha = 0.95f), style = TextStyle(fontSize = 13.sp))
                    Text("· 28 sets", color = Color.White.copy(alpha = 0.95f), style = TextStyle(fontSize = 13.sp))
                }
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable(onClick = onStartWorkout)
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppIcon("play", size = 14.dp, tint = Color(0xFF0B1020))
                        Text(
                            tr(R.string.start_workout),
                            color = Color(0xFF0B1020),
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }
        }

        // Streak row
        Column(modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 18.dp)) {
            GymCard(padding = 16.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppIcon("flame", size = 18.dp, tint = tokens.surface.danger)
                        Text(tr(R.string.streak), color = tokens.surface.text, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                        Text(
                            "· ${user?.stats?.streak ?: 0} ${tr(R.string.streak_days)}",
                            color = tokens.surface.textMuted,
                            style = TextStyle(fontSize = 13.sp),
                        )
                    }
                    AppIcon("chevron-right", size = 16.dp, tint = tokens.surface.textDim)
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                    val days = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
                    days.forEachIndexed { i, d ->
                        val filled = i < 4
                        StreakDayCell(day = d, filled = filled, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Quests
        SectionHeader(title = tr(R.string.achievements), action = "${tr(R.string.done)} →")
        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            state.quests.forEach { q ->
                GymCard(padding = 14.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(q.text, color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold), modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(tokens.accent.gradientSoft())
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                        ) {
                            Text(
                                "+${q.xp} XP",
                                color = tokens.accent.p2,
                                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ProgressBar(value = q.progress.toFloat(), max = q.total.toFloat(), height = 6.dp, modifier = Modifier.weight(1f))
                        Text(
                            "${q.progress}/${q.total}",
                            color = tokens.surface.textMuted,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                        )
                    }
                }
            }
        }

        // Stats grid
        SectionHeader(title = tr(R.string.this_week))
        Row(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            StatTile(
                value = (user?.stats?.workoutsThisWeek ?: 0).toString(),
                label = tr(R.string.workout),
                icon = "dumbbell",
                color = tokens.accent.p2,
                modifier = Modifier.weight(1f),
            )
            StatTile(
                value = "4h 20m",
                label = "Time",
                icon = "clock",
                color = tokens.accent.p1,
                modifier = Modifier.weight(1f),
            )
            StatTile(
                value = "12.4t",
                label = "Volume",
                icon = "chart",
                color = tokens.accent.p3,
                modifier = Modifier.weight(1f),
            )
        }

        // Recent routines
        SectionHeader(title = tr(R.string.recent_routines), action = "→")
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(state.routines, key = { it.id }) { r ->
                RoutineCardSmall(r)
            }
            if (state.routines.isEmpty()) {
                item {
                    GymCard(modifier = Modifier.width(220.dp), padding = 14.dp) {
                        Text("No routines yet", color = tokens.surface.textMuted, style = TextStyle(fontSize = 13.sp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatChip(icon: String, value: String, color: Color) {
    val tokens = GymTheme.tokens
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(tokens.surface.surface)
            .border(1.dp, tokens.surface.border, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AppIcon(icon, size = 14.dp, tint = color)
        Text(value, color = tokens.surface.text, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
    }
}

@Composable
private fun StreakDayCell(day: String, filled: Boolean, modifier: Modifier = Modifier) {
    val tokens = GymTheme.tokens
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            day,
            color = if (filled) tokens.accent.p2 else tokens.surface.textDim,
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (filled) tokens.accent.gradient() else androidx.compose.ui.graphics.SolidColor(tokens.surface.chip))
                .let { if (!filled) it.border(1.dp, tokens.surface.border, CircleShape) else it },
            contentAlignment = Alignment.Center,
        ) {
            if (filled) AppIcon("check", size = 16.dp, tint = Color.White)
        }
    }
}

@Composable
private fun RoutineCardSmall(routine: RoutineDto) {
    val tokens = GymTheme.tokens
    val color = runCatching { Color(android.graphics.Color.parseColor(routine.color)) }.getOrDefault(tokens.accent.p2)
    Column(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.13f))
            .border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon("dumbbell", size = 18.dp, tint = Color.White)
        }
        Text(routine.name, color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
        Text(
            "${routine.totalSets} sets · ${routine.estimatedDurationMin} min",
            color = tokens.surface.textMuted,
            style = TextStyle(fontSize = 11.sp),
        )
    }
}
