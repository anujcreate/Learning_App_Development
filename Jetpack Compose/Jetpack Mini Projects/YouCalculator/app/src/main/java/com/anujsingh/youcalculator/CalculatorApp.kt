package com.anujsingh.youcalculator

import android.app.Application
import com.anujsingh.youcalculator.feature.calculator.di.calculatorDataModule
import com.anujsingh.youcalculator.feature.calculator.di.calculatorPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CalculatorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CalculatorApp)
            modules(
                calculatorDataModule,
                calculatorPresentationModule
            )
        }
    }
}
