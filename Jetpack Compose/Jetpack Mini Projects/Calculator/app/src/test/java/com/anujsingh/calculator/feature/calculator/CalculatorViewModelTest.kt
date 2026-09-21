package com.anujsingh.calculator.feature.calculator

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.anujsingh.calculator.feature.calculator.domain.model.CalculatorOperator
import com.anujsingh.calculator.feature.calculator.domain.usecase.EvaluateExpressionUseCase
import com.anujsingh.calculator.feature.calculator.domain.usecase.FormatNumberUseCase
import com.anujsingh.calculator.feature.calculator.presentation.CalculatorAction
import com.anujsingh.calculator.feature.calculator.presentation.CalculatorViewModel
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
    fun `typing 38670 divided by 50000 shows real-time advance preview and final result`() = runTest {
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
            val divItem = awaitItem()
            assertThat(divItem.formulaDisplay).isEqualTo("38,670÷")
            assertThat(divItem.resultDisplay).isEqualTo("38,670")

            // Type 50000 - verifying real-time advance result preview as user types!
            viewModel.onAction(CalculatorAction.OnDigitClick(5))
            val item5 = awaitItem()
            assertThat(item5.formulaDisplay).isEqualTo("38,670÷5")
            assertThat(item5.resultDisplay).isEqualTo("7,734")

            viewModel.onAction(CalculatorAction.OnDigitClick(0))
            val item50 = awaitItem()
            assertThat(item50.formulaDisplay).isEqualTo("38,670÷50")
            assertThat(item50.resultDisplay).isEqualTo("773.4")

            viewModel.onAction(CalculatorAction.OnDigitClick(0))
            val item500 = awaitItem()
            assertThat(item500.formulaDisplay).isEqualTo("38,670÷500")
            assertThat(item500.resultDisplay).isEqualTo("77.34")

            viewModel.onAction(CalculatorAction.OnDigitClick(0))
            val item5000 = awaitItem()
            assertThat(item5000.formulaDisplay).isEqualTo("38,670÷5,000")
            assertThat(item5000.resultDisplay).isEqualTo("7.734")

            viewModel.onAction(CalculatorAction.OnDigitClick(0))
            val item50000 = awaitItem()
            assertThat(item50000.formulaDisplay).isEqualTo("38,670÷50,000")
            assertThat(item50000.resultDisplay).isEqualTo("0.7734")

            // Equals (=)
            viewModel.onAction(CalculatorAction.OnEqualsClick)
            val finalState = awaitItem()
            assertThat(finalState.formulaDisplay).isEqualTo("38,670÷50,000")
            assertThat(finalState.resultDisplay).isEqualTo("0.7734")
            assertThat(finalState.isEvaluated).isTrue()
        }
    }

    @Test
    fun `supports chaining over 100 numbers with live preview and no error`() = runTest {
        viewModel.state.test {
            awaitItem() // initial

            // Chain 100 additions: 1 + 1 + 1 ...
            viewModel.onAction(CalculatorAction.OnDigitClick(1))
            awaitItem()

            for (i in 2..105) {
                viewModel.onAction(CalculatorAction.OnOperatorClick(CalculatorOperator.ADD))
                awaitItem() // after operator

                viewModel.onAction(CalculatorAction.OnDigitClick(1))
                val item = awaitItem()
                assertThat(item.resultDisplay).isEqualTo(i.toString())
            }

            viewModel.onAction(CalculatorAction.OnEqualsClick)
            val finalItem = awaitItem()
            assertThat(finalItem.resultDisplay).isEqualTo("105")
            assertThat(finalItem.isEvaluated).isTrue()
        }
    }

    @Test
    fun `trailing operator on equals evaluates cleanly without invalid expression error`() = runTest {
        viewModel.state.test {
            awaitItem() // initial

            viewModel.onAction(CalculatorAction.OnDigitClick(8))
            awaitItem()
            viewModel.onAction(CalculatorAction.OnOperatorClick(CalculatorOperator.SUBTRACT))
            awaitItem()
            viewModel.onAction(CalculatorAction.OnDigitClick(3))
            awaitItem()
            viewModel.onAction(CalculatorAction.OnOperatorClick(CalculatorOperator.SUBTRACT))
            awaitItem()

            // User clicks equals with trailing minus
            viewModel.onAction(CalculatorAction.OnEqualsClick)
            val finalItem = awaitItem()
            assertThat(finalItem.resultDisplay).isEqualTo("5")
            assertThat(finalItem.isEvaluated).isTrue()
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

    @Test
    fun `operator selection highlights operator and typing digit clears it`() = runTest {
        viewModel.state.test {
            awaitItem() // initial

            viewModel.onAction(CalculatorAction.OnDigitClick(5))
            assertThat(awaitItem().canClearOperand).isTrue()

            viewModel.onAction(CalculatorAction.OnOperatorClick(CalculatorOperator.MULTIPLY))
            val opState = awaitItem()
            assertThat(opState.selectedOperator).isEqualTo(CalculatorOperator.MULTIPLY)
            assertThat(opState.canClearOperand).isFalse()

            // Typing a new digit deactivates the operator highlight
            viewModel.onAction(CalculatorAction.OnDigitClick(2))
            val digitState = awaitItem()
            assertThat(digitState.selectedOperator).isEqualTo(null)
            assertThat(digitState.canClearOperand).isTrue()
        }
    }

    @Test
    fun `clear action toggles from C to AC and clears operand`() = runTest {
        viewModel.state.test {
            val initial = awaitItem()
            assertThat(initial.canClearOperand).isFalse()

            // Typing a number changes state so clear button becomes 'C'
            viewModel.onAction(CalculatorAction.OnDigitClick(7))
            val typed = awaitItem()
            assertThat(typed.canClearOperand).isTrue()

            // Clicking clear clears the operand and changes button back to 'AC'
            viewModel.onAction(CalculatorAction.OnClearClick)
            val cleared = awaitItem()
            assertThat(cleared.canClearOperand).isFalse()
            assertThat(cleared.resultDisplay).isEqualTo("0")
        }
    }
}

