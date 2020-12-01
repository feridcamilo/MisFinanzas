package com.android.data.repository.balance.datasource

import androidx.sqlite.db.SimpleSQLiteQuery
import com.android.data.local.db.AppDatabase
import com.android.data.local.model.BalanceVO

class BalanceRoomDataSource(private val db: AppDatabase) {

    suspend fun getBalance(query: String): BalanceVO {
        val sqlQuery = SimpleSQLiteQuery(query)
        return db.movementDAO().getBalance(sqlQuery)
    }

}
