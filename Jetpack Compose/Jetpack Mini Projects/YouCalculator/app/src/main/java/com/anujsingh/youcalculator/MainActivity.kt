package com.anujsingh.youcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.anujsingh.youcalculator.core.designsystem.CalculatorTheme
import com.anujsingh.youcalculator.feature.calculator.presentation.navigation.CalculatorRoute
import com.anujsingh.youcalculator.feature.calculator.presentation.navigation.calculatorGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = CalculatorRoute,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CalculatorTheme.colors.background)
                ) {
                    calculatorGraph(navController = navController)
                }
            }
        }
    }
}