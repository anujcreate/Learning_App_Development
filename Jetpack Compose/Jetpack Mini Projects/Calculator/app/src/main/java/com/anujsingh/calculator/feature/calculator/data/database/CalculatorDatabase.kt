package com.anujsingh.calculator.feature.calculator.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anujsingh.calculator.feature.calculator.data.database.entity.CalculationHistoryEntity

@Database(entities = [CalculationHistoryEntity::class], version = 1, exportSchema = false)
abstract class CalculatorDatabase : RoomDatabase() {
    abstract fun calculatorDao(): CalculatorDao
}
