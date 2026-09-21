package com.anujsingh.calculator.feature.calculator

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.anujsingh.calculator.core.domain.Result
import com.anujsingh.calculator.feature.calculator.domain.error.CalculatorError
import com.anujsingh.calculator.feature.calculator.domain.usecase.EvaluateExpressionUseCase
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class EvaluateExpressionUseCaseTest {

    private val evaluateExpression = EvaluateExpressionUseCase()

    @Test
    fun `evaluates basic addition correctly`() {
        val result = evaluateExpression("12+8")
        assertThat(result).isInstanceOf(Result.Success::class)
        assertThat((result as Result.Success).data.compareTo(BigDecimal("20"))).isEqualTo(0)
    }

    @Test
    fun `evaluates operator precedence correctly`() {
        val result = evaluateExpression("2+3×4")
        assertThat(result).isInstanceOf(Result.Success::class)
        assertThat((result as Result.Success).data.compareTo(BigDecimal("14"))).isEqualTo(0)
    }

    @Test
    fun `evaluates division by zero returns DivideByZero error`() {
        val result = evaluateExpression("10÷0")
        assertThat(result).isInstanceOf(Result.Error::class)
        assertThat((result as Result.Error).error).isEqualTo(CalculatorError.DIVIDE_BY_ZERO)
    }

    @Test
    fun `evaluates percent operation correctly`() {
        // 200 + 10% = 220
        val result = evaluateExpression("200+10%")
        assertThat(result).isInstanceOf(Result.Success::class)
        assertThat((result as Result.Success).data.compareTo(BigDecimal("220"))).isEqualTo(0)
    }

    @Test
    fun `evaluates 150 chained additions without limit or error`() {
        // 1 + 1 + 1 + ... (150 ones) = 150
        val expr = (1..150).joinToString("+") { "1" }
        val result = evaluateExpression(expr)
        assertThat(result).isInstanceOf(Result.Success::class)
        assertThat((result as Result.Success).data.compareTo(BigDecimal("150"))).isEqualTo(0)
    }

    @Test
    fun `evaluates 100 chained subtractions matching user scenario`() {
        // 1000 - 5 - 5 - 5 ... (100 times) = 500
        val expr = "1000" + "-5".repeat(100)
        val result = evaluateExpression(expr)
        assertThat(result).isInstanceOf(Result.Success::class)
        assertThat((result as Result.Success).data.compareTo(BigDecimal("500"))).isEqualTo(0)
    }

    @Test
    fun `evaluates long expression with commas and spaces`() {
        val expr = "360,000 - 53 - 55 - 64 - 78 - 89"
        val result = evaluateExpression(expr)
        assertThat(result).isInstanceOf(Result.Success::class)
        assertThat((result as Result.Success).data.compareTo(BigDecimal("359661"))).isEqualTo(0)
    }
}
