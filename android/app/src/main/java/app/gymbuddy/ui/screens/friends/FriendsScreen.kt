package app.gymbuddy.ui.screens.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gymbuddy.R
import app.gymbuddy.data.remote.dto.FriendDto
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.Avatar
import app.gymbuddy.ui.components.GradientButton
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.ScreenHeader
import app.gymbuddy.ui.screens.workout.TabPills
import app.gymbuddy.viewmodel.ProfileViewModel

/**
 * Friends tab with Friends / Requests / Leaderboard sub-tabs.
 * Friends list + leaderboard list are functional; Requests is a placeholder.
 */
@Composable
fun FriendsScreen(
    onOpenLeaderboard: () -> Unit,
    vm: ProfileViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    var tab by remember { mutableStateOf("friends") }
    var showAddDialog by remember { mutableStateOf(false) }
    val statusInsets = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    if (showAddDialog) {
        AddFriendDialog(
            state = state,
            onSearch = { vm.searchByHandle(it) },
            onSendRequest = { vm.sendFriendRequest(it) },
            onDismiss = { showAddDialog = false; vm.clearSearch() },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .padding(top = statusInsets),
    ) {
        ScreenHeader(
            title = tr(R.string.friends),
            large = true,
            right = {
                IconButton(
                    name = "plus",
                    onClick = { showAddDialog = true },
                    background = tokens.accent.p2,
                    tint = Color.White,
                )
            },
        )

        TabPills(
            tabs = listOf(
                "friends" to tr(R.string.friends),
                "requests" to tr(R.string.requests),
                "lb" to tr(R.string.leaderboard),
            ),
            active = tab,
            onSelect = { tab = it },
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp),
        )

        when (tab) {
            "friends" -> FriendsList(state.friends, modifier = Modifier.weight(1f).fillMaxWidth())
            "requests" -> EmptyState(text = "No new requests", modifier = Modifier.weight(1f).fillMaxWidth())
            "lb" -> LeaderboardPodium(modifier = Modifier.weight(1f).fillMaxWidth(), onOpenFull = onOpenLeaderboard)
        }
    }
}

@Composable
private fun FriendsList(friends: List<FriendDto>, modifier: Modifier = Modifier) {
    val tokens = GymTheme.tokens
    LazyColumn(
        modifier = modifier.padding(horizontal = 18.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                "${tr(R.string.online).uppercase()} · ${friends.count { it.online }}",
                color = tokens.surface.textDim,
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
            )
        }
        items(friends, key = { it.id }) { friend ->
            FriendRow(friend)
        }
        if (friends.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                    Text("No friends yet", color = tokens.surface.textMuted, style = TextStyle(fontSize = 14.sp))
                }
            }
        }
    }
}

@Composable
private fun FriendRow(friend: FriendDto) {
    val tokens = GymTheme.tokens
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
        Box {
            Avatar(name = friend.user.name, size = 48.dp, color1 = tokens.accent.p3, color2 = tokens.accent.p2)
            if (friend.online) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(tokens.surface.success)
                        .border(2.dp, tokens.surface.surface, CircleShape),
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(friend.user.name, color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
            Text(
                "Lv. ${friend.user.stats.level} · ${friend.user.stats.streak} streak",
                color = tokens.surface.textMuted,
                style = TextStyle(fontSize = 11.sp),
            )
        }
        IconButton(name = "send", onClick = {}, size = 32.dp, iconSize = 14.dp)
    }
}

@Composable
private fun EmptyState(text: String, modifier: Modifier = Modifier) {
    val tokens = GymTheme.tokens
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AppIcon("users", size = 42.dp, tint = tokens.surface.textDim)
            Text(text, color = tokens.surface.textMuted, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
        }
    }
}

@Composable
private fun LeaderboardPodium(modifier: Modifier, onOpenFull: () -> Unit) {
    val tokens = GymTheme.tokens
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("Top this week", color = tokens.surface.text, style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold))
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom,
        ) {
            PodiumColumn(rank = 2, name = "Sofia", height = 90.dp, color = tokens.accent.p3)
            PodiumColumn(rank = 1, name = "Alina", height = 110.dp, color = tokens.accent.p2)
            PodiumColumn(rank = 3, name = "Mia", height = 80.dp, color = tokens.accent.p1)
        }
        Text(
            text = "View full leaderboard →",
            color = tokens.accent.p2,
            modifier = Modifier.padding(top = 8.dp).clip(RoundedCornerShape(10.dp)).background(tokens.surface.chip).padding(horizontal = 12.dp, vertical = 8.dp),
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
private fun PodiumColumn(rank: Int, name: String, height: androidx.compose.ui.unit.Dp, color: Color) {
    val tokens = GymTheme.tokens
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Avatar(name = name, size = 56.dp, color1 = color, color2 = color)
        Text(name, color = tokens.surface.text, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
        Column(
            modifier = Modifier
                .size(width = 70.dp, height = height)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(color),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(rank.toString(), color = Color.White, style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black), modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
private fun AddFriendDialog(
    state: app.gymbuddy.viewmodel.ProfileUiState,
    onSearch: (String) -> Unit,
    onSendRequest: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val tokens = GymTheme.tokens
    var handle by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .shadow(24.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(tokens.surface.bg)
                .imePadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Add Friend", color = tokens.surface.text, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold))
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(tokens.surface.chip).clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center,
                ) { AppIcon("close", size = 16.dp, tint = tokens.surface.textDim) }
            }

            Text(
                "Enter the user's handle (e.g. gb_00000001 or your_handle)",
                color = tokens.surface.textMuted,
                style = TextStyle(fontSize = 12.sp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(tokens.surface.surface)
                        .border(1.dp, tokens.surface.border, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                ) {
                    if (handle.isEmpty()) {
                        Text("gb_00000001", color = tokens.surface.textDim, style = TextStyle(fontSize = 14.sp))
                    }
                    BasicTextField(
                        value = handle,
                        onValueChange = { handle = it.lowercase().filter { c -> c.isLetterOrDigit() || c == '_' }.take(15) },
                        cursorBrush = SolidColor(tokens.surface.text),
                        textStyle = TextStyle(fontSize = 14.sp, color = tokens.surface.text),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(tokens.accent.p2)
                        .clickable(enabled = handle.length >= 3) { onSearch(handle) },
                    contentAlignment = Alignment.Center,
                ) {
                    if (state.searchLoading) {
                        Text("…", color = Color.White, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                    } else {
                        AppIcon("search", size = 18.dp, tint = Color.White)
                    }
                }
            }

            when {
                state.searchError != null -> {
                    Text(state.searchError!!, color = tokens.surface.danger, style = TextStyle(fontSize = 13.sp))
                }
                state.searchResult != null -> {
                    val u = state.searchResult!!
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(tokens.surface.surface)
                            .border(1.dp, tokens.surface.border, RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Avatar(name = u.name, size = 48.dp, color1 = tokens.accent.p3, color2 = tokens.accent.p2)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(u.name, color = tokens.surface.text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                            Text("@${u.userHandle.ifEmpty { u.username }}", color = tokens.surface.textMuted, style = TextStyle(fontSize = 11.sp))
                        }
                        if (state.friendRequestSent) {
                            Text("Sent ✓", color = tokens.surface.success, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(tokens.accent.p2)
                                    .clickable { onSendRequest(u.id) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                            ) {
                                Text("Add", color = Color.White, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }
        }
    }
}
