package com.penguenlabs.pushnote.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.penguenlabs.pushnote.R

val latoFontFamily = FontFamily(
    Font(R.font.lato_black, FontWeight.Black),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_light, FontWeight.Light),
    Font(R.font.lato_regular, FontWeight.Normal),
    Font(R.font.lato_thin, FontWeight.Thin)
)

val typography = Typography(
    displayLarge = TextStyle(fontFamily = latoFontFamily, fontSize = 57.sp, lineHeight = 64.sp),
    displayMedium = TextStyle(fontFamily = latoFontFamily, fontSize = 45.sp, lineHeight = 52.sp),
    displaySmall = TextStyle(fontFamily = latoFontFamily, fontSize = 36.sp, lineHeight = 44.sp),
    headlineLarge = TextStyle(fontFamily = latoFontFamily, fontSize = 32.sp, lineHeight = 40.sp),
    headlineMedium = TextStyle(fontFamily = latoFontFamily, fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall = TextStyle(fontFamily = latoFontFamily, fontSize = 24.sp, lineHeight = 32.sp),
    titleLarge = TextStyle(fontFamily = latoFontFamily, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = latoFontFamily, fontSize = 16.sp, lineHeight = 24.sp),
    titleSmall = TextStyle(fontFamily = latoFontFamily, fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontFamily = latoFontFamily, fontSize = 14.sp, lineHeight = 20.sp),
    bodyMedium = TextStyle(fontFamily = latoFontFamily, fontSize = 12.sp, lineHeight = 16.sp),
    bodySmall = TextStyle(fontFamily = latoFontFamily, fontSize = 11.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontFamily = latoFontFamily, fontSize = 16.sp, lineHeight = 24.sp),
    labelMedium = TextStyle(fontFamily = latoFontFamily, fontSize = 14.sp, lineHeight = 20.sp),
    labelSmall = TextStyle(fontFamily = latoFontFamily, fontSize = 12.sp, lineHeight = 10.sp)
)