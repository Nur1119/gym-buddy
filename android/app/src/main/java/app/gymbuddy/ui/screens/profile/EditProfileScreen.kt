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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
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
    var name by remember { mutableStateOf(u?.name ?: "") }
    var age by remember { mutableStateOf((u?.age ?: 0).toString()) }
    var height by remember { mutableStateOf((u?.height ?: 0).toString()) }
    var weight by remember { mutableStateOf((u?.weight ?: 0).toString()) }
    var bio by remember { mutableStateOf(u?.bio ?: "") }
    var goal by remember { mutableStateOf(u?.goal ?: "Hypertrophy") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .verticalScroll(rememberScrollState()),
    ) {
        ScreenHeader(
            title = tr(R.string.edit_profile),
            large = true,
            left = {
                Text(tr(R.string.cancel), color = tokens.accent.p2, modifier = Modifier.clickable(onClick = onBack), style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold))
            },
            right = {
                Text(tr(R.string.save), color = tokens.accent.p2, modifier = Modifier.clickable {
                    vm.updateProfile(
                        UpdateProfileRequest(
                            name = name,
                            age = age.toIntOrNull(),
                            height = height.toIntOrNull(),
                            weight = weight.toIntOrNull(),
                            bio = bio,
                            goal = goal,
                        ),
                        onDone = onBack,
                    )
                }, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
            },
        )

        // Photo grid
        Column(modifier = Modifier.padding(horizontal = 18.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(0 to 1, 2 to 3, 4 to 5).forEach { (a, b) ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(a, b).forEach { idx ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(3f / 4f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, tokens.surface.border, RoundedCornerShape(12.dp)),
                            ) {
                                if (idx < 2) {
                                    PhotoSlot(color1 = tokens.accent.p3, color2 = tokens.accent.p2, modifier = Modifier.fillMaxSize(), label = "photo ${idx + 1}")
                                } else {
                                    Box(modifier = Modifier.fillMaxSize().background(tokens.surface.surface), contentAlignment = Alignment.Center) {
                                        AppIcon("plus", size = 20.dp, tint = tokens.surface.textDim)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Field(label = tr(R.string.name), value = name, onChange = { name = it })

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Field(label = tr(R.string.age), value = age, onChange = { age = it }, keyboard = KeyboardType.Number, modifier = Modifier.weight(1f))
                Field(label = "${tr(R.string.height)} cm", value = height, onChange = { height = it }, keyboard = KeyboardType.Number, modifier = Modifier.weight(1f))
                Field(label = "${tr(R.string.weight_label)} kg", value = weight, onChange = { weight = it }, keyboard = KeyboardType.Number, modifier = Modifier.weight(1f))
            }

            Field(label = tr(R.string.bio), value = bio, onChange = { bio = it }, minLines = 3)

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(tr(R.string.goal).uppercase(), color = tokens.surface.textDim, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Strength", "Hypertrophy", "Mobility", "Calisthenics", "CrossFit", "Cardio").forEach { g ->
                        Pill(text = g, active = goal == g, onClick = { goal = g })
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
        }
    }
}
