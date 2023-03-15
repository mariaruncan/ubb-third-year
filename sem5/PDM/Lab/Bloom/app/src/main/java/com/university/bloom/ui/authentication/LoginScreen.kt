package com.university.bloom.ui.authentication

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.university.bloom.R
import com.university.bloom.theming.BloomTheme
import com.university.bloom.ui_foundation.BloomTextField
import com.university.bloom.utils.displayToast

@Composable
fun LoginDestination(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccessful: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    if (uiState.error != null) {
        displayToast(LocalContext.current, uiState.error?.message)
        viewModel.onErrorConsumed()
    }
    LaunchedEffect(key1 = uiState) {
        if (uiState.loginSuccessful) {
            onLoginSuccessful()
        }
    }

    var username: String by rememberSaveable { mutableStateOf("") }
    var password: String by rememberSaveable { mutableStateOf("") }
    LoginScreen(
        username = username,
        password = password,
        onLoginClick = { viewModel.onLoginClick(username, password) },
        onUsernameChanged = { username = it },
        onPasswordChanged = { password = it }
    )
}

@Composable
private fun LoginScreen(
    username: String,
    password: String,
    onLoginClick: () -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .padding(all = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var isLoginButtonEnabled by remember { mutableStateOf(false) }

        Image(
           painter = painterResource(id = R.drawable.logo),
           contentDescription = null
        )

        BloomTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            text = username,
            placeholder = "Username",
            onValueChanged = {
                onUsernameChanged(it)
                isLoginButtonEnabled = username.isNotEmpty() && password.isNotEmpty()
            },
        )

        BloomTextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            text = password,
            placeholder = "Password",
            onValueChanged = {
                onPasswordChanged(it)
                isLoginButtonEnabled = username.isNotEmpty() && password.isNotEmpty()
            }
        )

        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            onClick = onLoginClick,
            shape = MaterialTheme.shapes.medium,
            enabled = isLoginButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary,
                disabledBackgroundColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = "Log in", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onSecondary)
        }
    }
}

@Preview(name = "LightMode")
@Preview(name = "DarkMode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLoginScreen() {
    BloomTheme {
        LoginScreen(
            username = "",
            password = "",
            onLoginClick = { /*TODO*/ },
            onUsernameChanged = { /*TODO*/ },
            onPasswordChanged = { /*TODO*/ })
    }
}