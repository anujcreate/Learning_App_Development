package com.anujsingh.calculator.feature.calculator.presentation

import com.anujsingh.calculator.core.presentation.UiText

sealed interface CalculatorEvent {
    data object NavigateToHistory : CalculatorEvent
    data class ShowSnackbar(val message: UiText) : CalculatorEvent
}

