package com.anujsingh.calculator.feature.calculator.presentation

import androidx.compose.runtime.Stable
import com.anujsingh.calculator.core.presentation.UiText
import com.anujsingh.calculator.feature.calculator.domain.model.CalculatorOperator

@Stable
data class CalculatorState(
    val formulaDisplay: String = "",       // e.g., "38,670÷50,000" (secondary top line)
    val resultDisplay: String = "0",       // e.g., "0.7734" (primary bold white line)
    val isEvaluated: Boolean = false,      // true when '=' was just pressed
    val selectedOperator: CalculatorOperator? = null, // active operator for Apple highlight inversion
    val canClearOperand: Boolean = false,  // true when an operand is being typed (C vs AC)
    val error: UiText? = null
)

