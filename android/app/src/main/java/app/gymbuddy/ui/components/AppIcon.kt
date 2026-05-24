package app.gymbuddy.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Project-wide icon names map to Material Icons.
 *
 * Names mirror those used in the design tokens (home, dumbbell, flame, …)
 * so calling code stays close to the JSX prototype:
 *
 * ```kotlin
 * AppIcon(name = "heart-fill", size = 22.dp, tint = ...)
 * ```
 */
@Composable
fun AppIcon(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    tint: Color = Color.Unspecified,
) {
    val vector: ImageVector = when (name) {
        "home" -> Icons.Filled.Home
        "dumbbell" -> Icons.Filled.FitnessCenter
        "flame" -> Icons.Filled.LocalFireDepartment
        "spark" -> Icons.Outlined.AutoAwesome
        "heart" -> Icons.Filled.FavoriteBorder
        "heart-fill" -> Icons.Filled.Favorite
        "user" -> Icons.Outlined.Person
        "users" -> Icons.Outlined.People
        "cog" -> Icons.Filled.Settings
        "plus" -> Icons.Filled.Add
        "check" -> Icons.Filled.Check
        "close" -> Icons.Filled.Close
        "chevron-right" -> Icons.Filled.KeyboardArrowRight
        "chevron-left" -> Icons.Filled.KeyboardArrowLeft
        "chevron-down" -> Icons.Filled.KeyboardArrowDown
        "arrow-right" -> Icons.AutoMirrored.Outlined.ArrowForward
        "arrow-left" -> Icons.AutoMirrored.Outlined.ArrowBack
        "search" -> Icons.Filled.Search
        "calendar" -> Icons.Outlined.CalendarMonth
        "bell" -> Icons.Filled.Notifications
        "filter" -> Icons.Filled.Tune
        "bolt" -> Icons.Filled.Bolt
        "rewind" -> Icons.Filled.Replay
        "star" -> Icons.Filled.Star
        "send" -> Icons.AutoMirrored.Outlined.Send
        "apple" -> Icons.Filled.Restaurant
        "medal" -> Icons.Filled.MilitaryTech
        "trophy" -> Icons.Filled.EmojiEvents
        "chart" -> Icons.Filled.BarChart
        "play" -> Icons.Filled.PlayArrow
        "pause" -> Icons.Filled.Pause
        "image" -> Icons.Filled.Image
        "camera" -> Icons.Filled.CameraAlt
        "edit" -> Icons.Filled.Edit
        "language" -> Icons.Filled.Language
        "moon" -> Icons.Filled.DarkMode
        "sun" -> Icons.Filled.LightMode
        "location" -> Icons.Filled.LocationOn
        "clock" -> Icons.Outlined.AccessTime
        "list" -> Icons.AutoMirrored.Outlined.List
        "grid" -> Icons.Filled.GridView
        "more" -> Icons.Filled.MoreHoriz
        "globe" -> Icons.Outlined.Public
        "sliders" -> Icons.Filled.Tune
        "chat" -> Icons.AutoMirrored.Outlined.Chat
        "schedule" -> Icons.Filled.Schedule
        else -> Icons.Outlined.HelpOutline
    }
    Box(modifier = modifier.size(size)) {
        Icon(imageVector = vector, contentDescription = name, tint = tint, modifier = Modifier.size(size))
    }
}
