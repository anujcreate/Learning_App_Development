package com.anujsingh.youcalculator.feature.calculator.presentation.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anujsingh.youcalculator.feature.calculator.presentation.CalculatorRoot
import com.anujsingh.youcalculator.feature.history.presentation.HistoryRoot

private const val ANIMATION_DURATION = 350
private val animationEasing = FastOutSlowInEasing

fun NavGraphBuilder.calculatorGraph(
    navController: NavController,
    onNavigateToHistory: () -> Unit = { navController.navigate(HistoryRoute) },
    onNavigateBack: () -> Unit = { navController.popBackStack() }
) {
    composable<CalculatorRoute>(
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 4 },
                animationSpec = tween(ANIMATION_DURATION, easing = animationEasing)
            ) + fadeIn(animationSpec = tween(ANIMATION_DURATION, easing = animationEasing))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 4 },
                animationSpec = tween(ANIMATION_DURATION, easing = animationEasing)
            ) + fadeOut(animationSpec = tween(ANIMATION_DURATION, easing = animationEasing))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 4 },
                animationSpec = tween(ANIMATION_DURATION, easing = animationEasing)
            ) + fadeIn(animationSpec = tween(ANIMATION_DURATION, easing = animationEasing))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 4 },
                animationSpec = tween(ANIMATION_DURATION, easing = animationEasing)
            ) + fadeOut(animationSpec = tween(ANIMATION_DURATION, easing = animationEasing))
        }
    ) {
        CalculatorRoot(
            onNavigateToHistory = onNavigateToHistory
        )
    }

    composable<HistoryRoute>(
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(ANIMATION_DURATION, easing = animationEasing)
            ) + fadeIn(animationSpec = tween(ANIMATION_DURATION, easing = animationEasing))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(ANIMATION_DURATION, easing = animationEasing)
            ) + fadeOut(animationSpec = tween(ANIMATION_DURATION, easing = animationEasing))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(ANIMATION_DURATION, easing = animationEasing)
            ) + fadeIn(animationSpec = tween(ANIMATION_DURATION, easing = animationEasing))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(ANIMATION_DURATION, easing = animationEasing)
            ) + fadeOut(animationSpec = tween(ANIMATION_DURATION, easing = animationEasing))
        }
    ) {
        HistoryRoot(
            onNavigateBack = onNavigateBack
        )
    }
}



