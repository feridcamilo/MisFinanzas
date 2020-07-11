package com.example.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.*
import com.example.data.local.model.*

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
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDAO(): CategoryDAO
    abstract fun debtDAO(): DebtDAO
    abstract fun movementDAO(): MovementDAO
    abstract fun personDAO(): PersonDAO
    abstract fun placeDAO(): PlaceDAO
    abstract fun userDao(): UserDAO
}