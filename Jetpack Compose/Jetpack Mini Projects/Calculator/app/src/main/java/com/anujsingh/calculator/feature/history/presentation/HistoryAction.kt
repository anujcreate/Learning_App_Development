package com.anujsingh.calculator.feature.history.presentation

import com.anujsingh.calculator.feature.calculator.domain.model.CalculationHistory

sealed interface HistoryAction {
    data object OnBackClick : HistoryAction
    data object OnClearClick : HistoryAction
    data class OnItemClick(val item: CalculationHistory) : HistoryAction
}
