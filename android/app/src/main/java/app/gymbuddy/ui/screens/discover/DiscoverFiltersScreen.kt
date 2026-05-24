package app.gymbuddy.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gymbuddy.R
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.GradientButton
import app.gymbuddy.ui.components.GymCard
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.Pill
import app.gymbuddy.ui.components.ScreenHeader

/**
 * Discover filters: distance, age range, goal, level, schedule overlap.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiscoverFiltersScreen(onBack: () -> Unit, onApply: () -> Unit) {
    val tokens = GymTheme.tokens
    var maxDistance by remember { mutableStateOf(15f) }
    var ageMax by remember { mutableStateOf(35f) }
    var ageMin by remember { mutableStateOf(21f) }
    var goal by remember { mutableStateOf("All") }
    var level by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .verticalScroll(rememberScrollState()),
    ) {
        ScreenHeader(
            title = tr(R.string.filters),
            large = true,
            left = { IconButton(name = "arrow-left", onClick = onBack) },
            right = {
                Text(
                    "Reset",
                    color = tokens.accent.p2,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                )
            },
        )

        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            // Distance card
            GymCard(padding = 16.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppIcon("location", size = 18.dp, tint = tokens.accent.p2)
                        Text("Distance", color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                    }
                    Text(
                        "${maxDistance.toInt()} km",
                        color = tokens.accent.p2,
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    )
                }
                Slider(
                    value = maxDistance,
                    onValueChange = { maxDistance = it },
                    valueRange = 1f..50f,
                    colors = SliderDefaults.colors(thumbColor = tokens.accent.p2, activeTrackColor = tokens.accent.p2),
                )
            }

            // Age range card
            GymCard(padding = 16.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppIcon("user", size = 18.dp, tint = tokens.accent.p3)
                        Text(tr(R.string.age), color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                    }
                    Text(
                        "${ageMin.toInt()} – ${ageMax.toInt()}",
                        color = tokens.accent.p3,
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    )
                }
                Slider(
                    value = ageMax,
                    onValueChange = { ageMax = it },
                    valueRange = 18f..65f,
                    colors = SliderDefaults.colors(thumbColor = tokens.accent.p3, activeTrackColor = tokens.accent.p3),
                )
            }

            // Goal
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    tr(R.string.goal).uppercase(),
                    color = tokens.surface.textDim,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("All", "Strength", "Hypertrophy", "Mobility", "Calisthenics", "CrossFit", "Cardio").forEach { g ->
                        Pill(text = g, active = goal == g, onClick = { goal = g }, color = tokens.accent.p1)
                    }
                }
            }

            // Level
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    tr(R.string.level).uppercase(),
                    color = tokens.surface.textDim,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("All", "Beginner", "Intermediate", "Advanced", "Elite").forEach { l ->
                        Pill(text = l, active = level == l, onClick = { level = l }, color = tokens.accent.p2)
                    }
                }
            }

            // Schedule
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "SCHEDULE OVERLAP",
                    color = tokens.surface.textDim,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su").forEachIndexed { i, d ->
                        Pill(text = d, active = i < 5, onClick = {}, color = tokens.accent.p2)
                    }
                }
            }

            GradientButton(text = "Apply filters", icon = "check", onClick = onApply)
        }
    }
}
