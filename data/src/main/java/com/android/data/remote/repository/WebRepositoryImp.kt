package com.android.data.remote.repository

import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.model.Balance
import com.android.data.remote.model.Movement
import com.android.data.remote.model.User
import com.android.domain.result.Result

class WebRepositoryImp(private val dataSource: RetrofitDataSource) : IWebRepository {

    override suspend fun getUser(user: String, password: String): Result<User> {
        return dataSource.getUser(user, password)
    }

    override suspend fun getBalance(clientId: String): Result<Balance> {
        return dataSource.getBalance(clientId)
    }

    override suspend fun getMovements(clientId: String): Result<List<Movement>> {
        return dataSource.getMovements(clientId)
    }
}
