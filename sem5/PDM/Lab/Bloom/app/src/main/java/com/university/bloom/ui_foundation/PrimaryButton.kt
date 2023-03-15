package com.university.bloom.ui_foundation

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary,
            disabledBackgroundColor = MaterialTheme.colors.primary
        ),
        onClick = onClick,
    ) {
        Text(text = text, style = MaterialTheme.typography.button, color = MaterialTheme.colors.onSecondary)
    }
}