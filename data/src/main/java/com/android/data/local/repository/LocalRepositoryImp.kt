package com.android.data.local.repository

import com.android.data.local.RoomDataSource
import com.android.data.local.model.BalanceVO
import com.android.data.local.model.MovementVO
import com.android.data.local.model.UserVO

class LocalRepositoryImp(private val dataSource: RoomDataSource) : ILocalRepository {

    override suspend fun getUser(): UserVO {
        return dataSource.getUser()
    }

    override suspend fun insertUser(user: UserVO) {
        dataSource.insertUser(user)
    }

    override suspend fun getBalance(): BalanceVO {
        return dataSource.getBalance()
    }

    override suspend fun insertBalance(balance: BalanceVO) {
        dataSource.insertBalance(balance)
    }

    override suspend fun getMovements(): List<MovementVO> {
        return dataSource.getMovements()
    }

    override suspend fun insertMovements(movements: List<MovementVO>) {
        dataSource.insertMovements(movements)
    }
}
