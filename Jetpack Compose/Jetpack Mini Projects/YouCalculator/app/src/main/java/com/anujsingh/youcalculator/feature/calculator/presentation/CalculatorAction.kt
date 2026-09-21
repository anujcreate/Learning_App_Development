package com.anujsingh.youcalculator.feature.calculator.presentation

import com.anujsingh.youcalculator.feature.calculator.domain.model.CalculatorOperator

sealed interface CalculatorAction {
    data class OnDigitClick(val digit: Int) : CalculatorAction
    data class OnOperatorClick(val operator: CalculatorOperator) : CalculatorAction
    data object OnDecimalClick : CalculatorAction
    data object OnEqualsClick : CalculatorAction
    data object OnClearClick : CalculatorAction
    data object OnBackspaceClick : CalculatorAction
    data object OnNegateClick : CalculatorAction
    data object OnPercentClick : CalculatorAction
    data object OnHistoryClick : CalculatorAction
}

