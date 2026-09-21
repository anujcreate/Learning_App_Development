package com.anujsingh.calculator.feature.history.presentation

import androidx.compose.runtime.Immutable
import com.anujsingh.calculator.feature.calculator.domain.model.CalculationHistory

@Immutable
data class HistoryState(
    val historyItems: List<CalculationHistory> = emptyList(),
    val isLoading: Boolean = false
)
