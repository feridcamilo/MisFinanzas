package com.android.data.di

import com.android.data.local.di.localModule
import com.android.data.remote.di.remoteModule
import com.android.data.repository.di.repositoryModules
import com.android.domain.utils.listByElementsOf
import org.koin.core.module.Module

val dataModules by lazy {
    listByElementsOf<Module>(
        localModule,
        remoteModule,
        repositoryModules
    )
}
