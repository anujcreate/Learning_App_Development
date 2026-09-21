package com.anujsingh.youcalculator.core.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Shared Accent Colors
val VibrantOrange = Color(0xFFFF9F0A)
val VibrantOrangePressed = Color(0xFFCC7F08)

// Dark Theme Palette (matching screenshot)
val DarkBackground = Color(0xFF000000)
val DarkDisplayPrimary = Color(0xFFFFFFFF)
val DarkDisplaySecondary = Color(0xFF8E8E93)
val DarkButtonTopRow = Color(0xFF505050)
val DarkButtonNumber = Color(0xFF2C2C2E)
val DarkTopBarIconBg = Color(0xFF1C1C1E)
val DarkTextPrimary = Color(0xFFFFFFFF)

// Light Theme Palette (modern high-contrast light mode)
val LightBackground = Color(0xFFF2F2F7)
val LightDisplayPrimary = Color(0xFF000000)
val LightDisplaySecondary = Color(0xFF6C757D)
val LightButtonTopRow = Color(0xFFD1D1D6)
val LightButtonNumber = Color(0xFFFFFFFF)
val LightTopBarIconBg = Color(0xFFE5E5EA)
val LightTextPrimary = Color(0xFF000000)

@Immutable
data class CalculatorColorPalette(
    val background: Color,
    val displayPrimary: Color,
    val displaySecondary: Color,
    val buttonTopRow: Color,
    val buttonTopRowText: Color,
    val buttonNumber: Color,
    val buttonNumberText: Color,
    val buttonOperator: Color,
    val buttonOperatorText: Color,
    val topBarIconBg: Color,
    val topBarIconTint: Color
)

val DarkColorPalette = CalculatorColorPalette(
    background = DarkBackground,
    displayPrimary = DarkDisplayPrimary,
    displaySecondary = DarkDisplaySecondary,
    buttonTopRow = DarkButtonTopRow,
    buttonTopRowText = DarkTextPrimary,
    buttonNumber = DarkButtonNumber,
    buttonNumberText = DarkTextPrimary,
    buttonOperator = VibrantOrange,
    buttonOperatorText = Color.White,
    topBarIconBg = DarkTopBarIconBg,
    topBarIconTint = Color.White
)

val LightColorPalette = CalculatorColorPalette(
    background = LightBackground,
    displayPrimary = LightDisplayPrimary,
    displaySecondary = LightDisplaySecondary,
    buttonTopRow = LightButtonTopRow,
    buttonTopRowText = LightTextPrimary,
    buttonNumber = LightButtonNumber,
    buttonNumberText = LightTextPrimary,
    buttonOperator = VibrantOrange,
    buttonOperatorText = Color.White,
    topBarIconBg = LightTopBarIconBg,
    topBarIconTint = Color.Black
)

val LocalCalculatorColors = staticCompositionLocalOf { DarkColorPalette }