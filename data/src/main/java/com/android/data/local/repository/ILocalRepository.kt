package com.android.data.local.repository

import com.android.data.local.model.BalanceVO
import com.android.data.local.model.MovementVO
import com.android.data.local.model.UserVO

interface ILocalRepository {

    suspend fun getUser(): UserVO

    suspend fun insertUser(user: UserVO)

    suspend fun getBalance(): BalanceVO

    suspend fun insertBalance(balance: BalanceVO)

    suspend fun getMovements(): List<MovementVO>

    suspend fun insertMovements(movements: List<MovementVO>)
}
