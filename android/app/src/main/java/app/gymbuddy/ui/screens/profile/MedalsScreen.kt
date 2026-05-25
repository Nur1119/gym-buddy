package app.gymbuddy.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gymbuddy.R
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.ProgressBar
import app.gymbuddy.ui.components.ScreenHeader
import app.gymbuddy.viewmodel.ProfileViewModel

/**
 * Achievements grid: 2 columns of medal cards with locked/unlocked styling.
 */
@Composable
fun MedalsScreen(onBack: () -> Unit, vm: ProfileViewModel = hiltViewModel()) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    val medals = state.medals
    val unlocked = medals.count { it.unlocked }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .statusBarsPadding(),
    ) {
        ScreenHeader(
            title = tr(R.string.medals),
            large = true,
            left = { IconButton(name = "arrow-left", onClick = onBack) },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                "$unlocked",
                color = tokens.surface.text,
                style = TextStyle(fontSize = 38.sp, fontWeight = FontWeight.Black),
            )
            Text(
                "/${medals.size}",
                color = tokens.surface.textDim,
                style = TextStyle(fontSize = 18.sp),
            )
            Column(modifier = Modifier.weight(1f)) {
                ProgressBar(value = unlocked.toFloat(), max = medals.size.coerceAtLeast(1).toFloat(), height = 8.dp)
                Text(
                    "Medals unlocked",
                    color = tokens.surface.textMuted,
                    style = TextStyle(fontSize = 11.sp),
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 18.dp, end = 18.dp, top = 8.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(medals) { medal ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(tokens.surface.surface)
                        .border(1.dp, tokens.surface.border, RoundedCornerShape(18.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(if (medal.unlocked) tokens.accent.gradient() else androidx.compose.ui.graphics.SolidColor(tokens.surface.chip)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(medal.icon.ifBlank { if (medal.unlocked) "🏅" else "🔒" }, style = TextStyle(fontSize = 28.sp))
                    }
                    Text(
                        medal.name,
                        color = if (medal.unlocked) tokens.surface.text else tokens.surface.textDim,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
    }
}
