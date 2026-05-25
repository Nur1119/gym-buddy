package app.gymbuddy.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.text.KeyboardOptions
import app.gymbuddy.R
import app.gymbuddy.data.remote.dto.MessageDto
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.PhotoSlot
import app.gymbuddy.viewmodel.ChatViewModel

/**
 * Real-time chat for a single match. Connects the [ChatViewModel] to the
 * [WebSocketClient] stream via `bind(matchId)`.
 */
@Composable
fun ChatScreen(
    matchId: String,
    peerName: String,
    onBack: () -> Unit,
    onInvite: () -> Unit,
    vm: ChatViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    LaunchedEffect(matchId) { vm.bind(matchId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .imePadding(),
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(tokens.surface.surface)
                .border(1.dp, tokens.surface.border, RoundedCornerShape(0.dp))
                .statusBarsPadding()
                .padding(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            IconButton(name = "arrow-left", onClick = onBack)
            Box(modifier = Modifier.size(36.dp).clip(CircleShape)) {
                PhotoSlot(color1 = tokens.accent.p3, color2 = tokens.accent.p2, modifier = Modifier.fillMaxSize())
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(peerName, color = tokens.surface.text, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                Text(tr(R.string.online), color = tokens.surface.success, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold))
            }
            IconButton(name = "dumbbell", onClick = onInvite, tint = tokens.accent.p2)
            IconButton(name = "more", onClick = {})
        }

        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text(
                    text = "${tr(R.string.matched_on)} TODAY",
                    color = tokens.surface.textDim,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                    ),
                )
            }
            items(state.messages, key = { it.id }) { msg ->
                MessageBubble(msg)
            }
        }

        // Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(tokens.surface.surface)
                .border(1.dp, tokens.surface.border, RoundedCornerShape(0.dp))
                .navigationBarsPadding()
                .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconButton(name = "image", onClick = {})
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(22.dp))
                    .background(tokens.surface.bg)
                    .border(1.dp, tokens.surface.border, RoundedCornerShape(22.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                if (state.draft.isEmpty()) {
                    Text(
                        text = tr(R.string.type_message),
                        color = tokens.surface.textDim,
                        style = TextStyle(fontSize = 14.sp),
                    )
                }
                BasicTextField(
                    value = state.draft,
                    onValueChange = vm::setDraft,
                    singleLine = true,
                    cursorBrush = SolidColor(tokens.surface.text),
                    textStyle = TextStyle(fontSize = 14.sp, color = tokens.surface.text),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Send,
                    ),
                )
            }
            IconButton(
                name = "send",
                onClick = vm::send,
                background = tokens.accent.p2,
                tint = Color.White,
            )
        }
    }
}

@Composable
private fun MessageBubble(msg: MessageDto) {
    val tokens = GymTheme.tokens
    // Convention: if senderId is empty or matches a known "me" id (resolved server-side via /auth/me),
    // we'd flip alignment. Here we use a simple heuristic: empty/null senderId → mine.
    val mine = msg.senderId.isBlank()
    val bubbleBg: Brush =
        if (mine) tokens.accent.gradient() else SolidColor(tokens.surface.surface)
    val textColor = if (mine) Color.White else tokens.surface.text
    val shape = RoundedCornerShape(
        topStart = 18.dp,
        topEnd = 18.dp,
        bottomEnd = if (mine) 4.dp else 18.dp,
        bottomStart = if (mine) 18.dp else 4.dp,
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (mine) Arrangement.End else Arrangement.Start,
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(bubbleBg)
                .let { if (!mine) it.border(1.dp, tokens.surface.border, shape) else it }
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Text(
                text = msg.text ?: "",
                color = textColor,
                style = TextStyle(fontSize = 14.sp),
            )
        }
    }
}
