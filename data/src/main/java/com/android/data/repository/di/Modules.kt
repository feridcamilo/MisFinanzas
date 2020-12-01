package com.android.data.repository.di

import com.android.data.repository.LocalRepositoryImp
import com.android.data.repository.WebRepositoryImp
import org.koin.dsl.module

val repositoryModules by lazy {
    listOf {
        dataRepositoryModule
    }
}

private val dataRepositoryModule = module {
    factory { LocalRepositoryImp(get()) }
    factory { WebRepositoryImp(get()) }
}
