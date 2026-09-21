package com.anujsingh.youcalculator.feature.history.presentation

import com.anujsingh.youcalculator.core.presentation.UiText

sealed interface HistoryEvent {
    data object NavigateBack : HistoryEvent
    data class ShowSnackbar(val message: UiText) : HistoryEvent
}
