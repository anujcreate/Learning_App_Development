package com.anujsingh.youcalculator.feature.history.presentation

import androidx.compose.runtime.Immutable
import com.anujsingh.youcalculator.feature.calculator.domain.model.CalculationHistory

@Immutable
data class HistoryState(
    val historyItems: List<CalculationHistory> = emptyList(),
    val isLoading: Boolean = false
)
