# ProGuard & R8 Optimization Rules for Calculator

# Optimization passes
-repackageclasses ''
-allowaccessmodification

# Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Entity and Model classes for Room and Domain
-keep class com.anujsingh.calculator.feature.calculator.data.database.entity.** { *; }
-keepclassmembers class com.anujsingh.calculator.feature.calculator.data.database.entity.** { *; }
-keep class com.anujsingh.calculator.feature.calculator.domain.model.** { *; }

# Koin Dependency Injection
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# Kotlin Coroutines and Serialization
-keepclassmembers class kotlinx.coroutines.** { *; }
