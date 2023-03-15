package com.university.bloom.ui.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.university.bloom.theming.BloomTheme
import com.university.bloom.ui_foundation.BloomTextField

@Composable
fun LoginDestination(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccessful: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var username: String by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState.loginSuccessful) {
            onLoginSuccessful()
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .padding(all = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BloomTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            text = username,
            placeholder = "Username",
            onValueChanged = { username = it }
        )

        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            onClick = {
                viewModel.onLoginClick(username)
            },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary,
                disabledBackgroundColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = "Next", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onSecondary)
        }

        if (uiState.loading) {
            Spacer(modifier = Modifier.size(16.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary)
        }
    }
}

@Preview(name = "LightMode")
@Composable
private fun PreviewLoginScreen() {
    BloomTheme {
        LoginDestination(onLoginSuccessful = {})
    }
}