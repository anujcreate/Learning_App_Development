package com.anujsingh.calculator

import android.app.Application
import com.anujsingh.calculator.feature.calculator.di.calculatorDataModule
import com.anujsingh.calculator.feature.calculator.di.calculatorPresentationModule
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
