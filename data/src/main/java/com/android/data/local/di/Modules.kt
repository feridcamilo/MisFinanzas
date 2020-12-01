package com.android.data.local.di

import com.android.data.local.RoomDataSource
import com.android.data.local.db.AppDatabase
import org.koin.dsl.module

val localModule by lazy {
    listOf {
        localModules
    }
}

private val localModules = module {
    single { AppDatabase.getDatabase(get()) }
    factory { RoomDataSource(get()) }
}
