package com.anujsingh.youcalculator.feature.calculator

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.anujsingh.youcalculator.core.designsystem.CalculatorTheme
import com.anujsingh.youcalculator.feature.calculator.presentation.CalculatorAction
import com.anujsingh.youcalculator.feature.calculator.presentation.CalculatorScreen
import com.anujsingh.youcalculator.feature.calculator.presentation.CalculatorState
import org.junit.Rule
import org.junit.Test

class CalculatorRobot(private val composeTestRule: ComposeContentTestRule) {
    fun setContent(
        state: CalculatorState,
        darkTheme: Boolean = true,
        onAction: (CalculatorAction) -> Unit = {}
    ) = apply {
        composeTestRule.setContent {
            CalculatorTheme(darkTheme = darkTheme) {
                CalculatorScreen(state = state, onAction = onAction)
            }
        }
    }

    fun assertResultVisible(text: String) = apply {
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    fun assertFormulaVisible(text: String) = apply {
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    fun clickDigit(digit: Int) = apply {
        composeTestRule.onNodeWithContentDescription("Digit $digit").performClick()
    }

    fun clickOperator(operator: String) = apply {
        composeTestRule.onNodeWithContentDescription(operator).performClick()
    }

    fun clickEquals() = apply {
        composeTestRule.onNodeWithContentDescription("Equals").performClick()
    }
}

class CalculatorScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val robot by lazy { CalculatorRobot(composeTestRule) }

    @Test
    fun displaysFormulaAndResultCorrectly_InDarkMode() {
        robot
            .setContent(
                state = CalculatorState(
                    formulaDisplay = "38,670÷50,000",
                    resultDisplay = "0.7734"
                ),
                darkTheme = true
            )
            .assertFormulaVisible("38,670÷50,000")
            .assertResultVisible("0.7734")
    }

    @Test
    fun displaysFormulaAndResultCorrectly_InLightMode() {
        robot
            .setContent(
                state = CalculatorState(
                    formulaDisplay = "38,670÷50,000",
                    resultDisplay = "0.7734"
                ),
                darkTheme = false
            )
            .assertFormulaVisible("38,670÷50,000")
            .assertResultVisible("0.7734")
    }
}
