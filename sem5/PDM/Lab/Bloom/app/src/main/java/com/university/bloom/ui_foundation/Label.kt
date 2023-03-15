package com.university.bloom.ui_foundation

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun Label(text: String) {
    Text(text = text, style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.onPrimary)
}