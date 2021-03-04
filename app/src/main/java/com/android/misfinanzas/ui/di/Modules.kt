package com.android.misfinanzas.ui.di

import com.android.domain.utils.listByElementsOf
import com.android.misfinanzas.ui.logged.balance.BalanceViewModel
import com.android.misfinanzas.ui.logged.config.ConfigViewModel
import com.android.misfinanzas.ui.logged.masters.mastersList.MastersListViewModel
import com.android.misfinanzas.ui.logged.movements.MovementsViewModel
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailViewModel
import com.android.misfinanzas.ui.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val uiModules by lazy {
    listByElementsOf<Module>(
        loginFeatureModule,
        balanceFeatureModule,
        mastersListFeatureModule,
        movementsFeatureModule,
        movementDetailFeatureModule,
        configFeatureModule

    )
}

private val loginFeatureModule = module {
    viewModel { LoginViewModel(get()) }
}

private val balanceFeatureModule = module {
    viewModel { BalanceViewModel(get(), get(), get(), get()) }
}

private val mastersListFeatureModule = module {
    viewModel { MastersListViewModel(get(), get()) }
}

private val movementsFeatureModule = module {
    viewModel { MovementsViewModel(get(), get()) }
}

private val movementDetailFeatureModule = module {
    viewModel { MovementDetailViewModel(get(), get(), get()) }
}

private val configFeatureModule = module {
    viewModel { ConfigViewModel(get(), get(), get()) }
}
