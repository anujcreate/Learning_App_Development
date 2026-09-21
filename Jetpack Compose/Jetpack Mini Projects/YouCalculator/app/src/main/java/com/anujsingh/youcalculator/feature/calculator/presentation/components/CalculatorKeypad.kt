package com.anujsingh.youcalculator.feature.calculator.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anujsingh.youcalculator.core.designsystem.CalculatorTheme
import com.anujsingh.youcalculator.core.designsystem.components.CalculatorButton
import com.anujsingh.youcalculator.feature.calculator.domain.model.CalculatorOperator
import com.anujsingh.youcalculator.feature.calculator.presentation.CalculatorAction

@Composable
fun CalculatorKeypad(
    onAction: (CalculatorAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalculatorTheme.colors
    val buttonSpacing = 14.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(buttonSpacing)
    ) {
        // Row 1: [Backspace], [AC], [%], [÷]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(
                backgroundColor = colors.buttonTopRow,
                contentDescription = "Backspace",
                onClick = { onAction(CalculatorAction.OnBackspaceClick) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Backspace,
                    contentDescription = null,
                    tint = colors.buttonTopRowText,
                    modifier = Modifier.size(28.dp)
                )
            }

            CalculatorButton(
                backgroundColor = colors.buttonTopRow,
                contentDescription = "All Clear",
                onClick = { onAction(CalculatorAction.OnClearClick) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "AC", fontSize = 28.sp, color = colors.buttonTopRowText)
            }

            CalculatorButton(
                backgroundColor = colors.buttonTopRow,
                contentDescription = "Percent",
                onClick = { onAction(CalculatorAction.OnPercentClick) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "%", fontSize = 28.sp, color = colors.buttonTopRowText)
            }

            CalculatorButton(
                backgroundColor = colors.buttonOperator,
                contentDescription = "Divide",
                onClick = { onAction(CalculatorAction.OnOperatorClick(CalculatorOperator.DIVIDE)) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "÷", fontSize = 34.sp, color = colors.buttonOperatorText)
            }
        }

        // Row 2: [7], [8], [9], [×]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            KeypadNumberButton("7", 7, onAction, Modifier.weight(1f))
            KeypadNumberButton("8", 8, onAction, Modifier.weight(1f))
            KeypadNumberButton("9", 9, onAction, Modifier.weight(1f))
            CalculatorButton(
                backgroundColor = colors.buttonOperator,
                contentDescription = "Multiply",
                onClick = { onAction(CalculatorAction.OnOperatorClick(CalculatorOperator.MULTIPLY)) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "×", fontSize = 34.sp, color = colors.buttonOperatorText)
            }
        }

        // Row 3: [4], [5], [6], [-]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            KeypadNumberButton("4", 4, onAction, Modifier.weight(1f))
            KeypadNumberButton("5", 5, onAction, Modifier.weight(1f))
            KeypadNumberButton("6", 6, onAction, Modifier.weight(1f))
            CalculatorButton(
                backgroundColor = colors.buttonOperator,
                contentDescription = "Subtract",
                onClick = { onAction(CalculatorAction.OnOperatorClick(CalculatorOperator.SUBTRACT)) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "−", fontSize = 34.sp, color = colors.buttonOperatorText)
            }
        }

        // Row 4: [1], [2], [3], [+]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            KeypadNumberButton("1", 1, onAction, Modifier.weight(1f))
            KeypadNumberButton("2", 2, onAction, Modifier.weight(1f))
            KeypadNumberButton("3", 3, onAction, Modifier.weight(1f))
            CalculatorButton(
                backgroundColor = colors.buttonOperator,
                contentDescription = "Add",
                onClick = { onAction(CalculatorAction.OnOperatorClick(CalculatorOperator.ADD)) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "+", fontSize = 34.sp, color = colors.buttonOperatorText)
            }
        }

        // Row 5: [+/-], [0], [.], [=]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(
                backgroundColor = colors.buttonNumber,
                contentDescription = "Plus Minus Negate",
                onClick = { onAction(CalculatorAction.OnNegateClick) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "+/-", fontSize = 26.sp, color = colors.buttonNumberText)
            }

            KeypadNumberButton("0", 0, onAction, Modifier.weight(1f))

            CalculatorButton(
                backgroundColor = colors.buttonNumber,
                contentDescription = "Decimal point",
                onClick = { onAction(CalculatorAction.OnDecimalClick) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = ".", fontSize = 32.sp, color = colors.buttonNumberText)
            }

            CalculatorButton(
                backgroundColor = colors.buttonOperator,
                contentDescription = "Equals",
                onClick = { onAction(CalculatorAction.OnEqualsClick) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "=", fontSize = 34.sp, color = colors.buttonOperatorText)
            }
        }
    }
}

@Composable
private fun KeypadNumberButton(
    text: String,
    digit: Int,
    onAction: (CalculatorAction) -> Unit,
    modifier: Modifier = Modifier
) {
    CalculatorButton(
        backgroundColor = CalculatorTheme.colors.buttonNumber,
        contentDescription = "Digit $text",
        onClick = { onAction(CalculatorAction.OnDigitClick(digit)) },
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            color = CalculatorTheme.colors.buttonNumberText
        )
    }
}
