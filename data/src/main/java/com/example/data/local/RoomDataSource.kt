package com.example.data.local

import android.content.Context
import androidx.room.Room
import com.example.data.local.db.AppDatabase

open class RoomDataSource(private val context: Context) {
    @Volatile
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (instance != null) {
            return instance as AppDatabase
        }

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "MisFinanzas"
        ).build()
    }
}