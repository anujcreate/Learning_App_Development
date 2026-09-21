package com.anujsingh.calculator.feature.calculator.domain.error

import com.anujsingh.calculator.core.domain.Error

enum class CalculatorError : Error {
    DIVIDE_BY_ZERO,
    INVALID_EXPRESSION,
    OVERFLOW,
    UNKNOWN
}