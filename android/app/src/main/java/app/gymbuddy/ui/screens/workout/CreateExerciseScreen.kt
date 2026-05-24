package app.gymbuddy.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gymbuddy.R
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.GradientButton
import app.gymbuddy.ui.components.Pill
import app.gymbuddy.ui.components.ScreenHeader
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateExerciseScreen(onBack: () -> Unit, onSave: () -> Unit) {
    val tokens = GymTheme.tokens
    var name by remember { mutableStateOf("") }
    var muscle by remember { mutableStateOf("Chest") }
    var equipment by remember { mutableStateOf("Barbell") }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .verticalScroll(rememberScrollState()),
    ) {
        ScreenHeader(
            title = tr(R.string.create_custom),
            large = true,
            left = {
                Text(tr(R.string.cancel), color = tokens.accent.p2, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold), modifier = Modifier.clickable(onClick = onBack))
            },
            right = {
                Text(tr(R.string.save), color = tokens.accent.p2, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold), modifier = Modifier.clickable(onClick = onSave))
            },
        )

        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Image picker
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, tokens.surface.borderStrong, RoundedCornerShape(16.dp))
                    .background(tokens.surface.surface)
                    .clickable { /* TODO image picker */ },
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    AppIcon("camera", size = 28.dp, tint = tokens.surface.textDim)
                    Text("Add photo", color = tokens.surface.textMuted, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold))
                }
            }

            FieldLabel(tr(R.string.name))
            Input(value = name, onValueChange = { name = it }, placeholder = "Close-grip bench")

            FieldLabel("MUSCLE GROUP")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Chest", "Back", "Legs", "Shoulders", "Arms", "Core", "Cardio").forEach { m ->
                    Pill(text = m, active = muscle == m, onClick = { muscle = m })
                }
            }

            FieldLabel("EQUIPMENT")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Barbell", "Dumbbell", "Machine", "Cable", "Bodyweight").forEach { e ->
                    Pill(text = e, active = equipment == e, onClick = { equipment = e }, color = tokens.accent.p1)
                }
            }

            FieldLabel("NOTES")
            Input(value = notes, onValueChange = { notes = it }, placeholder = "Form cues, tips…", minLines = 4)

            GradientButton(text = "Create exercise", icon = "check", onClick = onSave)
        }
    }
}

@Composable
private fun FieldLabel(label: String) {
    val tokens = GymTheme.tokens
    Text(
        text = label,
        color = tokens.surface.textDim,
        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
        modifier = Modifier.padding(start = 4.dp),
    )
}

@Composable
private fun Input(value: String, onValueChange: (String) -> Unit, placeholder: String, minLines: Int = 1) {
    val tokens = GymTheme.tokens
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(tokens.surface.surface)
            .border(1.dp, tokens.surface.border, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        if (value.isEmpty()) {
            Text(placeholder, color = tokens.surface.textDim, style = TextStyle(fontSize = 15.sp))
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            cursorBrush = SolidColor(tokens.surface.text),
            textStyle = TextStyle(fontSize = 15.sp, color = tokens.surface.text),
            minLines = minLines,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
