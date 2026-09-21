package com.anujsingh.calculator.feature.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anujsingh.calculator.core.presentation.UiText
import com.anujsingh.calculator.feature.calculator.domain.repository.CalculatorRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val calculatorRepository: CalculatorRepository
) : ViewModel() {

    val state: StateFlow<HistoryState> = calculatorRepository.getHistory()
        .map { items ->
            HistoryState(
                historyItems = items,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HistoryState(isLoading = true)
        )

    private val _events = Channel<HistoryEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.OnBackClick -> {
                viewModelScope.launch { _events.send(HistoryEvent.NavigateBack) }
            }
            is HistoryAction.OnClearClick -> {
                viewModelScope.launch {
                    calculatorRepository.clearHistory()
                    _events.send(HistoryEvent.ShowSnackbar(UiText.DynamicString("History cleared")))
                }
            }
            is HistoryAction.OnItemClick -> {
                viewModelScope.launch {
                    _events.send(HistoryEvent.ShowSnackbar(UiText.DynamicString("Copied: ${action.item.result}")))
                }
            }
        }
    }
}
