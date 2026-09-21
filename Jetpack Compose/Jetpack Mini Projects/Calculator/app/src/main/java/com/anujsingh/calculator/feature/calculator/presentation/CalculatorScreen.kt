package com.anujsingh.calculator.feature.calculator.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anujsingh.calculator.core.designsystem.CalculatorTheme
import com.anujsingh.calculator.core.designsystem.components.TopBarIconButton
import com.anujsingh.calculator.core.presentation.ObserveAsEvents
import com.anujsingh.calculator.feature.calculator.presentation.components.CalculatorDisplay
import com.anujsingh.calculator.feature.calculator.presentation.components.CalculatorKeypad
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalculatorRoot(
    onNavigateToHistory: () -> Unit,
    viewModel: CalculatorViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CalculatorEvent.NavigateToHistory -> onNavigateToHistory()
            is CalculatorEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    CalculatorScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAction: (CalculatorAction) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = CalculatorTheme.colors.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CalculatorTheme.colors.background),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with History icon button on top right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopBarIconButton(
                    icon = Icons.AutoMirrored.Outlined.FormatListBulleted,
                    contentDescription = "History",
                    onClick = { onAction(CalculatorAction.OnHistoryClick) }
                )
            }



            Spacer(modifier = Modifier.weight(1f))

            // Display Area (Formula + Result)
            CalculatorDisplay(
                formula = state.formulaDisplay,
                result = state.resultDisplay
            )

            // Keypad Grid (5 rows x 4 columns)
            CalculatorKeypad(
                selectedOperator = state.selectedOperator,
                canClearOperand = state.canClearOperand,
                onAction = onAction,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(name = "Dark Mode - Match Screenshot", showBackground = true)
@Composable
private fun CalculatorScreenDarkPreview() {
    CalculatorTheme(darkTheme = true) {
        CalculatorScreen(
            state = CalculatorState(
                formulaDisplay = "38,670÷50,000",
                resultDisplay = "0.7734"
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Light Mode - High Contrast", showBackground = true)
@Composable
private fun CalculatorScreenLightPreview() {
    CalculatorTheme(darkTheme = false) {
        CalculatorScreen(
            state = CalculatorState(
                formulaDisplay = "38,670÷50,000",
                resultDisplay = "0.7734"
            ),
            onAction = {}
        )
    }
}
