package com.penguenlabs.pushnote.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.penguenlabs.pushnote.R

val RedHadDisplay = FontFamily(
    Font(R.font.red_had_display_light, FontWeight.Light),
    Font(R.font.red_had_display_regular, FontWeight.Normal),
    Font(R.font.red_had_display_medium, FontWeight.Medium),
    Font(R.font.red_had_display_bold, FontWeight.Bold),
    Font(R.font.red_had_display_black, FontWeight.Black)
)

val Typography = Typography(defaultFontFamily = RedHadDisplay)