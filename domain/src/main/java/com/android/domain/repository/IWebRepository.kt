package com.android.domain.repository

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

    suspend fun getServerDateTime(): String

    suspend fun getCategories(clientId: String, lastSync: Date?): List<Master>

    suspend fun getDebts(clientId: String, lastSync: Date?): List<Master>

    suspend fun getPlaces(clientId: String, lastSync: Date?): List<Master>

    suspend fun getPeople(clientId: String, lastSync: Date?): List<Master>

    suspend fun sendCategories(clientId: String, masters: List<Master>): Boolean

    suspend fun sendPlaces(clientId: String, masters: List<Master>): Boolean

    suspend fun sendPeople(clientId: String, masters: List<Master>): Boolean

    suspend fun sendDebts(clientId: String, masters: List<Master>): Boolean
}
