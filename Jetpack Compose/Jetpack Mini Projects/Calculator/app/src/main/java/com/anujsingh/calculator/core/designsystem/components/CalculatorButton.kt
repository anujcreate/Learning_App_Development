package com.anujsingh.calculator.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

@Composable
fun CalculatorButton(
    backgroundColor: Color,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    // Apple-style spring press scale (0.92f on press, bouncy spring return)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "AppleButtonScale"
    )

    // Apple-style subtle highlight/brightness shift on press
    val animatedBgColor by animateColorAsState(
        targetValue = if (isPressed) backgroundColor.copy(alpha = 0.78f) else backgroundColor,
        animationSpec = tween(durationMillis = 80),
        label = "AppleButtonColor"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            .background(animatedBgColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Authentic Apple touch interaction
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            )
            .semantics {
                this.role = Role.Button
                this.contentDescription = contentDescription
            }
    ) {
        content()
    }
}