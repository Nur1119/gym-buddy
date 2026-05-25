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
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun HomeScreen(
    onStartWorkout: () -> Unit,
    onOpenProfile: () -> Unit = {},
    onOpenMedals: () -> Unit = {},
    onGoWorkout: () -> Unit = {},
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.clickable(onClick = onOpenProfile),
            ) {
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

        // Hero — today's plan (data-driven)
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = tokens.accent.p2)
                .clip(RoundedCornerShape(20.dp))
                .background(tokens.accent.gradient())
                .fillMaxWidth()
                .padding(18.dp),
        ) {
            val firstRoutine = state.routines.firstOrNull()
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    tr(R.string.today_plan).uppercase(),
                    color = Color.White.copy(alpha = 0.85f),
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                )
                if (firstRoutine != null) {
                    Text(
                        firstRoutine.name,
                        color = Color.White,
                        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp),
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("${firstRoutine.estimatedDurationMin} min", color = Color.White.copy(alpha = 0.95f), style = TextStyle(fontSize = 13.sp))
                        Text("· ${firstRoutine.exercises.size} ex", color = Color.White.copy(alpha = 0.95f), style = TextStyle(fontSize = 13.sp))
                        Text("· ${firstRoutine.totalSets} sets", color = Color.White.copy(alpha = 0.95f), style = TextStyle(fontSize = 13.sp))
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
                            Text(tr(R.string.start_workout), color = Color(0xFF0B1020), style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                } else {
                    Text(
                        tr(R.string.no_workout_today),
                        color = Color.White,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable(onClick = onGoWorkout)
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                    ) {
                        Text(tr(R.string.create_plan), color = Color(0xFF0B1020), style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
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
                    // Mark days based on streak count, going backwards from today
                    val streak = (user?.stats?.streak ?: 0).coerceIn(0, 7)
                    val todayDow = LocalDate.now().dayOfWeek
                    val todayIdx = when (todayDow) {
                        DayOfWeek.MONDAY -> 0; DayOfWeek.TUESDAY -> 1; DayOfWeek.WEDNESDAY -> 2
                        DayOfWeek.THURSDAY -> 3; DayOfWeek.FRIDAY -> 4; DayOfWeek.SATURDAY -> 5
                        DayOfWeek.SUNDAY -> 6; else -> 0
                    }
                    days.forEachIndexed { i, d ->
                        val filled = streak > 0 && i <= todayIdx && i > todayIdx - streak
                        StreakDayCell(day = d, filled = filled, isToday = i == todayIdx, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Quests / Achievements
        SectionHeader(title = tr(R.string.achievements), action = "${tr(R.string.done)} →", onAction = onOpenMedals)
        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (state.quests.isEmpty()) {
                GymCard(padding = 14.dp) {
                    Text(
                        "Complete workouts to earn achievements",
                        color = tokens.surface.textMuted,
                        style = TextStyle(fontSize = 13.sp),
                    )
                }
            }
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
                            Text("+${q.xp} XP", color = tokens.accent.p2, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ProgressBar(value = q.progress.toFloat(), max = q.total.toFloat(), height = 6.dp, modifier = Modifier.weight(1f))
                        Text("${q.progress}/${q.total}", color = tokens.surface.textMuted, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold))
                    }
                }
            }
        }

        // Stats grid — real data
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
            val vol = state.stats.weeklyVolume
            val volLabel = when {
                vol >= 1000 -> "${"%.1f".format(vol / 1000)}t"
                vol > 0 -> "${vol.toInt()} kg"
                else -> "0 kg"
            }
            StatTile(
                value = volLabel,
                label = "Volume",
                icon = "chart",
                color = tokens.accent.p1,
                modifier = Modifier.weight(1f),
            )
            StatTile(
                value = "${user?.stats?.streak ?: 0}d",
                label = tr(R.string.streak),
                icon = "flame",
                color = tokens.surface.danger,
                modifier = Modifier.weight(1f),
            )
        }

        // Recent routines
        SectionHeader(
            title = tr(R.string.recent_routines),
            action = if (state.routines.isNotEmpty()) "→" else null,
            onAction = onGoWorkout,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(state.routines, key = { it.id }) { r ->
                RoutineCardSmall(r, onClick = onGoWorkout)
            }
            if (state.routines.isEmpty()) {
                item {
                    GymCard(modifier = Modifier.width(220.dp), padding = 14.dp, onClick = onGoWorkout) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            AppIcon("plus", size = 18.dp, tint = tokens.accent.p2)
                            Text("Create your first routine", color = tokens.surface.textMuted, style = TextStyle(fontSize = 13.sp))
                        }
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
private fun StreakDayCell(day: String, filled: Boolean, isToday: Boolean = false, modifier: Modifier = Modifier) {
    val tokens = GymTheme.tokens
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            day,
            color = when {
                filled -> tokens.accent.p2
                isToday -> tokens.surface.text
                else -> tokens.surface.textDim
            },
            style = TextStyle(fontSize = 10.sp, fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.Bold, letterSpacing = 0.5.sp),
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    if (filled) tokens.accent.gradient()
                    else androidx.compose.ui.graphics.SolidColor(tokens.surface.chip)
                )
                .let {
                    if (!filled && isToday) it.border(2.dp, tokens.accent.p2, CircleShape)
                    else if (!filled) it.border(1.dp, tokens.surface.border, CircleShape)
                    else it
                },
            contentAlignment = Alignment.Center,
        ) {
            if (filled) AppIcon("check", size = 16.dp, tint = Color.White)
        }
    }
}

@Composable
private fun RoutineCardSmall(routine: RoutineDto, onClick: () -> Unit = {}) {
    val tokens = GymTheme.tokens
    val color = runCatching { Color(android.graphics.Color.parseColor(routine.color)) }.getOrDefault(tokens.accent.p2)
    Column(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.13f))
            .border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
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
