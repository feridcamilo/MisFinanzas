package com.android.data.repository.balance

import com.android.data.repository.balance.datasource.BalanceCloudDataSource
import com.android.data.repository.balance.datasource.BalanceRoomDataSource
import com.android.data.repository.balance.mappers.BalanceDataMapper
import com.android.domain.model.Balance
import com.android.domain.repository.BalanceRepository

class BalanceDataRepository(
    private val roomDataSource: BalanceRoomDataSource,
    private val cloudDataSource: BalanceCloudDataSource,
    private val mapper: BalanceDataMapper
) : BalanceRepository {

    override suspend fun getBalance(query: String): Balance {
        return mapper.map(roomDataSource.getBalance(query))
    }

    override suspend fun getCloudBalance(clientId: String): Balance {
        return mapper.map(cloudDataSource.getBalance(clientId))
    }

}
