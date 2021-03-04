package com.android.data.preferences.di

import android.content.Context
import com.android.data.preferences.ConfigPreferences
import com.android.data.preferences.SyncPreferences
import org.koin.dsl.module

val preferencesModule = module {
    single { ConfigPreferences(get<Context>().configPreferences) }
    single { SyncPreferences(get<Context>().syncPreferences) }
}

private val Context.configPreferences
    get() = getSharedPreferences(ConfigPreferences.PREFERENCE_NAME, Context.MODE_PRIVATE)

private val Context.syncPreferences
    get() = getSharedPreferences(SyncPreferences.PREFERENCE_NAME, Context.MODE_PRIVATE)
