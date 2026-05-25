package app.gymbuddy.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gymbuddy.R
import app.gymbuddy.data.remote.dto.MatchDto
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.PhotoSlot
import app.gymbuddy.ui.components.ScreenHeader
import app.gymbuddy.viewmodel.MatchesViewModel

/**
 * Matches list screen — horizontal strip of new matches plus a vertical list
 * of conversations with last-message preview and unread badge.
 */
@Composable
fun MatchesListScreen(
    onBack: () -> Unit,
    onOpenChat: (MatchDto) -> Unit,
    vm: MatchesViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .statusBarsPadding(),
    ) {
        ScreenHeader(
            title = tr(R.string.matches),
            large = true,
            left = { IconButton(name = "arrow-left", onClick = onBack) },
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp),
        ) {
            // ── New matches strip
            item {
                Text(
                    text = "NEW MATCHES",
                    color = tokens.surface.textDim,
                    modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 0.dp, bottom = 10.dp),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                )
                LazyRow(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    item { LikesYouTile() }
                    items(state.matches) { match ->
                        MatchStripItem(match = match, onClick = { onOpenChat(match) })
                    }
                }
                Box(modifier = Modifier.height(16.dp))
            }

            // ── Messages list
            item {
                Text(
                    text = "MESSAGES",
                    color = tokens.surface.textDim,
                    modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 10.dp),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                )
            }
            items(state.matches, key = { it.id }) { match ->
                MatchRow(match = match, onClick = { onOpenChat(match) })
            }
            if (state.matches.isEmpty() && !state.loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "No matches yet — swipe right!",
                            color = tokens.surface.textMuted,
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LikesYouTile() {
    val tokens = GymTheme.tokens
    Column(
        modifier = Modifier
            .size(width = 72.dp, height = 96.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(tokens.accent.gradient()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AppIcon("bolt", size = 20.dp, tint = Color.White)
        Text(
            "14",
            color = Color.White,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold),
        )
        Text(
            "LIKES",
            color = Color.White.copy(alpha = 0.9f),
            style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
        )
    }
}

@Composable
private fun MatchStripItem(match: MatchDto, onClick: () -> Unit) {
    val tokens = GymTheme.tokens
    val unread = match.unreadCount
    val seed = match.user.id.hashCode() and 0xFFFF
    val (c1, c2) = stripColors(seed, tokens.accent.p1, tokens.accent.p2, tokens.accent.p3)
    Box(
        modifier = Modifier
            .size(width = 72.dp, height = 96.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .let { if (unread > 0) it.border(2.dp, tokens.accent.p2, RoundedCornerShape(14.dp)) else it },
    ) {
        PhotoSlot(color1 = c1, color2 = c2, modifier = Modifier.fillMaxSize())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)))
                )
                .align(Alignment.BottomCenter)
                .padding(start = 6.dp, end = 6.dp, top = 12.dp, bottom = 6.dp),
            contentAlignment = Alignment.BottomStart,
        ) {
            Text(
                text = match.user.name,
                color = Color.White,
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
            )
        }
        if (unread > 0) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.TopEnd)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(tokens.accent.p2),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = unread.toString(),
                    color = Color.White,
                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

@Composable
private fun MatchRow(match: MatchDto, onClick: () -> Unit) {
    val tokens = GymTheme.tokens
    val unread = match.unreadCount
    val seed = match.user.id.hashCode() and 0xFFFF
    val (c1, c2) = stripColors(seed, tokens.accent.p1, tokens.accent.p2, tokens.accent.p3)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 18.dp, end = 18.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape),
        ) {
            PhotoSlot(color1 = c1, color2 = c2, modifier = Modifier.fillMaxSize())
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = match.user.name,
                    color = tokens.surface.text,
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                )
                Text(
                    text = relativeTime(match.matchedAt),
                    color = tokens.surface.textDim,
                    style = TextStyle(fontSize = 11.sp),
                )
            }
            val preview = match.lastMessage?.text ?: "You matched! Say hi."
            Text(
                text = preview,
                color = if (unread > 0) tokens.surface.text else tokens.surface.textMuted,
                style = TextStyle(fontSize = 13.sp, fontWeight = if (unread > 0) FontWeight.SemiBold else FontWeight.Medium),
                maxLines = 1,
            )
        }
        if (unread > 0) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(tokens.accent.p2),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = unread.toString(),
                    color = Color.White,
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

private fun stripColors(seed: Int, p1: Color, p2: Color, p3: Color): Pair<Color, Color> {
    val palette = listOf(p3 to p2, p2 to p1, p1 to p3)
    return palette[(seed % palette.size + palette.size) % palette.size]
}

/** Lightweight relative-time formatter; trims to first 10 chars. */
private fun relativeTime(iso: String): String = iso.take(10)
