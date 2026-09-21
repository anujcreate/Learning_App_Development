package com.anujsingh.calculator.feature.calculator.presentation.navigation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anujsingh.calculator.feature.calculator.presentation.CalculatorRoot
import com.anujsingh.calculator.feature.history.presentation.HistoryRoot

private const val ANIMATION_DURATION = 400
// Authentic Apple iOS UINavigationController push/pop curve
private val AppleEasing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f)

fun NavGraphBuilder.calculatorGraph(
    navController: NavController,
    onNavigateToHistory: () -> Unit = { navController.navigate(HistoryRoute) },
    onNavigateBack: () -> Unit = { navController.popBackStack() }
) {
    composable<CalculatorRoute>(
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth * 3 / 10 },
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            ) + fadeIn(
                initialAlpha = 0.7f,
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth * 3 / 10 },
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            ) + fadeOut(
                targetAlpha = 0.7f,
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth * 3 / 10 },
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            ) + fadeIn(
                initialAlpha = 0.7f,
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth * 3 / 10 },
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            ) + fadeOut(
                targetAlpha = 0.7f,
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            )
        }
    ) {
        CalculatorRoot(
            onNavigateToHistory = onNavigateToHistory
        )
    }

    composable<HistoryRoute>(
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(ANIMATION_DURATION, easing = AppleEasing)
            )
        }
    ) {
        HistoryRoot(
            onNavigateBack = onNavigateBack
        )
    }
}



