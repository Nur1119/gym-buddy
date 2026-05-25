package app.gymbuddy.ui.screens.auth

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gymbuddy.BuildConfig
import app.gymbuddy.R
import app.gymbuddy.l10n.tr
import app.gymbuddy.theme.GymTheme
import app.gymbuddy.ui.components.GradientButton
import app.gymbuddy.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    onGoRegister: () -> Unit,
    vm: AuthViewModel = hiltViewModel(),
) {
    val tokens = GymTheme.tokens
    val state by vm.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.success) { if (state.success) onLoggedIn() }

    val context = LocalContext.current
    val googleSignInClient = remember(context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            runCatching {
                val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    .getResult(ApiException::class.java)
                account.idToken?.let { vm.loginWithGoogle(it) }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.surface.bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Spacer(Modifier.height(40.dp))

        Text(
            text = tr(R.string.welcome),
            color = tokens.surface.text,
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp),
        )
        Text(
            text = "Sign in to your account",
            color = tokens.surface.textMuted,
            style = TextStyle(fontSize = 14.sp),
        )

        Spacer(Modifier.height(8.dp))

        FormField(label = tr(R.string.email), value = state.email, onChange = vm::setEmail, keyboard = KeyboardType.Email)
        FormField(label = tr(R.string.password), value = state.password, onChange = vm::setPassword, keyboard = KeyboardType.Password, password = true)

        if (state.error != null) {
            Text(state.error!!, color = tokens.surface.danger, style = TextStyle(fontSize = 13.sp))
        }

        Spacer(Modifier.height(8.dp))

        if (state.loading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = tokens.accent.p2)
            }
        } else {
            GradientButton(text = tr(R.string.sign_in), onClick = vm::login)
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(tokens.surface.surface)
                .border(1.dp, tokens.surface.border, RoundedCornerShape(12.dp))
                .clickable { googleLauncher.launch(googleSignInClient.signInIntent) }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "G  Continue with Google",
                color = tokens.surface.text,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(tr(R.string.no_account) + " ", color = tokens.surface.textMuted, style = TextStyle(fontSize = 13.sp))
            Text(
                text = tr(R.string.sign_up),
                color = tokens.accent.p2,
                modifier = Modifier.clickable(onClick = onGoRegister),
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Composable
internal fun FormField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    keyboard: KeyboardType = KeyboardType.Text,
    password: Boolean = false,
) {
    val tokens = GymTheme.tokens
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label.uppercase(),
            color = tokens.surface.textDim,
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
        )
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
                singleLine = true,
                cursorBrush = SolidColor(tokens.surface.text),
                textStyle = TextStyle(fontSize = 15.sp, color = tokens.surface.text),
                visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboard,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

