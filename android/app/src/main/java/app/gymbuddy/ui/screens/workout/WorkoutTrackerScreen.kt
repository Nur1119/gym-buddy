package app.gymbuddy.ui.screens.workout

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import app.gymbuddy.data.remote.dto.RoutineDto
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.Avatar
import app.gymbuddy.ui.components.GymCard
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.SectionHeader
import app.gymbuddy.viewmodel.WorkoutTrackerViewModel

/**
 * Workout home screen.
 *
 * Polished: stats strip, Tracker/My-Plan tab pills, "Create plan" placeholder,
 * "Start empty workout" + "AI workout" action cards, list of routines.
 */
@Composable
fun WorkoutTrackerScreen(
    onStartActive: () -> Unit,
    onOpenExercises: () -> Unit,
    onOpenRoutine: (RoutineDto) -> Unit,
    vm: WorkoutTrackerViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    var tab by remember { mutableStateOf("tracker") }
    val statusInsets = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .verticalScroll(rememberScrollState())
            .padding(top = statusInsets, bottom = 24.dp),
    ) {
        // Hero strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Avatar(name = "G", size = 40.dp, color1 = tokens.accent.p3, color2 = tokens.accent.p2)
                Column {
                    Text("Lv. 7", color = tokens.surface.textDim, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(tokens.surface.chip),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.74f)
                                .background(tokens.accent.gradient())
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(name = "plus", onClick = { /* TODO add */ }, background = tokens.accent.p2, tint = Color.White, size = 32.dp, iconSize = 18.dp)
            }
        }

        // Tabs
        Spacer(Modifier.height(16.dp))
        TabPills(
            tabs = listOf("tracker" to tr(R.string.tracker), "plan" to tr(R.string.my_plan)),
            active = tab,
            onSelect = { tab = it },
            modifier = Modifier.padding(horizontal = 18.dp),
        )

        // Planned workout
        SectionHeader(title = tr(R.string.planned_workout))
        Column(modifier = Modifier.padding(horizontal = 18.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(tokens.surface.surface)
                    .border(1.dp, tokens.surface.borderStrong, RoundedCornerShape(16.dp))
                    .clickable { /* TODO open create plan */ }
                    .padding(18.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(tokens.accent.gradientSoft()),
                            contentAlignment = Alignment.Center,
                        ) { AppIcon("edit", size = 20.dp, tint = tokens.accent.p2) }
                        Text(
                            tr(R.string.create_plan),
                            color = tokens.surface.text,
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        )
                    }
                    AppIcon("chevron-right", size = 18.dp, tint = tokens.surface.textDim)
                }
            }
        }

        // New workout
        SectionHeader(title = "New workout")
        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ActionCard(title = tr(R.string.start_empty_workout), icon = "dumbbell", color = tokens.accent.p2, onClick = onStartActive)
            ActionCard(title = tr(R.string.generate_workout), icon = "spark", color = tokens.accent.p1, onClick = { /* TODO AI */ })
        }

        // Routines
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(tr(R.string.routines), color = tokens.surface.text, style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(name = "list", onClick = onOpenExercises, size = 32.dp, iconSize = 16.dp)
                IconButton(name = "plus", onClick = { /* TODO add */ }, size = 32.dp, iconSize = 16.dp)
            }
        }

        Text(
            "${tr(R.string.my_routines)} (${state.routines.size})",
            color = tokens.surface.textMuted,
            modifier = Modifier.padding(horizontal = 18.dp),
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
        )
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            state.routines.forEach { r ->
                RoutineRow(routine = r, onClick = { onOpenRoutine(r) })
            }
            if (state.routines.isEmpty()) {
                GymCard(padding = 16.dp) {
                    Text("No routines yet. Create one!", color = tokens.surface.textMuted, style = TextStyle(fontSize = 14.sp))
                }
            }
        }
    }
}

@Composable
internal fun TabPills(
    tabs: List<Pair<String, String>>,
    active: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = GymTheme.tokens
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(tokens.surface.surface2)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        tabs.forEach { (key, label) ->
            val isActive = key == active
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isActive) tokens.surface.surface else Color.Transparent)
                    .clickable { onSelect(key) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label,
                    color = if (isActive) tokens.surface.text else tokens.surface.textMuted,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

@Composable
private fun ActionCard(title: String, icon: String, color: Color, onClick: () -> Unit) {
    val tokens = GymTheme.tokens
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(tokens.surface.surface)
            .border(1.dp, tokens.surface.border, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(title, color = tokens.surface.text, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(icon, size = 22.dp, tint = Color.White)
        }
    }
}

@Composable
private fun RoutineRow(routine: RoutineDto, onClick: () -> Unit) {
    val tokens = GymTheme.tokens
    val color = runCatching { Color(android.graphics.Color.parseColor(routine.color)) }.getOrDefault(tokens.accent.p2)
    GymCard(padding = 14.dp, onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(color),
                )
                Column {
                    Text(routine.name, color = tokens.surface.text, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                    Text(
                        "${routine.totalSets} sets · ${routine.estimatedDurationMin} min",
                        color = tokens.surface.textMuted,
                        style = TextStyle(fontSize = 11.sp),
                    )
                }
            }
            IconButton(name = "more", onClick = {}, size = 28.dp, iconSize = 16.dp)
        }
    }
}
