package com.android.data.remote.repository

import com.android.data.remote.model.Balance
import com.android.data.remote.model.Master
import com.android.data.remote.model.Movement
import com.android.data.remote.model.User
import java.util.*

interface IWebRepository {

    suspend fun getUser(user: String, password: String): User

    suspend fun getBalance(clientId: String): Balance

    suspend fun getMovements(clientId: String, lastSync: Date?): List<Movement>

    suspend fun getDeletedMovements(clientId: String, lastSync: Date?): List<Int>

    suspend fun deleteMovements(ids: List<Int>): Boolean

    suspend fun sendMovements(clientId: String, movements: List<Movement>): Boolean

    suspend fun getCategories(clientId: String): List<Master>

    suspend fun getDebts(clientId: String): List<Master>

    suspend fun getPlaces(clientId: String): List<Master>

    suspend fun getPeople(clientId: String): List<Master>
}
