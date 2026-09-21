package com.anujsingh.calculator.feature.calculator

import com.anujsingh.calculator.core.domain.DataError
import com.anujsingh.calculator.core.domain.EmptyResult
import com.anujsingh.calculator.core.domain.Result
import com.anujsingh.calculator.feature.calculator.domain.model.CalculationHistory
import com.anujsingh.calculator.feature.calculator.domain.repository.CalculatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeCalculatorRepository : CalculatorRepository {
    private val historyList = mutableListOf<CalculationHistory>()
    private val historyFlow = MutableStateFlow<List<CalculationHistory>>(emptyList())
    var shouldReturnError = false

    override fun getHistory(): Flow<List<CalculationHistory>> = historyFlow.asStateFlow()

    override suspend fun saveCalculation(history: CalculationHistory): EmptyResult<DataError.Local> {
        if (shouldReturnError) return Result.Error(DataError.Local.UNKNOWN)
        historyList.add(history)
        historyFlow.value = historyList.toList()
        return Result.Success(Unit)
    }

    override suspend fun clearHistory(): EmptyResult<DataError.Local> {
        if (shouldReturnError) return Result.Error(DataError.Local.UNKNOWN)
        historyList.clear()
        historyFlow.value = emptyList()
        return Result.Success(Unit)
    }
}
