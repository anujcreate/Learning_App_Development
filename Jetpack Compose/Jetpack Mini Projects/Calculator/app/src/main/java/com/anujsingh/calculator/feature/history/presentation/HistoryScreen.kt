package com.anujsingh.calculator.feature.history.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anujsingh.calculator.core.designsystem.CalculatorTheme
import com.anujsingh.calculator.core.designsystem.components.TopBarIconButton
import com.anujsingh.calculator.core.presentation.ObserveAsEvents
import com.anujsingh.calculator.feature.calculator.domain.model.CalculationHistory
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryRoot(
    onNavigateBack: () -> Unit,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HistoryEvent.NavigateBack -> onNavigateBack()
            is HistoryEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    HistoryScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            if (action is HistoryAction.OnItemClick) {
                clipboardManager.setText(AnnotatedString(action.item.result))
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun HistoryScreen(
    state: HistoryState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAction: (HistoryAction) -> Unit
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
                .background(CalculatorTheme.colors.background)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TopBarIconButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    onClick = { onAction(HistoryAction.OnBackClick) }
                )

                Text(
                    text = "History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CalculatorTheme.colors.displayPrimary
                )

                if (state.historyItems.isNotEmpty()) {
                    TopBarIconButton(
                        icon = Icons.Outlined.Delete,
                        contentDescription = "Clear History",
                        onClick = { onAction(HistoryAction.OnClearClick) }
                    )
                } else {
                    Spacer(modifier = Modifier.padding(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CalculatorTheme.colors.buttonOperator)
                    }
                }
                state.historyItems.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No calculation history yet",
                            fontSize = 16.sp,
                            color = CalculatorTheme.colors.displaySecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.historyItems,
                            key = { it.id }
                        ) { item ->
                            HistoryItemCard(
                                item = item,
                                onClick = { onAction(HistoryAction.OnItemClick(item)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryItemCard(
    item: CalculationHistory,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CalculatorTheme.colors.buttonNumber)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = item.expression,
            fontSize = 16.sp,
            color = CalculatorTheme.colors.displaySecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "= ${item.result}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CalculatorTheme.colors.displayPrimary
        )
    }
}
