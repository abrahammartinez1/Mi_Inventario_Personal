package com.abraham.mi.inventario.miinventariopersonal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val TextStyleGlobal = TextStyle(
    color = Color.Black,
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    letterSpacing = 0.sp,
    lineHeight = 24.sp
)

val TextStyleButton = TextStyle(
    color = Color.White,
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    letterSpacing = 0.sp,
    lineHeight = 22.sp
)

val TextStyleTitle = TextStyle(
    color = Color.Blue,
    fontSize = 18.sp,
    fontWeight = FontWeight.W600,
    letterSpacing = 1.sp,
    lineHeight = 24.sp,
)