package com.android.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.data.local.dao.*
import com.android.data.local.model.*
import com.android.data.local.model.converters.BigDecimalConverter
import com.android.data.local.model.converters.DateConverter
import com.android.data.local.model.converters.MovementTypeConverter

@Database(
    entities = [
        UserVO::class,
        CategoryVO::class,
        DebtVO::class,
        PersonVO::class,
        PlaceVO::class,
        MovementVO::class
    ], version = 1
)
@TypeConverters(MovementTypeConverter::class, DateConverter::class, BigDecimalConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDAO(): CategoryDAO
    abstract fun debtDAO(): DebtDAO
    abstract fun movementDAO(): MovementDAO
    abstract fun personDAO(): PersonDAO
    abstract fun placeDAO(): PlaceDAO
    abstract fun userDao(): UserDAO
}