package com.android.data.repository.di

import com.android.data.repository.balance.BalanceDataRepository
import com.android.data.repository.balance.datasource.BalanceCloudDataSource
import com.android.data.repository.balance.datasource.BalanceRoomDataSource
import com.android.data.repository.balance.mappers.BalanceDataMapper
import com.android.data.repository.master.MasterDataRepository
import com.android.data.repository.master.datasource.MasterCloudDataSource
import com.android.data.repository.master.datasource.MasterRoomDataSource
import com.android.data.repository.master.mappers.*
import com.android.data.repository.movement.MovementDataRepository
import com.android.data.repository.movement.datasource.MovementCloudDataSource
import com.android.data.repository.movement.datasource.MovementRoomDataSource
import com.android.data.repository.movement.mappers.MovementDataMapper
import com.android.data.repository.user.UserDataRepository
import com.android.data.repository.user.datasource.UserCloudDataSource
import com.android.data.repository.user.datasource.UserRoomDataSource
import com.android.data.repository.user.mappers.UserDataMapper
import com.android.domain.repository.BalanceRepository
import com.android.domain.repository.MasterRepository
import com.android.domain.repository.MovementRepository
import com.android.domain.repository.UserRepository
import com.android.domain.utils.listByElementsOf
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModules by lazy {
    listByElementsOf<Module>(
        balanceRepositoryModule,
        masterRepositoryModule,
        movementRepositoryModule,
        userRepositoryModule
    )
}

private val balanceRepositoryModule = module {
    factory { BalanceDataMapper() }
    factory { BalanceRoomDataSource(get()) }
    factory { BalanceCloudDataSource() }
    factory<BalanceRepository> { BalanceDataRepository(get(), get(), get()) }
}

private val masterRepositoryModule = module {
    factory { CategoryDataMapper() }
    factory { DebtDataMapper() }
    factory { MasterDataMapper() }
    factory { PersonDataMapper() }
    factory { PlaceDataMapper() }
    factory { MasterRoomDataSource(get()) }
    factory { MasterCloudDataSource() }
    factory<MasterRepository> { MasterDataRepository(get(), get(), get(), get(), get(), get(), get()) }
}

private val movementRepositoryModule = module {
    factory { MovementDataMapper() }
    factory { MovementRoomDataSource(get()) }
    factory { MovementCloudDataSource() }
    factory<MovementRepository> { MovementDataRepository(get(), get(), get()) }
}

private val userRepositoryModule = module {
    factory { UserDataMapper() }
    factory { UserRoomDataSource(get()) }
    factory { UserCloudDataSource() }
    factory<UserRepository> { UserDataRepository(get(), get(), get()) }
}
