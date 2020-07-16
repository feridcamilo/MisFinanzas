package com.android.data.remote.repository

import com.android.data.remote.model.Balance
import com.android.data.remote.model.Movement
import com.android.data.remote.model.User

interface IWebRepository {

    suspend fun getUser(user: String, password: String): User

    suspend fun getBalance(clientId: String): Balance

    suspend fun getMovements(clientId: String): List<Movement>
}
