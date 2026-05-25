package app.gymbuddy.ui.screens.profile

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gymbuddy.R
import app.gymbuddy.l10n.AppLocale
import app.gymbuddy.l10n.LocalAppLocale
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.AccentPalette
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.theme.ThemeMode
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.IconButton
import app.gymbuddy.ui.components.ScreenHeader
import app.gymbuddy.viewmodel.AppSettingsViewModel

/**
 * Settings: theme mode (dark switch), accent palette picker, language toggle,
 * and stubbed account / legal sections + logout.
 */
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    settingsVm: AppSettingsViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by settingsVm.state.collectAsStateWithLifecycle()
    val locale = LocalAppLocale.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        ScreenHeader(
            title = tr(R.string.settings),
            large = true,
            left = { IconButton(name = "arrow-left", onClick = onBack) },
        )

        // Pro banner
        Box(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(tokens.accent.gradient())
                .padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center,
                ) { AppIcon("spark", size = 22.dp, tint = Color.White) }
                Column(modifier = Modifier.weight(1f)) {
                    Text("GymBuddy Pro", color = Color.White, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.ExtraBold))
                    Text(
                        "Unlimited swipes, boosts, analytics",
                        color = Color.White.copy(alpha = 0.9f),
                        style = TextStyle(fontSize = 12.sp),
                    )
                }
                AppIcon("chevron-right", size = 18.dp, tint = Color.White)
            }
        }

        // Preferences
        SectionTitle("PREFERENCES")
        SettingsGroup {
            // Theme toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                IconChip(icon = if (tokens.isDark) "moon" else "sun", color = tokens.surface.warn)
                Text(
                    tr(R.string.appearance),
                    color = tokens.surface.text,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
                )
                Switch(
                    checked = tokens.isDark,
                    onCheckedChange = { settingsVm.toggleDarkMode() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = tokens.accent.p2,
                    ),
                )
            }
            Divider()
            // Accent palette
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                IconChip(icon = "spark", color = tokens.accent.p2)
                Text(
                    tr(R.string.accent),
                    color = tokens.surface.text,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AccentSwatch(AccentPalette.Aurora, selected = state.accent == AccentPalette.Aurora) { settingsVm.setAccent(AccentPalette.Aurora) }
                    AccentSwatch(AccentPalette.Sunset, selected = state.accent == AccentPalette.Sunset) { settingsVm.setAccent(AccentPalette.Sunset) }
                    AccentSwatch(AccentPalette.Neon, selected = state.accent == AccentPalette.Neon) { settingsVm.setAccent(AccentPalette.Neon) }
                }
            }
            Divider()
            // Language
            SettingsRow(icon = "language", label = tr(R.string.language), color = tokens.accent.p2, value = if (locale == AppLocale.Ru) "Русский" else "English") {
                settingsVm.toggleLocale()
            }
            Divider()
            SettingsRow(icon = "dumbbell", label = tr(R.string.units), color = tokens.accent.p1, value = "kg / cm") { }
            Divider()
            SettingsRow(icon = "bell", label = tr(R.string.notifications), color = tokens.surface.warn, value = "On") { }
        }

        // Account
        SectionTitle("ACCOUNT")
        SettingsGroup {
            SettingsRow(icon = "user", label = "Profile", color = tokens.accent.p2) { }
            Divider()
            SettingsRow(icon = "medal", label = tr(R.string.achievements), color = tokens.surface.warn) { }
            Divider()
            SettingsRow(icon = "users", label = "Referrals", color = tokens.accent.p1) { }
            Divider()
            SettingsRow(icon = "trophy", label = tr(R.string.stats), color = tokens.surface.danger) { }
        }

        // Legal
        SectionTitle("LEGAL")
        SettingsGroup {
            SettingsRow(icon = "list", label = tr(R.string.privacy), color = tokens.surface.textMuted) { }
            Divider()
            SettingsRow(icon = "list", label = tr(R.string.terms), color = tokens.surface.textMuted) { }
        }

        // Logout
        Box(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                tr(R.string.logout),
                color = tokens.surface.danger,
                modifier = Modifier.clickable { settingsVm.logout(); onLogout() },
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    val tokens = GymTheme.tokens
    Text(
        text,
        color = tokens.surface.textDim,
        modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 20.dp, bottom = 6.dp),
        style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
    )
}

@Composable
private fun SettingsGroup(content: @Composable () -> Unit) {
    val tokens = GymTheme.tokens
    Column(
        modifier = Modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(tokens.surface.surface)
            .border(1.dp, tokens.surface.border, RoundedCornerShape(14.dp)),
    ) { content() }
}

@Composable
private fun Divider() {
    val tokens = GymTheme.tokens
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(tokens.surface.border),
    )
}

@Composable
private fun SettingsRow(
    icon: String,
    label: String,
    color: Color,
    value: String? = null,
    onClick: () -> Unit,
) {
    val tokens = GymTheme.tokens
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        IconChip(icon = icon, color = color)
        Text(
            label,
            color = tokens.surface.text,
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
        )
        if (value != null) {
            Text(value, color = tokens.surface.textMuted, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold))
        }
        AppIcon("chevron-right", size = 16.dp, tint = tokens.surface.textDim)
    }
}

@Composable
private fun IconChip(icon: String, color: Color) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.13f)),
        contentAlignment = Alignment.Center,
    ) { AppIcon(icon, size = 18.dp, tint = color) }
}

@Composable
private fun AccentSwatch(palette: AccentPalette, selected: Boolean, onClick: () -> Unit) {
    val tokens = GymTheme.tokens
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(androidx.compose.foundation.shape.CircleShape)
            .background(palette.tokens().gradient())
            .border(
                width = if (selected) 3.dp else 0.dp,
                color = if (selected) tokens.surface.text else Color.Transparent,
                shape = androidx.compose.foundation.shape.CircleShape,
            )
            .clickable(onClick = onClick),
    )
}
