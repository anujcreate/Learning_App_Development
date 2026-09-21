package com.anujsingh.youcalculator.feature.calculator.domain.model

enum class CalculatorOperator(val symbol: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("×"),
    DIVIDE("÷");

    companion object {
        fun fromSymbol(symbol: String): CalculatorOperator? {
            return entries.find { it.symbol == symbol }
        }
    }
}