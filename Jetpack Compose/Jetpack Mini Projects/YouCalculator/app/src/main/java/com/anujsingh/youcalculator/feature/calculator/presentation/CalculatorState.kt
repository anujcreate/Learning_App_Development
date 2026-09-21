package com.anujsingh.youcalculator.feature.calculator.presentation

import androidx.compose.runtime.Stable
import com.anujsingh.youcalculator.core.presentation.UiText

@Stable
data class CalculatorState(
    val formulaDisplay: String = "",       // e.g., "38,670÷50,000" (secondary top line)
    val resultDisplay: String = "0",       // e.g., "0.7734" (primary bold white line)
    val isEvaluated: Boolean = false,      // true when '=' was just pressed
    val error: UiText? = null
)
