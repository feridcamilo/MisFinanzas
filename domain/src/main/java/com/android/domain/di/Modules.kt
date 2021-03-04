package com.android.domain.di

import com.android.domain.usecase.LoginUseCase
import org.koin.dsl.module

val domainModules by lazy {
    listOf {
        useCasesModule
    }
}

private val useCasesModule = module {
    factory { LoginUseCase() }
}
