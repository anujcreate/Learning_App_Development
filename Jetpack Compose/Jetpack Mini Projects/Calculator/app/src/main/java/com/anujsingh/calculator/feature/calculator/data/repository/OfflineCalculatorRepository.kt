package com.anujsingh.calculator.feature.calculator.data.repository

import com.anujsingh.calculator.core.domain.DataError
import com.anujsingh.calculator.core.domain.EmptyResult
import com.anujsingh.calculator.feature.calculator.data.datasource.CalculatorLocalDataSource
import com.anujsingh.calculator.feature.calculator.domain.model.CalculationHistory
import com.anujsingh.calculator.feature.calculator.domain.repository.CalculatorRepository
import kotlinx.coroutines.flow.Flow

class OfflineCalculatorRepository(
    private val localDataSource: CalculatorLocalDataSource
) : CalculatorRepository {

    override fun getHistory(): Flow<List<CalculationHistory>> {
        return localDataSource.getHistory()
    }

    override suspend fun saveCalculation(history: CalculationHistory): EmptyResult<DataError.Local> {
        return localDataSource.insert(history)
    }

    override suspend fun clearHistory(): EmptyResult<DataError.Local> {
        return localDataSource.clear()
    }
}
