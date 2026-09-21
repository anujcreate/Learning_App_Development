package com.anujsingh.calculator.feature.history.presentation

import com.anujsingh.calculator.core.presentation.UiText

sealed interface HistoryEvent {
    data object NavigateBack : HistoryEvent
    data class ShowSnackbar(val message: UiText) : HistoryEvent
}
