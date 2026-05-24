package app.gymbuddy.ui.screens.discover

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import app.gymbuddy.data.remote.dto.UserDto
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.viewmodel.DiscoverViewModel
import app.gymbuddy.viewmodel.SwipeDirection
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Main Discover screen: a stack of swipe cards with action buttons.
 *
 * Polished to match the prototype: stacked depth (top + 2 ghost cards),
 * draggable top card with rotation, LIKE/NOPE badges, smooth animated
 * dismissal, and five action buttons (rewind / pass / superlike / like / boost).
 */
@Composable
fun DiscoverScreen(
    onOpenFilters: () -> Unit,
    onOpenMatches: () -> Unit,
    onMatch: (UserDto) -> Unit,
    vm: DiscoverViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val screenWidthPx = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val statusInsets = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .padding(top = statusInsets),
    ) {
        // ── Header (no large title; brand wordmark center, filters left, bell right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(name = "sliders", onClick = onOpenFilters)
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "GymBuddy",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp,
                        brush = tokens.accent.gradient(),
                    ),
                )
            }
            IconButton(name = "bell", onClick = onOpenMatches, badge = 3)
        }

        // ── Card stack (takes remaining vertical space minus action row)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            val stack = state.stack
            if (stack.isEmpty()) {
                EmptyDeck()
            } else {
                // Render up to 3 cards: top is interactive, behind cards have slight scale offset
                val visible = stack.take(3)
                visible.reversed().forEachIndexed { idxFromBack, user ->
                    val stackDepth = visible.size - 1 - idxFromBack // 0 = top
                    val isTop = stackDepth == 0
                    val rotation = if (isTop) (offsetX.value / 20f) else 0f
                    val cardModifier = if (isTop) {
                        Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = offsetX.value
                                translationY = offsetY.value
                                rotationZ = rotation
                            }
                            .pointerInput(user.id) {
                                detectDragGestures(
                                    onDragEnd = {
                                        val shouldSwipe = abs(offsetX.value) > screenWidthPx * 0.25f
                                        if (shouldSwipe) {
                                            val target = if (offsetX.value > 0) screenWidthPx * 1.2f else -screenWidthPx * 1.2f
                                            val dir = if (offsetX.value > 0) SwipeDirection.Like else SwipeDirection.Pass
                                            scope.launch {
                                                offsetX.animateTo(target, animationSpec = tween(280))
                                                vm.swipe(dir)
                                                offsetX.snapTo(0f); offsetY.snapTo(0f)
                                            }
                                        } else {
                                            scope.launch {
                                                offsetX.animateTo(0f, animationSpec = tween(200))
                                                offsetY.animateTo(0f, animationSpec = tween(200))
                                            }
                                        }
                                    },
                                    onDragCancel = {
                                        scope.launch {
                                            offsetX.animateTo(0f, animationSpec = tween(200))
                                            offsetY.animateTo(0f, animationSpec = tween(200))
                                        }
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        scope.launch {
                                            offsetX.snapTo(offsetX.value + dragAmount.x)
                                            offsetY.snapTo(offsetY.value + dragAmount.y * 0.5f)
                                        }
                                    },
                                )
                            }
                            .zIndex(10f)
                    } else {
                        Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val s = 1f - stackDepth * 0.04f
                                scaleX = s
                                scaleY = s
                                translationY = stackDepth * 16.dp.toPx()
                                alpha = 0.85f - stackDepth * 0.15f
                            }
                            .zIndex(10f - stackDepth)
                    }
                    Box(modifier = cardModifier) {
                        SwipeCard(user = user, offsetX = if (isTop) offsetX.value else 0f, isTop = isTop)
                    }
                }
            }
        }

        // ── Action row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActionButton(icon = "rewind", color = tokens.surface.warn, size = 44.dp) { vm.rewind() }
            ActionButton(icon = "close", color = tokens.accent.nope, size = 56.dp) {
                scope.launch {
                    offsetX.animateTo(-screenWidthPx * 1.2f, tween(280))
                    vm.swipe(SwipeDirection.Pass)
                    offsetX.snapTo(0f)
                }
            }
            ActionButton(icon = "star", color = tokens.accent.superLike, size = 48.dp, glow = true) {
                vm.swipe(SwipeDirection.SuperLike)
            }
            ActionButton(icon = "heart-fill", color = tokens.accent.like, size = 56.dp) {
                scope.launch {
                    offsetX.animateTo(screenWidthPx * 1.2f, tween(280))
                    vm.swipe(SwipeDirection.Like)
                    offsetX.snapTo(0f)
                }
            }
            ActionButton(icon = "bolt", color = tokens.accent.boost, size = 44.dp, glow = true) { vm.boost() }
        }
    }

    // ── Match modal
    state.newMatch?.let { match ->
        MatchModal(
            user = match.user,
            onClose = { vm.dismissMatch() },
            onChat = {
                val u = match.user
                vm.dismissMatch()
                onMatch(u)
            },
        )
    }
}

@Composable
private fun EmptyDeck() {
    val tokens = GymTheme.tokens
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        AppIcon("heart", size = 48.dp, tint = tokens.surface.textDim)
        Text(
            text = "You're all caught up! Check back later.",
            color = tokens.surface.textMuted,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
private fun ActionButton(
    icon: String,
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    glow: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(size)
            .shadow(
                elevation = if (glow) 18.dp else 6.dp,
                shape = CircleShape,
                ambientColor = color,
                spotColor = color,
            )
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        AppIcon(icon, size = size * 0.45f, tint = color)
    }
}
