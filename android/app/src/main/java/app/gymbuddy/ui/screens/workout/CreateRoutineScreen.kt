package app.gymbuddy.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gymbuddy.R
import app.gymbuddy.data.remote.dto.ExerciseDto
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.GradientButton
import app.gymbuddy.ui.components.ScreenHeader
import app.gymbuddy.viewmodel.CreateRoutineViewModel
import app.gymbuddy.viewmodel.RoutineExerciseEntry

@Composable
fun CreateRoutineScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    vm: CreateRoutineViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    var routineName by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .statusBarsPadding(),
    ) {
        ScreenHeader(
            title = tr(R.string.create_plan),
            large = true,
            left = {
                Text(
                    tr(R.string.cancel),
                    color = tokens.accent.p2,
                    modifier = Modifier.clickable(onClick = onBack),
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
                )
            },
            right = {
                Text(
                    tr(R.string.save),
                    color = if (state.saving) tokens.surface.textDim else tokens.accent.p2,
                    modifier = Modifier.clickable(enabled = !state.saving) { vm.save(routineName, onSaved) },
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                )
            },
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
        ) {
            // Routine name
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    "ROUTINE NAME",
                    color = tokens.surface.textDim,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(tokens.surface.surface)
                        .border(1.dp, tokens.surface.border, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                ) {
                    if (routineName.isEmpty()) {
                        Text("e.g. Push Day A", color = tokens.surface.textDim, style = TextStyle(fontSize = 15.sp))
                    }
                    BasicTextField(
                        value = routineName,
                        onValueChange = { routineName = it },
                        cursorBrush = SolidColor(tokens.surface.text),
                        textStyle = TextStyle(fontSize = 15.sp, color = tokens.surface.text),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                if (state.error != null) {
                    Text(state.error!!, color = tokens.surface.danger, style = TextStyle(fontSize = 12.sp))
                }
            }

            // Added exercises
            if (state.added.isNotEmpty()) {
                Text(
                    "EXERCISES (${state.added.size})",
                    color = tokens.surface.textDim,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                    modifier = Modifier.padding(start = 18.dp, top = 4.dp, bottom = 6.dp),
                )
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.added.forEach { entry ->
                        AddedExerciseRow(
                            entry = entry,
                            onRemove = { vm.removeExercise(entry.exercise.id) },
                            onSetsChange = { vm.updateSets(entry.exercise.id, it) },
                            onRepsChange = { vm.updateReps(entry.exercise.id, it) },
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            // Search + add exercises
            Text(
                "ADD EXERCISES",
                color = tokens.surface.textDim,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                modifier = Modifier.padding(start = 18.dp, top = 4.dp, bottom = 6.dp),
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(tokens.surface.surface)
                    .border(1.dp, tokens.surface.border, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                if (state.query.isEmpty()) {
                    Text("Search exercises…", color = tokens.surface.textDim, style = TextStyle(fontSize = 14.sp))
                }
                BasicTextField(
                    value = state.query,
                    onValueChange = { vm.setQuery(it) },
                    cursorBrush = SolidColor(tokens.surface.text),
                    textStyle = TextStyle(fontSize = 14.sp, color = tokens.surface.text),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(Modifier.height(8.dp))

            val filtered = state.allExercises.filter { ex ->
                state.query.isBlank() || ex.name.contains(state.query, ignoreCase = true)
            }
            Column(
                modifier = Modifier.padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                filtered.take(20).forEach { ex ->
                    val alreadyAdded = state.added.any { it.exercise.id == ex.id }
                    ExercisePickRow(
                        exercise = ex,
                        added = alreadyAdded,
                        onClick = { if (!alreadyAdded) vm.addExercise(ex) },
                    )
                }
                if (filtered.isEmpty()) {
                    Text("No exercises found", color = tokens.surface.textMuted, style = TextStyle(fontSize = 13.sp))
                }
            }

            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.padding(horizontal = 18.dp)) {
                GradientButton(
                    text = if (state.saving) "Saving…" else "Save routine",
                    icon = "check",
                    onClick = { vm.save(routineName, onSaved) },
                )
            }
        }
    }
}

@Composable
private fun AddedExerciseRow(
    entry: RoutineExerciseEntry,
    onRemove: () -> Unit,
    onSetsChange: (Int) -> Unit,
    onRepsChange: (Int) -> Unit,
) {
    val tokens = GymTheme.tokens
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(tokens.surface.surface)
            .border(1.dp, tokens.accent.p2.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(entry.exercise.name, color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                NumberStepper(
                    label = "Sets",
                    value = entry.sets,
                    onMinus = { onSetsChange(entry.sets - 1) },
                    onPlus = { onSetsChange(entry.sets + 1) },
                )
                NumberStepper(
                    label = "Reps",
                    value = entry.reps,
                    onMinus = { onRepsChange(entry.reps - 1) },
                    onPlus = { onRepsChange(entry.reps + 1) },
                )
            }
        }
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(tokens.surface.danger.copy(alpha = 0.15f))
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon("close", size = 14.dp, tint = tokens.surface.danger)
        }
    }
}

@Composable
private fun NumberStepper(label: String, value: Int, onMinus: () -> Unit, onPlus: () -> Unit) {
    val tokens = GymTheme.tokens
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, color = tokens.surface.textDim, style = TextStyle(fontSize = 11.sp))
        Box(
            modifier = Modifier.size(22.dp).clip(CircleShape).background(tokens.surface.chip).clickable(onClick = onMinus),
            contentAlignment = Alignment.Center,
        ) { Text("−", color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)) }
        Text("$value", color = tokens.surface.text, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
        Box(
            modifier = Modifier.size(22.dp).clip(CircleShape).background(tokens.surface.chip).clickable(onClick = onPlus),
            contentAlignment = Alignment.Center,
        ) { Text("+", color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)) }
    }
}

@Composable
private fun ExercisePickRow(exercise: ExerciseDto, added: Boolean, onClick: () -> Unit) {
    val tokens = GymTheme.tokens
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(tokens.surface.surface)
            .border(1.dp, tokens.surface.border, RoundedCornerShape(12.dp))
            .clickable(enabled = !added, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(exercise.name, color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
            Text(exercise.muscle, color = tokens.surface.textMuted, style = TextStyle(fontSize = 11.sp))
        }
        Spacer(Modifier.width(8.dp))
        if (added) {
            AppIcon("check", size = 18.dp, tint = tokens.surface.success)
        } else {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(tokens.accent.p2),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon("plus", size = 14.dp, tint = Color.White)
            }
        }
    }
}
