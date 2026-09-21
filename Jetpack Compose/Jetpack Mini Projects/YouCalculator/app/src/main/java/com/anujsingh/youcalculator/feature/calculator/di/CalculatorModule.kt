package com.anujsingh.youcalculator.feature.calculator.di

import androidx.room.Room
import com.anujsingh.youcalculator.feature.calculator.data.database.CalculatorDatabase
import com.anujsingh.youcalculator.feature.calculator.data.datasource.CalculatorLocalDataSource
import com.anujsingh.youcalculator.feature.calculator.data.datasource.RoomCalculatorDataSource
import com.anujsingh.youcalculator.feature.calculator.data.repository.OfflineCalculatorRepository
import com.anujsingh.youcalculator.feature.calculator.domain.repository.CalculatorRepository
import com.anujsingh.youcalculator.feature.calculator.domain.usecase.EvaluateExpressionUseCase
import com.anujsingh.youcalculator.feature.calculator.domain.usecase.FormatNumberUseCase
import com.anujsingh.youcalculator.feature.calculator.presentation.CalculatorViewModel
import com.anujsingh.youcalculator.feature.history.presentation.HistoryViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val calculatorDataModule = module {
    single {
        Room.databaseBuilder(
            get(),
            CalculatorDatabase::class.java,
            "calculator_db"
        ).build()
    }
    single { get<CalculatorDatabase>().calculatorDao() }
    single<CalculatorLocalDataSource> { RoomCalculatorDataSource(get()) }
    single<CalculatorRepository> { OfflineCalculatorRepository(get()) }
}

val calculatorPresentationModule = module {
    singleOf(::EvaluateExpressionUseCase)
    single { FormatNumberUseCase() }
    viewModelOf(::CalculatorViewModel)
    viewModelOf(::HistoryViewModel)
}


