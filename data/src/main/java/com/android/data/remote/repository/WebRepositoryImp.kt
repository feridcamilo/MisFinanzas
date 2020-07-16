package com.android.data.remote.repository

import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.model.Balance
import com.android.data.remote.model.Master
import com.android.data.remote.model.Movement
import com.android.data.remote.model.User
import java.util.*

class WebRepositoryImp(private val dataSource: RetrofitDataSource) : IWebRepository {

    override suspend fun getUser(user: String, password: String): User {
        return dataSource.getUser(user, password)
    }

    override suspend fun getBalance(clientId: String): Balance {
        return dataSource.getBalance(clientId)
    }

    override suspend fun getMovements(clientId: String, lastSync: Date?): List<Movement> {
        return dataSource.getMovements(clientId, lastSync)
    }

    override suspend fun getCategories(clientId: String): List<Master> {
        return dataSource.getCategories(clientId)
    }

    override suspend fun getDebts(clientId: String): List<Master> {
        return dataSource.getDebts(clientId)
    }

    override suspend fun getPlaces(clientId: String): List<Master> {
        return dataSource.getPlaces(clientId)
    }

    override suspend fun getPeople(clientId: String): List<Master> {
        return dataSource.getPeople(clientId)
    }
}
