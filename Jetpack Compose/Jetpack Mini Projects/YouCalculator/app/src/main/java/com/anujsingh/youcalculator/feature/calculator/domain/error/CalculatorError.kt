package com.anujsingh.youcalculator.feature.calculator.domain.error

import com.anujsingh.youcalculator.core.domain.Error

enum class CalculatorError : Error {
    DIVIDE_BY_ZERO,
    INVALID_EXPRESSION,
    OVERFLOW,
    UNKNOWN
}