package com.university.bloom.theming

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val ColorScheme = lightColors(
    primary = Pink100,
    secondary = Pink900,

    background = White,
    surface = White850,
    onPrimary = Gray,
    onSecondary = White,
    onBackground = Gray,
    onSurface = Gray,
)

@Composable
fun BloomTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = ColorScheme,
        typography = BloomTypography,
        shapes = BloomShapes,
        content = content
    )
}