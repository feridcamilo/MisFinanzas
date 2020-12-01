package com.android.data.repository

import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.model.Balance
import com.android.data.remote.model.Master
import com.android.data.remote.model.Movement
import com.android.data.remote.model.User
import com.android.domain.repository.IWebRepository
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

    override suspend fun getDeletedMovements(clientId: String, lastSync: Date?): List<Int> {
        return dataSource.getDeletedMovements(clientId, lastSync)
    }

    override suspend fun deleteMovements(ids: List<Int>): Boolean {
        return dataSource.deleteMovements(ids)
    }

    override suspend fun sendMovements(clientId: String, movements: List<Movement>): Boolean {
        return dataSource.sendMovements(clientId, movements)
    }

    override suspend fun getServerDateTime(): String {
        return dataSource.getServerDateTime()
    }

    override suspend fun getCategories(clientId: String, lastSync: Date?): List<Master> {
        return dataSource.getCategories(clientId, lastSync)
    }

    override suspend fun getDebts(clientId: String, lastSync: Date?): List<Master> {
        return dataSource.getDebts(clientId, lastSync)
    }

    override suspend fun getPlaces(clientId: String, lastSync: Date?): List<Master> {
        return dataSource.getPlaces(clientId, lastSync)
    }

    override suspend fun getPeople(clientId: String, lastSync: Date?): List<Master> {
        return dataSource.getPeople(clientId, lastSync)
    }

    override suspend fun sendCategories(clientId: String, masters: List<Master>): Boolean {
        return dataSource.sendCategories(clientId, masters)
    }

    override suspend fun sendPlaces(clientId: String, masters: List<Master>): Boolean {
        return dataSource.sendPlaces(clientId, masters)
    }

    override suspend fun sendPeople(clientId: String, masters: List<Master>): Boolean {
        return dataSource.sendPeople(clientId, masters)
    }

    override suspend fun sendDebts(clientId: String, masters: List<Master>): Boolean {
        return dataSource.sendDebts(clientId, masters)
    }
}
