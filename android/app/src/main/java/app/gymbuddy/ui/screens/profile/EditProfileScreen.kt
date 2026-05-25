package app.gymbuddy.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gymbuddy.R
import app.gymbuddy.data.remote.dto.UpdateProfileRequest
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.AppIcon
import app.gymbuddy.ui.components.PhotoSlot
import app.gymbuddy.ui.components.Pill
import app.gymbuddy.ui.components.ScreenHeader
import app.gymbuddy.viewmodel.ProfileViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditProfileScreen(onBack: () -> Unit, vm: ProfileViewModel = hiltViewModel()) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    val u = state.user
    val statusInsets = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    var name by remember { mutableStateOf(u?.name ?: "") }
    var handle by remember { mutableStateOf(u?.userHandle ?: "") }
    var handleError by remember { mutableStateOf<String?>(null) }
    var age by remember { mutableStateOf(u?.age?.toString() ?: "") }
    var height by remember { mutableStateOf(u?.height?.toString() ?: "") }
    var weight by remember { mutableStateOf(u?.weight?.toString() ?: "") }
    var bio by remember { mutableStateOf(u?.bio ?: "") }
    var goal by remember { mutableStateOf(u?.goal ?: "Hypertrophy") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .padding(top = statusInsets),
    ) {
        // Fixed header — won't be hidden by status bar
        ScreenHeader(
            title = tr(R.string.edit_profile),
            large = true,
            left = {
                Text(
                    tr(R.string.cancel),
                    color = tokens.accent.p2,
                    modifier = Modifier.clickable(onClick = onBack),
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
                )
            },
            right = {
                Text(
                    tr(R.string.save),
                    color = tokens.accent.p2,
                    modifier = Modifier.clickable {
                        val handleVal = handle.trim()
                        val handleRegex = Regex("^[a-z][a-z0-9_]{2,14}$")
                        if (handleVal.isNotEmpty() && !handleRegex.matches(handleVal)) {
                            handleError = "3–15 chars, start with letter, only a–z 0–9 _"
                            return@clickable
                        }
                        handleError = null
                        vm.updateProfile(
                            UpdateProfileRequest(
                                name = name,
                                userHandle = handleVal.ifEmpty { null },
                                age = age.toIntOrNull(),
                                height = height.toIntOrNull(),
                                weight = weight.toIntOrNull(),
                                bio = bio,
                                goal = goal,
                            ),
                            onDone = onBack,
                        )
                    },
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                )
            },
        )

        // Scrollable content — properly handles keyboard and bottom nav
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
        ) {
            // Photos — only 2 prominent slots
            Row(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                listOf(0, 1).forEach { idx ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(3f / 4f)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, tokens.surface.border, RoundedCornerShape(14.dp)),
                    ) {
                        val photo = u?.photos?.getOrNull(idx)
                        if (photo != null) {
                            coil.compose.AsyncImage(
                                model = photo.url,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            )
                        } else {
                            PhotoSlot(color1 = tokens.accent.p3, color2 = tokens.accent.p2, modifier = Modifier.fillMaxSize(), label = "Photo ${idx + 1}")
                        }
                    }
                }
            }
            // Add more photos hint
            Text(
                "Tap a photo to change · Up to 6 photos total",
                color = tokens.surface.textDim,
                style = TextStyle(fontSize = 11.sp),
                modifier = Modifier.padding(start = 18.dp, top = 6.dp, bottom = 4.dp),
            )

            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Field(label = tr(R.string.name), value = name, onChange = { name = it })

                // User handle
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Field(
                        label = "ID / HANDLE",
                        value = handle,
                        onChange = { handle = it.lowercase().filter { c -> c.isLetterOrDigit() || c == '_' }.take(15) },
                    )
                    if (handleError != null) {
                        Text(handleError!!, color = tokens.surface.danger, style = TextStyle(fontSize = 11.sp))
                    } else {
                        Text(
                            "3–15 chars, lowercase letters, numbers, _ only",
                            color = tokens.surface.textDim,
                            style = TextStyle(fontSize = 11.sp),
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Field(label = tr(R.string.age), value = age, onChange = { age = it }, keyboard = KeyboardType.Number, modifier = Modifier.weight(1f))
                    Field(label = "${tr(R.string.height)} cm", value = height, onChange = { height = it }, keyboard = KeyboardType.Number, modifier = Modifier.weight(1f))
                    Field(label = "${tr(R.string.weight_label)} kg", value = weight, onChange = { weight = it }, keyboard = KeyboardType.Number, modifier = Modifier.weight(1f))
                }

                Field(label = tr(R.string.bio), value = bio, onChange = { bio = it }, minLines = 3)

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "${tr(R.string.goal).uppercase()}",
                        color = tokens.surface.textDim,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Strength", "Hypertrophy", "Mobility", "Calisthenics", "CrossFit", "Cardio").forEach { g ->
                            Pill(text = g, active = goal == g, onClick = { goal = g })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Field(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    keyboard: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    modifier: Modifier = Modifier,
) {
    val tokens = GymTheme.tokens
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label.uppercase(), color = tokens.surface.textDim, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(tokens.surface.surface)
                .border(1.dp, tokens.surface.border, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onChange,
                singleLine = minLines == 1,
                minLines = minLines,
                cursorBrush = SolidColor(tokens.surface.text),
                textStyle = TextStyle(fontSize = 15.sp, color = tokens.surface.text),
                keyboardOptions = KeyboardOptions(keyboardType = keyboard),
                modifier = Modifier.fillMaxWidth(),
            )
            if (value.isEmpty()) {
                Text("—", color = tokens.surface.textDim, style = TextStyle(fontSize = 15.sp))
            }
        }
    }
}
