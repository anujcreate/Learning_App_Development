package com.anujsingh.youcalculator.feature.calculator.data.mapper

import com.anujsingh.youcalculator.feature.calculator.data.database.entity.CalculationHistoryEntity
import com.anujsingh.youcalculator.feature.calculator.domain.model.CalculationHistory

fun CalculationHistoryEntity.toCalculationHistory(): CalculationHistory =
    CalculationHistory(
        id = id,
        expression = expression,
        result = result,
        timestamp = timestamp
    )

fun CalculationHistory.toEntity(): CalculationHistoryEntity =
    CalculationHistoryEntity(
        id = id,
        expression = expression,
        result = result,
        timestamp = timestamp
    )
