package com.anujsingh.youcalculator.feature.calculator

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.anujsingh.youcalculator.feature.calculator.domain.model.CalculatorOperator
import com.anujsingh.youcalculator.feature.calculator.domain.usecase.EvaluateExpressionUseCase
import com.anujsingh.youcalculator.feature.calculator.domain.usecase.FormatNumberUseCase
import com.anujsingh.youcalculator.feature.calculator.presentation.CalculatorAction
import com.anujsingh.youcalculator.feature.calculator.presentation.CalculatorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class CalculatorViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: CalculatorViewModel
    private lateinit var repository: FakeCalculatorRepository

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeCalculatorRepository()
        viewModel = CalculatorViewModel(
            savedStateHandle = SavedStateHandle(),
            evaluateExpressionUseCase = EvaluateExpressionUseCase(),
            formatNumberUseCase = FormatNumberUseCase(Locale.US),
            calculatorRepository = repository
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `typing 38670 divided by 50000 and equals outputs 0,7734 matching screenshot`() = runTest {
        viewModel.state.test {
            // Initial state
            assertThat(awaitItem().resultDisplay).isEqualTo("0")

            // Type 38670
            viewModel.onAction(CalculatorAction.OnDigitClick(3))
            assertThat(awaitItem().resultDisplay).isEqualTo("3")
            viewModel.onAction(CalculatorAction.OnDigitClick(8))
            assertThat(awaitItem().resultDisplay).isEqualTo("38")
            viewModel.onAction(CalculatorAction.OnDigitClick(6))
            assertThat(awaitItem().resultDisplay).isEqualTo("386")
            viewModel.onAction(CalculatorAction.OnDigitClick(7))
            assertThat(awaitItem().resultDisplay).isEqualTo("3,867")
            viewModel.onAction(CalculatorAction.OnDigitClick(0))
            assertThat(awaitItem().resultDisplay).isEqualTo("38,670")

            // Operator Divide (÷)
            viewModel.onAction(CalculatorAction.OnOperatorClick(CalculatorOperator.DIVIDE))
            assertThat(awaitItem().formulaDisplay).isEqualTo("38,670÷")

            // Type 50000
            viewModel.onAction(CalculatorAction.OnDigitClick(5))
            assertThat(awaitItem().resultDisplay).isEqualTo("5")
            viewModel.onAction(CalculatorAction.OnDigitClick(0))
            assertThat(awaitItem().resultDisplay).isEqualTo("50")
            viewModel.onAction(CalculatorAction.OnDigitClick(0))
            assertThat(awaitItem().resultDisplay).isEqualTo("500")
            viewModel.onAction(CalculatorAction.OnDigitClick(0))
            assertThat(awaitItem().resultDisplay).isEqualTo("5,000")
            viewModel.onAction(CalculatorAction.OnDigitClick(0))
            assertThat(awaitItem().resultDisplay).isEqualTo("50,000")

            // Equals (=)
            viewModel.onAction(CalculatorAction.OnEqualsClick)
            val finalState = awaitItem()
            assertThat(finalState.formulaDisplay).isEqualTo("38,670÷50,000")
            assertThat(finalState.resultDisplay).isEqualTo("0.7734")
            assertThat(finalState.isEvaluated).isTrue()
        }
    }

    @Test
    fun `clear action resets state to initial`() = runTest {
        viewModel.state.test {
            awaitItem() // initial
            viewModel.onAction(CalculatorAction.OnDigitClick(9))
            assertThat(awaitItem().resultDisplay).isEqualTo("9")

            viewModel.onAction(CalculatorAction.OnClearClick)
            val cleared = awaitItem()
            assertThat(cleared.resultDisplay).isEqualTo("0")
            assertThat(cleared.formulaDisplay).isEqualTo("")
            assertThat(cleared.isEvaluated).isFalse()
        }
    }
}
