package app.gymbuddy.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import app.gymbuddy.ui.components.GymCard
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.ScreenHeader
import app.gymbuddy.viewmodel.CalendarViewModel

/**
 * Monthly calendar. Skeleton; renders a 7-column grid with workout dots.
 * TODO: match prototype layout (month switcher, full upcoming list).
 */
@Composable
fun CalendarScreen(onBack: () -> Unit, vm: CalendarViewModel = hiltViewModel()) {
    val tokens = GymTheme.tokens
    @Suppress("UNUSED_VARIABLE")
    val state by vm.state.collectAsStateWithLifecycle()
    val days = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg),
    ) {
        ScreenHeader(
            title = tr(R.string.calendar),
            large = true,
            left = { IconButton(name = "arrow-left", onClick = onBack) },
            right = { IconButton(name = "plus", onClick = {}, background = tokens.accent.p2, tint = Color.White) },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                "May 2026",
                color = tokens.surface.text,
                style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(name = "chevron-left", onClick = {}, size = 32.dp, iconSize = 16.dp)
                IconButton(name = "chevron-right", onClick = {}, size = 32.dp, iconSize = 16.dp)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 18.dp)) {
            GymCard(padding = 12.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    days.forEach {
                        Text(
                            it,
                            color = tokens.surface.textDim,
                            modifier = Modifier.weight(1f),
                            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    val cells = (1..35).toList()
                    cells.chunked(7).forEach { weekIdxs ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            weekIdxs.forEach { idx ->
                                val day = idx - 3
                                val isToday = day == 24
                                val hasWorkout = day in listOf(18, 20, 22, 23)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (hasWorkout) tokens.accent.gradientSoft()
                                            else SolidColor(Color.Transparent)
                                        )
                                        .border(
                                            width = if (isToday) 2.dp else 1.dp,
                                            color = if (isToday) tokens.accent.p2 else tokens.surface.border,
                                            shape = RoundedCornerShape(8.dp),
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    if (day in 1..31) {
                                        Text(
                                            text = day.toString(),
                                            color = when {
                                                isToday -> tokens.accent.p2
                                                hasWorkout -> tokens.surface.text
                                                else -> tokens.surface.textDim
                                            },
                                            style = TextStyle(
                                                fontSize = 13.sp,
                                                fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.SemiBold,
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
