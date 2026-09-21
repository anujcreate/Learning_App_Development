package com.anujsingh.youcalculator.feature.calculator.domain.repository

import com.anujsingh.youcalculator.core.domain.DataError
import com.anujsingh.youcalculator.core.domain.EmptyResult
import com.anujsingh.youcalculator.feature.calculator.domain.model.CalculationHistory
import kotlinx.coroutines.flow.Flow

interface CalculatorRepository {
    fun getHistory(): Flow<List<CalculationHistory>>
    suspend fun saveCalculation(history: CalculationHistory): EmptyResult<DataError.Local>
    suspend fun clearHistory(): EmptyResult<DataError.Local>
}