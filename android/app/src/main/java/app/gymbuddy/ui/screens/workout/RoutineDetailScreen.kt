package app.gymbuddy.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gymbuddy.R
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.GradientButton
import app.gymbuddy.ui.components.GymCard
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.ScreenHeader

/**
 * Routine detail: shows exercises in the routine and lets you start a workout
 * from it. TODO: match prototype layout for the per-exercise breakdown.
 */
@Composable
fun RoutineDetailScreen(routineId: String, onBack: () -> Unit, onStart: () -> Unit) {
    val tokens = GymTheme.tokens
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg),
    ) {
        ScreenHeader(
            title = "Routine",
            large = true,
            left = { IconButton(name = "arrow-left", onClick = onBack) },
        )
        Column(modifier = Modifier.padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            GymCard(padding = 16.dp) {
                Text("Routine ID: $routineId", color = tokens.surface.text, style = TextStyle(fontSize = 14.sp))
                Text("TODO: match prototype layout (exercise list, edit / duplicate / delete actions)", color = tokens.surface.textMuted, style = TextStyle(fontSize = 12.sp), modifier = Modifier.padding(top = 8.dp))
            }
            GradientButton(text = tr(R.string.start_workout), icon = "play", onClick = onStart)
        }
    }
}
