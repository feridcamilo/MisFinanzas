package com.android.data.local.repository

import com.android.data.local.model.BalanceVO
import com.android.data.local.model.MovementVO
import com.android.data.local.model.UserVO
import com.android.domain.result.Result

interface ILocalRepository {

    suspend fun getUser(): Result<UserVO>

    suspend fun insertUser(user: UserVO)

    suspend fun getBalance(): Result<BalanceVO>

    suspend fun insertBalance(balance: BalanceVO)

    suspend fun getMovements(): Result<List<MovementVO>>

    suspend fun insertMovements(movements: List<MovementVO>)
}
