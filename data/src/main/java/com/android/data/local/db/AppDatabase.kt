package com.android.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.data.local.dao.*
import com.android.data.local.model.*
import com.android.data.local.model.converters.BigDecimalConverter
import com.android.data.local.model.converters.DateConverter

@Database(
    entities = [
        BalanceVO::class,
        CategoryVO::class,
        DebtVO::class,
        MovementVO::class,
        PersonVO::class,
        PlaceVO::class,
        UserVO::class
    ], version = 1
)
@TypeConverters(DateConverter::class, BigDecimalConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDAO(): BalanceDAO
    abstract fun categoryDAO(): CategoryDAO
    abstract fun debtDAO(): DebtDAO
    abstract fun movementDAO(): MovementDAO
    abstract fun personDAO(): PersonDAO
    abstract fun placeDAO(): PlaceDAO
    abstract fun userDao(): UserDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            INSTANCE = INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "MisFinanzas").build()
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
