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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gymbuddy.R
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.GymCard
import app.gymbuddy.ui.components.IconButton
import kotlinx.coroutines.delay

/**
 * In-progress workout tracker.
 *
 * Polished: header (back + monospaced elapsed timer + Finish), rest banner when
 * a set is checked off (90s countdown), per-exercise set tables with check
 * buttons that flip the row to the gradient-soft "done" style.
 */
@Composable
fun ActiveWorkoutScreen(onBack: () -> Unit) {
    val tokens = GymTheme.tokens
    var elapsed by remember { mutableIntStateOf(0) }
    var resting by remember { mutableStateOf(false) }
    var restRemaining by remember { mutableIntStateOf(0) }
    val statusInsets = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    // Elapsed timer
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            elapsed += 1
        }
    }
    // Rest timer
    LaunchedEffect(resting) {
        while (resting && restRemaining > 0) {
            delay(1000)
            restRemaining -= 1
        }
        if (restRemaining <= 0) resting = false
    }

    // Local exercise + set model
    val exercises = remember {
        mutableStateOf(
            listOf(
                ActiveExercise(name = "Bench Press", muscle = "Chest", icon = "dumbbell", sets = mutableListOf(
                    ActiveSet(50.0, 10, true), ActiveSet(55.0, 8, true), ActiveSet(55.0, 8, false),
                )),
                ActiveExercise(name = "Pull-ups", muscle = "Back", icon = "dumbbell", sets = mutableListOf(
                    ActiveSet(0.0, 10, true), ActiveSet(0.0, 9, false), ActiveSet(0.0, 8, false),
                )),
                ActiveExercise(name = "Cable Row", muscle = "Back", icon = "dumbbell", sets = mutableListOf(
                    ActiveSet(25.0, 12, false), ActiveSet(25.0, 12, false), ActiveSet(25.0, 12, false),
                )),
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .verticalScroll(rememberScrollState())
            .padding(top = statusInsets, bottom = 80.dp),
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(tokens.surface.chip)
                    .clickable(onClick = onBack)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Text(
                    tr(R.string.back),
                    color = tokens.surface.text,
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                AppIcon("clock", size = 18.dp, tint = tokens.accent.p1)
                Text(
                    formatTime(elapsed),
                    color = tokens.accent.p1,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace),
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(tokens.surface.success)
                    .clickable(onClick = onBack)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Text(
                    tr(R.string.finish_workout),
                    color = Color.White,
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
                )
            }
        }

        // Rest banner
        if (resting && restRemaining > 0) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(tokens.accent.gradient())
                    .padding(14.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            tr(R.string.rest_timer).uppercase(),
                            color = Color.White.copy(alpha = 0.85f),
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        )
                        Text(
                            formatTime(restRemaining),
                            color = Color.White,
                            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .clickable { resting = false; restRemaining = 0 }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                    ) {
                        Text("Skip", color = Color.White, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // Exercise blocks
        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            exercises.value.forEachIndexed { ei, ex ->
                GymCard(padding = 14.dp) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(tokens.surface.chip),
                            contentAlignment = Alignment.Center,
                        ) { AppIcon(ex.icon, size = 18.dp, tint = tokens.surface.text) }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(ex.name, color = tokens.surface.text, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                            Text(ex.muscle, color = tokens.surface.textMuted, style = TextStyle(fontSize = 11.sp))
                        }
                        IconButton(name = "more", onClick = {}, size = 28.dp, iconSize = 14.dp)
                    }
                    Spacer(Modifier.height(12.dp))

                    // Header row
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        Text(tr(R.string.set).uppercase(), color = tokens.surface.textDim, modifier = Modifier.width(36.dp), style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp))
                        Text(tr(R.string.weight_unit), color = tokens.surface.textDim, modifier = Modifier.weight(1f), style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp))
                        Text(tr(R.string.reps), color = tokens.surface.textDim, modifier = Modifier.weight(1f), style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp))
                        Spacer(Modifier.width(30.dp))
                    }
                    Spacer(Modifier.height(6.dp))

                    ex.sets.forEachIndexed { si, set ->
                        SetRow(
                            index = si + 1,
                            set = set,
                            onToggle = {
                                val newSets = ex.sets.toMutableList()
                                newSets[si] = set.copy(completed = !set.completed)
                                val newList = exercises.value.toMutableList()
                                newList[ei] = ex.copy(sets = newSets)
                                exercises.value = newList
                                if (newSets[si].completed) {
                                    resting = true
                                    restRemaining = 90
                                }
                            },
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, tokens.surface.borderStrong, RoundedCornerShape(8.dp))
                            .clickable { /* TODO add set */ }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("+ Set", color = tokens.surface.textMuted, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold))
                    }
                }
            }

            // Add exercise
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, tokens.surface.borderStrong, RoundedCornerShape(14.dp))
                    .clickable { /* TODO open exercise picker */ }
                    .padding(14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AppIcon("plus", size = 16.dp, tint = tokens.accent.p2)
                    Text(
                        tr(R.string.add_exercise),
                        color = tokens.accent.p2,
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
    }
}

@Composable
private fun SetRow(index: Int, set: ActiveSet, onToggle: () -> Unit) {
    val tokens = GymTheme.tokens
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (set.completed) tokens.accent.gradientSoft() else androidx.compose.ui.graphics.SolidColor(Color.Transparent))
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = index.toString(),
            color = if (set.completed) tokens.accent.p2 else tokens.surface.text,
            modifier = Modifier.width(36.dp),
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
        )
        Text(
            text = if (set.weight > 0) "${set.weight.toInt()} kg" else "—",
            color = tokens.surface.text,
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = set.reps.toString(),
            color = tokens.surface.text,
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (set.completed) tokens.accent.p1 else tokens.surface.chip)
                .clickable(onClick = onToggle),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon("check", size = 14.dp, tint = if (set.completed) Color.White else tokens.surface.textDim)
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
}

private data class ActiveSet(val weight: Double, val reps: Int, val completed: Boolean)
private data class ActiveExercise(
    val name: String,
    val muscle: String,
    val icon: String,
    val sets: List<ActiveSet>,
)
