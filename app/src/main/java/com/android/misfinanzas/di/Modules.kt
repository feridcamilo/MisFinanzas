package com.android.misfinanzas.di

import android.app.Application
import com.android.data.di.dataModules
import com.android.domain.di.domainModules
import com.android.domain.utils.listByElementsOf
import com.android.misfinanzas.mappers.BalanceMapper
import com.android.misfinanzas.mappers.MastersMapper
import com.android.misfinanzas.mappers.MovementMapper
import com.android.misfinanzas.mappers.UserMapper
import com.android.misfinanzas.ui.di.uiModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun injectModules(app: Application) {
    startKoin {
        androidLogger(Level.ERROR)
        androidContext(app)
        modules(appModules)
    }
}

val appModules by lazy {
    listByElementsOf<Module>(
        uiModules,
        mappersModule,
        dataModules,
        domainModules
    )
}

private val mappersModule = module {
    factory { BalanceMapper() }
    factory { MastersMapper() }
    factory { MovementMapper() }
    factory { UserMapper() }
}
