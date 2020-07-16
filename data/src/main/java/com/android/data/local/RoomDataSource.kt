package com.android.data.local

import android.content.Context
import com.android.data.local.db.AppDatabase
import com.android.data.local.model.BalanceVO
import com.android.data.local.model.MovementVO
import com.android.data.local.model.UserVO

class RoomDataSource(private val context: Context) {
    suspend fun getUser(): UserVO {
        return AppDatabase.getDatabase(context).userDao().getUser()
    }

    suspend fun insertUser(user: UserVO) {
        return AppDatabase.getDatabase(context).userDao().insert(user)
    }

    suspend fun getBalance(): BalanceVO {
        return AppDatabase.getDatabase(context).balanceDAO().getBalance()
    }

    suspend fun insertBalance(balance: BalanceVO) {
        return AppDatabase.getDatabase(context).balanceDAO().insert(balance)
    }

    suspend fun insertMovements(movements: List<MovementVO>) {
        AppDatabase.getDatabase(context).movementDAO().insertAll(movements)
    }

    suspend fun getMovements(): List<MovementVO> {
        return AppDatabase.getDatabase(context).movementDAO().getAll()
    }
}
