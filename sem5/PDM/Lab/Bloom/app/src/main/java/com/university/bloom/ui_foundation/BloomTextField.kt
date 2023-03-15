package com.university.bloom.ui_foundation

import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BloomTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String = "",
    icon: @Composable (() -> Unit)? = null,
    onValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        leadingIcon = icon,
        placeholder = { Text(text = placeholder) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.onPrimary,
            textColor = MaterialTheme.colors.onPrimary
        ),
        onValueChange = { onValueChanged(it) },
    )
}