package com.anujsingh.youcalculator.feature.calculator.domain.model

data class CalculationHistory(
    val id: Long = 0,
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)