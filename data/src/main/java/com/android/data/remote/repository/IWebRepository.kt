package com.android.data.remote.repository

import com.android.data.remote.model.Balance
import com.android.data.remote.model.Movement
import com.android.data.remote.model.User
import com.android.domain.result.Result

interface IWebRepository {

    suspend fun getUser(user: String, password: String): Result<User>

    suspend fun getBalance(clientId: String): Result<Balance>

    suspend fun getMovements(clientId: String): Result<List<Movement>>
}
