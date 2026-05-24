package app.gymbuddy.ui.screens.profile

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
import androidx.compose.foundation.rememberScrollState
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
import app.gymbuddy.ui.components.GymCard
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.ProgressBar
import app.gymbuddy.ui.components.ScreenHeader
import app.gymbuddy.ui.components.SectionHeader
import app.gymbuddy.viewmodel.NutritionViewModel

/**
 * Daily nutrition: calorie ring, macros card row, meals list.
 * Polished layout; will display backend data when available, falls back to
 * static placeholder otherwise.
 */
@Composable
fun NutritionScreen(onBack: () -> Unit, vm: NutritionViewModel = hiltViewModel()) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    val day = state.day

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .verticalScroll(rememberScrollState()),
    ) {
        ScreenHeader(
            title = tr(R.string.nutrition),
            large = true,
            left = { IconButton(name = "arrow-left", onClick = onBack) },
            right = { IconButton(name = "plus", onClick = {}, background = tokens.accent.p2, tint = Color.White) },
        )

        // Calories card
        Column(modifier = Modifier.padding(horizontal = 18.dp)) {
            GymCard(padding = 20.dp) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(tokens.accent.gradient()),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text((day?.totalKcal ?: 1820).toString(), color = Color.White, style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold))
                            Text("KCAL", color = Color.White.copy(alpha = 0.85f), style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(tr(R.string.calories).uppercase(), color = tokens.surface.textDim, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp))
                        Text("${day?.totalKcal ?: 1820} / 2400", color = tokens.surface.text, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.ExtraBold))
                        Text("580 remaining", color = tokens.surface.textMuted, style = TextStyle(fontSize = 12.sp))
                    }
                }
            }
        }

        // Macros
        Row(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MacroTile(tr(R.string.protein), (day?.protein ?: 142.0).toInt(), 180, tokens.surface.danger, modifier = Modifier.weight(1f))
            MacroTile(tr(R.string.carbs), (day?.carbs ?: 220.0).toInt(), 280, tokens.surface.warn, modifier = Modifier.weight(1f))
            MacroTile(tr(R.string.fats), (day?.fats ?: 58.0).toInt(), 72, tokens.accent.p2, modifier = Modifier.weight(1f))
        }

        // Meals (placeholder if no backend day)
        SectionHeader(title = "Meals")
        Column(modifier = Modifier.padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            val meals = day?.meals?.map { it.kind to it.items.sumOf { i -> i.kcal } }
                ?: listOf("breakfast" to 520, "lunch" to 680, "snack" to 220, "dinner" to 0)
            meals.forEach { (kind, kcal) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(tokens.surface.surface)
                        .border(1.dp, tokens.surface.border, RoundedCornerShape(14.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(tokens.surface.chip), contentAlignment = Alignment.Center) {
                        Text(emojiFor(kind), style = TextStyle(fontSize = 22.sp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(kind.replaceFirstChar(Char::titlecase), color = tokens.surface.text, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                        Text("$kcal kcal", color = tokens.surface.textMuted, style = TextStyle(fontSize = 12.sp))
                    }
                }
            }
        }
    }
}

@Composable
private fun MacroTile(label: String, value: Int, max: Int, color: Color, modifier: Modifier = Modifier) {
    val tokens = GymTheme.tokens
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(tokens.surface.surface)
            .border(1.dp, tokens.surface.border, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(label.uppercase(), color = tokens.surface.textDim, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp))
        Text("${value}g", color = tokens.surface.text, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold))
        ProgressBar(value = value.toFloat(), max = max.toFloat(), height = 4.dp, color = color)
        Text("/${max}g", color = tokens.surface.textDim, style = TextStyle(fontSize = 10.sp))
    }
}

private fun emojiFor(kind: String): String = when (kind.lowercase()) {
    "breakfast" -> "🥣"
    "lunch" -> "🥗"
    "snack" -> "🍎"
    "dinner" -> "🍽"
    else -> "🍴"
}
