package com.android.data.local.di

import com.android.data.local.db.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { AppDatabase.getDatabase(get()) }
}
