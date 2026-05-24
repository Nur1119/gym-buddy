package app.gymbuddy.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import app.gymbuddy.ui.components.PhotoSlot
import app.gymbuddy.ui.components.ScreenHeader

/**
 * Screen for inviting a match to a workout: shows the peer card, date/time/gym,
 * and a list of shared routines.
 */
@Composable
fun InviteWorkoutScreen(
    matchId: String,
    peerName: String,
    onBack: () -> Unit,
    onSend: () -> Unit,
) {
    val tokens = GymTheme.tokens
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .verticalScroll(rememberScrollState()),
    ) {
        ScreenHeader(
            title = tr(R.string.invite_to_gym),
            large = true,
            left = {
                Text(
                    tr(R.string.cancel),
                    color = tokens.accent.p2,
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.clickable(onClick = onBack),
                )
            },
            right = {
                Text(
                    "Send",
                    color = tokens.accent.p2,
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.clickable(onClick = onSend),
                )
            },
        )

        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // To
            GymCard(padding = 14.dp) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(modifier = Modifier.size(44.dp).clip(CircleShape)) {
                        PhotoSlot(color1 = tokens.accent.p3, color2 = tokens.accent.p2, modifier = Modifier.fillMaxSize())
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "To",
                            color = tokens.surface.textDim,
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                        )
                        Text(
                            peerName,
                            color = tokens.surface.text,
                            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }

            // Date + time
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GymCard(modifier = Modifier.weight(1f), padding = 14.dp) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AppIcon("calendar", size = 14.dp, tint = tokens.accent.p2)
                        Text("DATE", color = tokens.surface.textDim, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold))
                    }
                    Text("Tomorrow", color = tokens.surface.text, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                    Text("Tue, May 26", color = tokens.surface.textMuted, style = TextStyle(fontSize = 11.sp))
                }
                GymCard(modifier = Modifier.weight(1f), padding = 14.dp) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AppIcon("clock", size = 14.dp, tint = tokens.accent.p1)
                        Text("TIME", color = tokens.surface.textDim, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold))
                    }
                    Text("07:00", color = tokens.surface.text, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                    Text("1h 30m", color = tokens.surface.textMuted, style = TextStyle(fontSize = 11.sp))
                }
            }

            // Gym
            GymCard(padding = 14.dp) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AppIcon("location", size = 14.dp, tint = tokens.accent.p3)
                    Text("GYM", color = tokens.surface.textDim, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold))
                }
                Text("Iron Gym", color = tokens.surface.text, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                Text("2 km · 4.8 stars", color = tokens.surface.textMuted, style = TextStyle(fontSize = 11.sp))
            }

            // Routine — TODO: match prototype layout (full picker)
            GymCard(padding = 14.dp) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AppIcon("dumbbell", size = 14.dp, tint = tokens.accent.p2)
                    Text(
                        tr(R.string.shared_workout).uppercase(),
                        color = tokens.surface.textDim,
                        style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                    )
                }
                Text("Upper Body", color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold), modifier = Modifier.padding(top = 10.dp))
                Text("6 ex · 28 sets · ~65 min", color = tokens.surface.textMuted, style = TextStyle(fontSize = 11.sp))
            }

            GradientButton(text = "Send invite", icon = "send", onClick = onSend)
        }
    }
}
