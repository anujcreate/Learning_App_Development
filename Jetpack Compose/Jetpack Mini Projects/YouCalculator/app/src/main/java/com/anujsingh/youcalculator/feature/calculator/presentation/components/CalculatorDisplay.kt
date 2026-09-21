package com.anujsingh.youcalculator.feature.calculator.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anujsingh.youcalculator.core.designsystem.CalculatorTheme

@Composable
fun CalculatorDisplay(
    formula: String,
    result: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Secondary Formula Line (e.g. 38,670÷50,000)
        Text(
            text = formula,
            fontSize = 28.sp,
            color = CalculatorTheme.colors.displaySecondary,
            textAlign = TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        // Primary Result Line (e.g. 0.7734)
        val dynamicFontSize = when {
            result.length > 12 -> 38.sp
            result.length > 8 -> 48.sp
            else -> 64.sp
        }

        Text(
            text = result,
            fontSize = dynamicFontSize,
            fontWeight = FontWeight.Light,
            color = CalculatorTheme.colors.displayPrimary,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
