package com.anujsingh.youcalculator.feature.calculator.data.datasource

import com.anujsingh.youcalculator.core.domain.DataError
import com.anujsingh.youcalculator.core.domain.EmptyResult
import com.anujsingh.youcalculator.core.domain.Result
import com.anujsingh.youcalculator.feature.calculator.data.database.CalculatorDao
import com.anujsingh.youcalculator.feature.calculator.data.mapper.toCalculationHistory
import com.anujsingh.youcalculator.feature.calculator.data.mapper.toEntity
import com.anujsingh.youcalculator.feature.calculator.domain.model.CalculationHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCalculatorDataSource(
    private val dao: CalculatorDao
) : CalculatorLocalDataSource {

    override fun getHistory(): Flow<List<CalculationHistory>> {
        return dao.getAllHistory().map { list ->
            list.map { it.toCalculationHistory() }
        }
    }

    override suspend fun insert(history: CalculationHistory): EmptyResult<DataError.Local> {
        return try {
            dao.insert(history.toEntity())
            Result.Success(Unit)
        } catch (_: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun clear(): EmptyResult<DataError.Local> {
        return try {
            dao.clearAll()
            Result.Success(Unit)
        } catch (_: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
