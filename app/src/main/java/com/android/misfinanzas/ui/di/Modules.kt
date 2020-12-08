package com.android.misfinanzas.ui.di

import com.android.domain.utils.listByElementsOf
import com.android.misfinanzas.ui.logged.balance.BalanceViewModel
import com.android.misfinanzas.ui.logged.masters.MastersViewModel
import com.android.misfinanzas.ui.logged.movements.MovementsViewModel
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailViewModel
import com.android.misfinanzas.ui.logged.sync.SyncViewModel
import com.android.misfinanzas.ui.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val uiModules by lazy {
    listByElementsOf<Module>(
        loginFeatureModule,
        balanceFeatureModule,
        mastersFeatureModule,
        movementsFeatureModule,
        movementDetailFeatureModule,
        syncFeatureModule

    )
}

private val loginFeatureModule = module {
    viewModel { LoginViewModel(get()) }
}

private val balanceFeatureModule = module {
    viewModel { BalanceViewModel(get(), get()) }
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
