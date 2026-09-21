package com.anujsingh.youcalculator.feature.history.presentation

import com.anujsingh.youcalculator.feature.calculator.domain.model.CalculationHistory

sealed interface HistoryAction {
    data object OnBackClick : HistoryAction
    data object OnClearClick : HistoryAction
    data class OnItemClick(val item: CalculationHistory) : HistoryAction
}
