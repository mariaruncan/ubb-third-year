package com.university.bloom.theming

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.university.bloom.R

val NunitoSans = FontFamily(
    // Regular
    Font(R.font.nunitosans_regular, FontWeight.Medium, FontStyle.Normal),
    // Bold
    Font(R.font.nunitosans_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.nunito_sans_bold_italic, FontWeight.Bold, FontStyle.Italic),
    // Semibold
    Font(R.font.nunitosans_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.nunitosans_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    // Light
    Font(R.font.nunitosans_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.nunitosans_light_italic, FontWeight.Light, FontStyle.Italic),
)

// Set of Material typography styles to start with
val BloomTypography = Typography(
    h1 = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.sp
    ),
    h2 = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        letterSpacing = 0.sp
    ),
    body1 = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    body2 = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        letterSpacing = 0.sp
    ),
    button = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 1.sp
    ),
    caption = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 0.sp
    ),
)