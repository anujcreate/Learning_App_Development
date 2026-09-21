package com.anujsingh.calculator.feature.calculator.domain.repository

import com.anujsingh.calculator.core.domain.DataError
import com.anujsingh.calculator.core.domain.EmptyResult
import com.anujsingh.calculator.feature.calculator.domain.model.CalculationHistory
import kotlinx.coroutines.flow.Flow

interface CalculatorRepository {
    fun getHistory(): Flow<List<CalculationHistory>>
    suspend fun saveCalculation(history: CalculationHistory): EmptyResult<DataError.Local>
    suspend fun clearHistory(): EmptyResult<DataError.Local>
}