package com.android.misfinanzas.ui.di

import com.android.domain.utils.listByElementsOf
import com.android.misfinanzas.ui.balance.BalanceViewModel
import com.android.misfinanzas.ui.masters.MastersViewModel
import com.android.misfinanzas.ui.movements.MovementsViewModel
import com.android.misfinanzas.ui.sync.SyncViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val uiModules by lazy {
    listByElementsOf<Module>(
        balanceFeatureModule,
        mastersFeatureModule,
        movementsFeatureModule,
        syncFeatureModule
    )
}

private val balanceFeatureModule = module {
    viewModel { BalanceViewModel(get()) }
}

private val mastersFeatureModule = module {
    viewModel { MastersViewModel(get()) }
}

private val movementsFeatureModule = module {
    viewModel { MovementsViewModel(get(), get()) }
}

private val syncFeatureModule = module {
    viewModel { SyncViewModel(get(), get()) }
}
