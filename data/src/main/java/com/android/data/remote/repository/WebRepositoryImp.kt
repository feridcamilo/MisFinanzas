package com.android.data.remote.repository

import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.model.Movement
import com.android.data.remote.model.UserDTO
import com.android.domain.result.Result

class WebRepositoryImp(private val dataSource: RetrofitDataSource) : IWebRepository {

    override suspend fun getUser(user: String, password: String): Result<UserDTO> {
        return dataSource.getUser(user, password)
    }

    override suspend fun getMovements(clientId: String): Result<List<Movement>> {
        return dataSource.getMovements(clientId)
    }
}
