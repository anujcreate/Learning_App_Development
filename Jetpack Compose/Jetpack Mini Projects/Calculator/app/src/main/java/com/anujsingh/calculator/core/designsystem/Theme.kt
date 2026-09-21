package com.anujsingh.calculator.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

object CalculatorTheme {
    val colors: CalculatorColorPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalCalculatorColors.current
}

@Composable
fun CalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorPalette = if (darkTheme) DarkColorPalette else LightColorPalette
    val materialScheme = if (darkTheme) {
        darkColorScheme(
            background = colorPalette.background,
            surface = colorPalette.buttonNumber
        )
    } else {
        lightColorScheme(
            background = colorPalette.background,
            surface = colorPalette.buttonNumber
        )
    }

    CompositionLocalProvider(LocalCalculatorColors provides colorPalette) {
        MaterialTheme(
            colorScheme = materialScheme,
            content = content
        )
    }
}