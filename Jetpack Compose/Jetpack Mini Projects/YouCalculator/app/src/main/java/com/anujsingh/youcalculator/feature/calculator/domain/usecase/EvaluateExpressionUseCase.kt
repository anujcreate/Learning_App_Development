package com.anujsingh.youcalculator.feature.calculator.domain.usecase

import com.anujsingh.youcalculator.core.domain.Result
import com.anujsingh.youcalculator.feature.calculator.domain.error.CalculatorError
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class EvaluateExpressionUseCase {

    private val mathContext = MathContext(16, RoundingMode.HALF_UP)

    operator fun invoke(expression: String): Result<BigDecimal, CalculatorError> {
        if (expression.isBlank()) {
            return Result.Error(CalculatorError.INVALID_EXPRESSION)
        }

        return try {
            val tokens = tokenize(expression)
            if (tokens.isEmpty()) {
                return Result.Error(CalculatorError.INVALID_EXPRESSION)
            }
            evaluateTokens(tokens)
        } catch (_: ArithmeticException) {
            Result.Error(CalculatorError.DIVIDE_BY_ZERO)
        } catch (_: Exception) {
            Result.Error(CalculatorError.INVALID_EXPRESSION)
        }
    }

    private fun tokenize(expr: String): List<String> {
        val sanitized = expr.replace(" ", "")
        val tokens = mutableListOf<String>()
        val currentNumber = StringBuilder()

        var i = 0
        while (i < sanitized.length) {
            val c = sanitized[i]
            when {
                c.isDigit() || c == '.' -> currentNumber.append(c)
                c == '-' && (i == 0 || isOperator(sanitized[i - 1])) -> {
                    // Unary minus
                    currentNumber.append(c)
                }
                c == '%' -> {
                    if (currentNumber.isNotEmpty()) {
                        tokens.add(currentNumber.toString())
                        currentNumber.clear()
                    }
                    tokens.add("%")
                }
                isOperator(c) -> {
                    if (currentNumber.isNotEmpty()) {
                        tokens.add(currentNumber.toString())
                        currentNumber.clear()
                    }
                    tokens.add(c.toString())
                }
            }
            i++
        }

        if (currentNumber.isNotEmpty()) {
            tokens.add(currentNumber.toString())
        }

        return tokens
    }

    private fun isOperator(c: Char): Boolean = c == '+' || c == '-' || c == '×' || c == '÷'

    private fun evaluateTokens(tokens: List<String>): Result<BigDecimal, CalculatorError> {
        // Step 1: Process percentages (e.g. 50% -> 0.5; 200 + 10% -> 200 + 20)
        val tokensAfterPercent = mutableListOf<String>()
        var idx = 0
        while (idx < tokens.size) {
            val token = tokens[idx]
            if (token == "%") {
                if (tokensAfterPercent.isNotEmpty()) {
                    val prevIndex = tokensAfterPercent.lastIndex
                    val prevVal = BigDecimal(tokensAfterPercent[prevIndex])
                    val percentVal = prevVal.divide(BigDecimal(100), mathContext)

                    // Contextual percent: A + B% => A + (A * B / 100)
                    if (tokensAfterPercent.size >= 3) {
                        val op = tokensAfterPercent[prevIndex - 1]
                        val baseVal = BigDecimal(tokensAfterPercent[prevIndex - 2])
                        if (op == "+" || op == "-") {
                            val contextual = baseVal.multiply(percentVal, mathContext)
                            tokensAfterPercent[prevIndex] = contextual.toPlainString()
                        } else {
                            tokensAfterPercent[prevIndex] = percentVal.toPlainString()
                        }
                    } else {
                        tokensAfterPercent[prevIndex] = percentVal.toPlainString()
                    }
                }
            } else {
                tokensAfterPercent.add(token)
            }
            idx++
        }

        // Step 2: High precedence (×, ÷)
        val tokensAfterMulDiv = mutableListOf<String>()
        var i = 0
        while (i < tokensAfterPercent.size) {
            val token = tokensAfterPercent[i]
            if (token == "×" || token == "÷") {
                if (tokensAfterMulDiv.isEmpty() || i + 1 >= tokensAfterPercent.size) {
                    return Result.Error(CalculatorError.INVALID_EXPRESSION)
                }
                val left = BigDecimal(tokensAfterMulDiv.removeAt(tokensAfterMulDiv.lastIndex))
                val right = BigDecimal(tokensAfterPercent[i + 1])
                val res = if (token == "×") {
                    left.multiply(right, mathContext)
                } else {
                    if (right.compareTo(BigDecimal.ZERO) == 0) {
                        return Result.Error(CalculatorError.DIVIDE_BY_ZERO)
                    }
                    left.divide(right, mathContext)
                }
                tokensAfterMulDiv.add(res.toPlainString())
                i += 2
            } else {
                tokensAfterMulDiv.add(token)
                i++
            }
        }

        // Step 3: Low precedence (+, -)
        if (tokensAfterMulDiv.isEmpty()) return Result.Error(CalculatorError.INVALID_EXPRESSION)
        var result = BigDecimal(tokensAfterMulDiv[0])
        var j = 1
        while (j < tokensAfterMulDiv.size) {
            val op = tokensAfterMulDiv[j]
            if (j + 1 >= tokensAfterMulDiv.size) return Result.Error(CalculatorError.INVALID_EXPRESSION)
            val right = BigDecimal(tokensAfterMulDiv[j + 1])
            result = if (op == "+") {
                result.add(right, mathContext)
            } else {
                result.subtract(right, mathContext)
            }
            j += 2
        }

        return Result.Success(result)
    }
}
