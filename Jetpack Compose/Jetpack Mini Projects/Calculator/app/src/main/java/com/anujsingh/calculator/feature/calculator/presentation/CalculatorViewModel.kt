package com.anujsingh.calculator.feature.calculator.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anujsingh.calculator.core.domain.Result
import com.anujsingh.calculator.core.domain.onFailure
import com.anujsingh.calculator.core.domain.onSuccess
import com.anujsingh.calculator.core.presentation.UiText
import com.anujsingh.calculator.feature.calculator.domain.error.CalculatorError
import com.anujsingh.calculator.feature.calculator.domain.model.CalculationHistory
import com.anujsingh.calculator.feature.calculator.domain.model.CalculatorOperator
import com.anujsingh.calculator.feature.calculator.domain.repository.CalculatorRepository
import com.anujsingh.calculator.feature.calculator.domain.usecase.EvaluateExpressionUseCase
import com.anujsingh.calculator.feature.calculator.domain.usecase.FormatNumberUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val evaluateExpressionUseCase: EvaluateExpressionUseCase,
    private val formatNumberUseCase: FormatNumberUseCase,
    private val calculatorRepository: CalculatorRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        CalculatorState(
            formulaDisplay = savedStateHandle["formula"] ?: "",
            resultDisplay = savedStateHandle["result"] ?: "0",
            canClearOperand = (savedStateHandle["currentOperand"] as? String)?.isNotEmpty() == true
        )
    )
    val state = _state.asStateFlow()

    private val _events = Channel<CalculatorEvent>()
    val events = _events.receiveAsFlow()

    // Internal raw expressions without thousands grouping for precise calculations
    private var rawExpression: String = savedStateHandle["rawExpression"] ?: ""
    private var currentOperand: String = savedStateHandle["currentOperand"] ?: ""

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.OnDigitClick -> handleDigit(action.digit)
            is CalculatorAction.OnOperatorClick -> handleOperator(action.operator)
            is CalculatorAction.OnDecimalClick -> handleDecimal()
            is CalculatorAction.OnEqualsClick -> handleEquals()
            is CalculatorAction.OnClearClick -> handleClear()
            is CalculatorAction.OnBackspaceClick -> handleBackspace()
            is CalculatorAction.OnNegateClick -> handleNegate()
            is CalculatorAction.OnPercentClick -> handlePercent()
            is CalculatorAction.OnHistoryClick -> {
                viewModelScope.launch { _events.send(CalculatorEvent.NavigateToHistory) }
            }
        }
    }


    private fun handleDigit(digit: Int) {
        if (_state.value.isEvaluated) {
            rawExpression = ""
            currentOperand = digit.toString()
            _state.update {
                it.copy(
                    formulaDisplay = "",
                    resultDisplay = currentOperand,
                    isEvaluated = false,
                    selectedOperator = null,
                    canClearOperand = true
                )
            }
        } else {
            if (currentOperand == "0") {
                currentOperand = digit.toString()
            } else {
                currentOperand += digit
            }
            updateDisplays(selectedOperator = null, isEvaluated = false)
        }
        persistState()
    }

    private fun handleDecimal() {
        val sep = formatNumberUseCase.decimalSeparator
        if (_state.value.isEvaluated) {
            rawExpression = ""
            currentOperand = "0$sep"
            _state.update {
                it.copy(
                    formulaDisplay = "",
                    resultDisplay = currentOperand,
                    isEvaluated = false,
                    selectedOperator = null,
                    canClearOperand = true
                )
            }
        } else {
            if (!currentOperand.contains(sep)) {
                currentOperand = if (currentOperand.isEmpty()) "0$sep" else "$currentOperand$sep"
                updateDisplays(selectedOperator = null, isEvaluated = false)
            }
        }
        persistState()
    }

    private fun handleOperator(operator: CalculatorOperator) {
        if (currentOperand.isNotEmpty()) {
            val parsable = formatNumberUseCase.cleanToParsable(currentOperand)
            rawExpression += parsable
            currentOperand = ""
        }

        if (rawExpression.isNotEmpty()) {
            val lastChar = rawExpression.last().toString()
            if (CalculatorOperator.fromSymbol(lastChar) != null) {
                rawExpression = rawExpression.dropLast(1) + operator.symbol
            } else {
                rawExpression += operator.symbol
            }
        } else if (operator == CalculatorOperator.SUBTRACT) {
            currentOperand = "-"
            updateDisplays(selectedOperator = null, isEvaluated = false)
            persistState()
            return
        }

        updateDisplays(selectedOperator = operator, isEvaluated = false)
        persistState()
    }

    private fun handleEquals() {
        if (currentOperand.isNotEmpty()) {
            val parsable = formatNumberUseCase.cleanToParsable(currentOperand)
            rawExpression += parsable
            currentOperand = ""
        }

        if (rawExpression.isEmpty()) return

        // Strip any trailing operators before evaluating
        while (rawExpression.isNotEmpty() && CalculatorOperator.fromSymbol(rawExpression.last().toString()) != null) {
            rawExpression = rawExpression.dropLast(1)
        }

        if (rawExpression.isEmpty()) return

        val fullFormulaDisplay = formatFormula(rawExpression)
        val parsableExpression = formatNumberUseCase.cleanToParsable(rawExpression)

        evaluateExpressionUseCase(parsableExpression)
            .onSuccess { result ->
                val formattedResult = formatNumberUseCase.formatResult(result)
                _state.update {
                    it.copy(
                        formulaDisplay = fullFormulaDisplay,
                        resultDisplay = formattedResult,
                        isEvaluated = true,
                        selectedOperator = null,
                        canClearOperand = false,
                        error = null
                    )
                }

                // Save to Room DB
                viewModelScope.launch {
                    calculatorRepository.saveCalculation(
                        CalculationHistory(
                            expression = fullFormulaDisplay,
                            result = formattedResult
                        )
                    )
                }

                rawExpression = result.toPlainString()
                currentOperand = ""
                persistState()
            }
            .onFailure { error ->
                val errorMsg = when (error) {
                    CalculatorError.DIVIDE_BY_ZERO -> UiText.DynamicString("Cannot divide by zero")
                    CalculatorError.INVALID_EXPRESSION -> UiText.DynamicString("Invalid expression")
                    else -> UiText.DynamicString("Error")
                }
                _state.update {
                    it.copy(
                        resultDisplay = "Error",
                        selectedOperator = null,
                        error = errorMsg
                    )
                }
                viewModelScope.launch {
                    _events.send(CalculatorEvent.ShowSnackbar(errorMsg))
                }
            }
    }

    private fun handleClear() {
        if (currentOperand.isNotEmpty()) {
            currentOperand = ""
            updateDisplays(selectedOperator = null, isEvaluated = false)
        } else {
            rawExpression = ""
            currentOperand = ""
            _state.update {
                CalculatorState(
                    formulaDisplay = "",
                    resultDisplay = "0",
                    isEvaluated = false,
                    selectedOperator = null,
                    canClearOperand = false
                )
            }
        }
        persistState()
    }

    private fun handleBackspace() {
        if (_state.value.isEvaluated) {
            handleClear()
            return
        }

        if (currentOperand.isNotEmpty()) {
            currentOperand = currentOperand.dropLast(1)
            updateDisplays(isEvaluated = false)
        } else if (rawExpression.isNotEmpty()) {
            rawExpression = rawExpression.dropLast(1)
            extractTrailingOperandIfNeeded()
            updateDisplays(selectedOperator = null, isEvaluated = false)
        }
        persistState()
    }

    private fun extractTrailingOperandIfNeeded() {
        if (rawExpression.isEmpty() || currentOperand.isNotEmpty()) return

        val lastChar = rawExpression.last().toString()
        if (CalculatorOperator.fromSymbol(lastChar) != null || lastChar == "%") return

        var lastOpIdx = -1
        for (i in rawExpression.indices) {
            val c = rawExpression[i]
            if ((c == '+' || c == '-' || c == '×' || c == '÷') && i > 0 && CalculatorOperator.fromSymbol(rawExpression[i - 1].toString()) == null) {
                lastOpIdx = i
            }
        }

        if (lastOpIdx != -1) {
            currentOperand = rawExpression.substring(lastOpIdx + 1)
            rawExpression = rawExpression.substring(0, lastOpIdx + 1)
        } else {
            currentOperand = rawExpression
            rawExpression = ""
        }
    }

    private fun handleNegate() {
        if (currentOperand.isNotEmpty()) {
            currentOperand = if (currentOperand.startsWith("-")) {
                currentOperand.substring(1)
            } else {
                "-$currentOperand"
            }
            updateDisplays(selectedOperator = null, isEvaluated = false)
        } else if (_state.value.resultDisplay != "0" && _state.value.isEvaluated) {
            val currentRes = _state.value.resultDisplay
            val negated = if (currentRes.startsWith("-")) currentRes.substring(1) else "-$currentRes"
            currentOperand = negated
            rawExpression = ""
            _state.update {
                it.copy(
                    resultDisplay = negated,
                    isEvaluated = false,
                    selectedOperator = null,
                    canClearOperand = true
                )
            }
        }
        persistState()
    }

    private fun handlePercent() {
        if (currentOperand.isNotEmpty()) {
            val parsable = formatNumberUseCase.cleanToParsable(currentOperand)
            rawExpression += "$parsable%"
            currentOperand = ""
            updateDisplays(selectedOperator = null, isEvaluated = false)
        }
        persistState()
    }

    private fun updateDisplays(
        selectedOperator: CalculatorOperator? = _state.value.selectedOperator,
        isEvaluated: Boolean = _state.value.isEvaluated
    ) {
        val canClear = currentOperand.isNotEmpty()
        if (rawExpression.isEmpty()) {
            val formattedCurrent = if (currentOperand.isEmpty()) "0" else formatNumberUseCase.format(currentOperand)
            _state.update {
                it.copy(
                    formulaDisplay = "",
                    resultDisplay = formattedCurrent,
                    isEvaluated = isEvaluated,
                    selectedOperator = selectedOperator,
                    canClearOperand = canClear,
                    error = null
                )
            }
        } else {
            val parsableOperand = if (currentOperand.isNotEmpty()) formatNumberUseCase.cleanToParsable(currentOperand) else ""
            val activeRaw = rawExpression + parsableOperand
            val activeFormulaDisplay = formatFormula(activeRaw)

            // Real-time advance result preview
            val parsableExpression = formatNumberUseCase.cleanToParsable(activeRaw)
            val previewResult = evaluateExpressionUseCase(parsableExpression)

            val displayResult = when (previewResult) {
                is Result.Success -> formatNumberUseCase.formatResult(previewResult.data)
                is Result.Error -> {
                    if (currentOperand.isNotEmpty()) {
                        formatNumberUseCase.format(currentOperand)
                    } else {
                        _state.value.resultDisplay
                    }
                }
            }

            _state.update {
                it.copy(
                    formulaDisplay = activeFormulaDisplay,
                    resultDisplay = displayResult,
                    isEvaluated = isEvaluated,
                    selectedOperator = selectedOperator,
                    canClearOperand = canClear,
                    error = null
                )
            }
        }
    }

    private fun formatFormula(raw: String): String {
        val regex = "([0-9.,]+)".toRegex()
        return regex.replace(raw) { matchResult ->
            formatNumberUseCase.format(matchResult.value)
        }
    }

    private fun persistState() {
        savedStateHandle["formula"] = _state.value.formulaDisplay
        savedStateHandle["result"] = _state.value.resultDisplay
        savedStateHandle["rawExpression"] = rawExpression
        savedStateHandle["currentOperand"] = currentOperand
    }
}
