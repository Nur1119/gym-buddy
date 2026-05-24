package app.gymbuddy.ui.screens.friends

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import app.gymbuddy.ui.components.Avatar
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.ScreenHeader
import app.gymbuddy.viewmodel.ProfileViewModel

@Composable
fun LeaderboardScreen(onBack: () -> Unit, vm: ProfileViewModel = hiltViewModel()) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg),
    ) {
        ScreenHeader(
            title = tr(R.string.leaderboard),
            large = true,
            left = { IconButton(name = "arrow-left", onClick = onBack) },
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.leaderboard) { entry ->
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
                    Text(entry.rank.toString(), color = tokens.surface.textMuted, modifier = Modifier.width(28.dp), style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.ExtraBold))
                    Avatar(name = entry.user.name, size = 36.dp, color1 = tokens.accent.p3, color2 = tokens.accent.p2)
                    Text(entry.user.name, color = tokens.surface.text, modifier = Modifier.weight(1f), style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                    Text(entry.xp.toString(), color = tokens.accent.p2, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                }
            }
            if (state.leaderboard.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                        Text("Leaderboard unavailable", color = tokens.surface.textMuted, style = TextStyle(fontSize = 14.sp))
                    }
                }
            }
        }
    }
}
