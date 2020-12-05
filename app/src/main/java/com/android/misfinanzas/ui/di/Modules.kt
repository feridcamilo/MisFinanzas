package com.android.misfinanzas.ui.di

import com.android.domain.utils.listByElementsOf
import com.android.misfinanzas.ui.balance.BalanceViewModel
import com.android.misfinanzas.ui.masters.MastersViewModel
import com.android.misfinanzas.ui.movements.MovementsViewModel
import com.android.misfinanzas.ui.movements.movementDetail.MovementDetailViewModel
import com.android.misfinanzas.ui.sync.SyncViewModel
import com.android.misfinanzas.ui.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val uiModules by lazy {
    listByElementsOf<Module>(
        balanceFeatureModule,
        mastersFeatureModule,
        movementsFeatureModule,
        movementDetailFeatureModule,
        syncFeatureModule,
        loginFeatureModule
    )
}

private val balanceFeatureModule = module {
    viewModel { BalanceViewModel(get(), get(), get()) }
}

private val mastersFeatureModule = module {
    viewModel { MastersViewModel(get()) }
}

private val movementsFeatureModule = module {
    viewModel { MovementsViewModel(get(), get()) }
}

private val movementDetailFeatureModule = module {
    viewModel { MovementDetailViewModel(get()) }
}

private val syncFeatureModule = module {
    viewModel { SyncViewModel(get(), get(), get()) }
}

private val loginFeatureModule = module {
    viewModel { LoginViewModel(get()) }
}
