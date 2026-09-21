package com.anujsingh.calculator.feature.calculator.data.datasource

import com.anujsingh.calculator.core.domain.DataError
import com.anujsingh.calculator.core.domain.EmptyResult
import com.anujsingh.calculator.feature.calculator.domain.model.CalculationHistory
import kotlinx.coroutines.flow.Flow

interface CalculatorLocalDataSource {
    fun getHistory(): Flow<List<CalculationHistory>>
    suspend fun insert(history: CalculationHistory): EmptyResult<DataError.Local>
    suspend fun clear(): EmptyResult<DataError.Local>
}
