package com.anujsingh.youcalculator.feature.calculator.data.datasource

import com.anujsingh.youcalculator.core.domain.DataError
import com.anujsingh.youcalculator.core.domain.EmptyResult
import com.anujsingh.youcalculator.feature.calculator.domain.model.CalculationHistory
import kotlinx.coroutines.flow.Flow

interface CalculatorLocalDataSource {
    fun getHistory(): Flow<List<CalculationHistory>>
    suspend fun insert(history: CalculationHistory): EmptyResult<DataError.Local>
    suspend fun clear(): EmptyResult<DataError.Local>
}
