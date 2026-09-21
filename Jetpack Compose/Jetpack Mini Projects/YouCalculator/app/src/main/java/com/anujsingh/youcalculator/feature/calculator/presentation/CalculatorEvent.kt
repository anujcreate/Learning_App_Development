package com.anujsingh.youcalculator.feature.calculator.presentation

import com.anujsingh.youcalculator.core.presentation.UiText

sealed interface CalculatorEvent {
    data object NavigateToHistory : CalculatorEvent
    data class ShowSnackbar(val message: UiText) : CalculatorEvent
}

