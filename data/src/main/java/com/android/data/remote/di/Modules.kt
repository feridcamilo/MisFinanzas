package com.android.data.remote.di

import com.android.data.remote.RetrofitDataSource
import org.koin.dsl.module

val remoteModule by lazy {
    listOf {
        remoteModules
    }
}

private val remoteModules = module {
    single { RetrofitDataSource() }
}
