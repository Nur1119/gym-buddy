package app.gymbuddy.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.Pill
import app.gymbuddy.ui.components.ScreenHeader
import app.gymbuddy.viewmodel.ExercisesViewModel

@Composable
fun ExercisesListScreen(
    onBack: () -> Unit,
    onCreate: () -> Unit,
    vm: ExercisesViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()

    val muscleGroups = listOf(
        "all" to "All", "Chest" to "Chest", "Back" to "Back", "Legs" to "Legs",
        "Shoulders" to "Shoulders", "Arms" to "Arms", "Core" to "Core", "Cardio" to "Cardio",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg),
    ) {
        ScreenHeader(
            title = tr(R.string.exercises),
            large = true,
            left = { IconButton(name = "arrow-left", onClick = onBack) },
            right = { IconButton(name = "plus", onClick = onCreate, background = tokens.accent.p2, tint = Color.White) },
        )

        // Search
        Box(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(tokens.surface.surface)
                .border(1.dp, tokens.surface.border, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AppIcon("search", size = 18.dp, tint = tokens.surface.textDim)
                Box(modifier = Modifier.weight(1f)) {
                    if (state.query.isEmpty()) {
                        Text(tr(R.string.search), color = tokens.surface.textDim, style = TextStyle(fontSize = 15.sp))
                    }
                    BasicTextField(
                        value = state.query,
                        onValueChange = vm::setQuery,
                        singleLine = true,
                        cursorBrush = SolidColor(tokens.surface.text),
                        textStyle = TextStyle(fontSize = 15.sp, color = tokens.surface.text),
                    )
                }
            }
        }

        // Filter pills
        LazyRow(
            modifier = Modifier.padding(vertical = 12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(muscleGroups) { (key, label) ->
                Pill(text = label, active = state.muscle == key, onClick = { vm.setMuscle(key) })
            }
        }

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.items, key = { it.id }) { ex ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(tokens.surface.surface)
                        .border(1.dp, tokens.surface.border, RoundedCornerShape(14.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(tokens.surface.chip),
                        contentAlignment = Alignment.Center,
                    ) { AppIcon("dumbbell", size = 22.dp, tint = tokens.surface.text) }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(ex.name, color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Text("${ex.muscle} · ${ex.equipment}", color = tokens.surface.textMuted, style = TextStyle(fontSize = 11.sp))
                    }
                    AppIcon("chevron-right", size = 16.dp, tint = tokens.surface.textDim)
                }
            }
            if (state.items.isEmpty() && !state.loading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(30.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("No exercises found", color = tokens.surface.textMuted, style = TextStyle(fontSize = 14.sp))
                    }
                }
            }
        }
    }
}
