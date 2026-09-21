package com.anujsingh.calculator.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.anujsingh.calculator.core.designsystem.CalculatorTheme

@Composable
fun TopBarIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(CalculatorTheme.colors.topBarIconBg)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .semantics {
                this.role = Role.Button
                this.contentDescription = contentDescription
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CalculatorTheme.colors.topBarIconTint,
            modifier = Modifier.size(24.dp)
        )
    }
}