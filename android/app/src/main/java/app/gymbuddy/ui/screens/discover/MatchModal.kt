package app.gymbuddy.ui.screens.discover

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.gymbuddy.R
import app.gymbuddy.data.remote.dto.UserDto
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.GradientButton
import app.gymbuddy.ui.components.PhotoSlot

/**
 * Full-screen "It's a match!" modal with the gradient title, two tilted photos,
 * "Send a message" CTA, and "Keep swiping" secondary action.
 */
@Composable
fun MatchModal(user: UserDto, onClose: () -> Unit, onChat: () -> Unit) {
    val tokens = GymTheme.tokens
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = true),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f)),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = tr(R.string.its_a_match),
                    style = TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp,
                        brush = tokens.accent.gradient(),
                    ),
                )
                Text(
                    text = "You and ${user.name} liked each other",
                    color = Color.White.copy(alpha = 0.75f),
                    style = TextStyle(fontSize = 14.sp),
                )

                Spacer(Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box(
                        modifier = Modifier
                            .size(width = 110.dp, height = 140.dp)
                            .rotate(-8f)
                            .border(3.dp, Color.White, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp)),
                    ) {
                        PhotoSlot(color1 = tokens.accent.p3, color2 = tokens.accent.p2, modifier = Modifier.fillMaxSize())
                    }
                    Box(
                        modifier = Modifier
                            .size(width = 110.dp, height = 140.dp)
                            .rotate(8f)
                            .border(3.dp, Color.White, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp)),
                    ) {
                        PhotoSlot(color1 = tokens.accent.p2, color2 = tokens.accent.p1, modifier = Modifier.fillMaxSize())
                    }
                }

                Spacer(Modifier.height(32.dp))

                Column(
                    modifier = Modifier.widthIn(max = 280.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    GradientButton(text = tr(R.string.send_message), onClick = onChat, icon = "send")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .clickable(onClick = onClose)
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "Keep swiping",
                            color = Color.White,
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }
        }
    }
}
