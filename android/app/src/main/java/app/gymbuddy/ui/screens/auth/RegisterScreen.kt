package app.gymbuddy.ui.screens.auth

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gymbuddy.R
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.GradientButton
import app.gymbuddy.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    onGoLogin: () -> Unit,
    vm: AuthViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.success) { if (state.success) onRegistered() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Spacer(Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(tokens.accent.gradient()),
            contentAlignment = Alignment.Center,
        ) {
            Text("GB", color = Color.White, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.ExtraBold))
        }
        Text(
            text = tr(R.string.join),
            color = tokens.surface.text,
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp),
        )
        Spacer(Modifier.height(8.dp))
        FormField(label = tr(R.string.name), value = state.name, onChange = vm::setName)
        FormField(label = tr(R.string.email), value = state.email, onChange = vm::setEmail, keyboard = KeyboardType.Email)
        FormField(label = tr(R.string.password), value = state.password, onChange = vm::setPassword, keyboard = KeyboardType.Password, password = true)
        FormField(label = tr(R.string.age), value = state.age, onChange = vm::setAge, keyboard = KeyboardType.Number)
        if (state.error != null) {
            Text(state.error!!, color = tokens.surface.danger, style = TextStyle(fontSize = 13.sp))
        }
        Spacer(Modifier.height(8.dp))
        if (state.loading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = tokens.accent.p2)
            }
        } else {
            GradientButton(text = tr(R.string.sign_up), onClick = vm::register)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(tr(R.string.have_account) + " ", color = tokens.surface.textMuted, style = TextStyle(fontSize = 13.sp))
            Text(
                text = tr(R.string.sign_in),
                color = tokens.accent.p2,
                modifier = Modifier.clickable(onClick = onGoLogin),
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
            )
        }
    }
}
