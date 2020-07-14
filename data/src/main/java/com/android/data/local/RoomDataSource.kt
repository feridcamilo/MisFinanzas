package com.android.data.local

import android.content.Context
import com.android.data.local.db.AppDatabase
import com.android.data.local.model.BalanceVO
import com.android.data.local.model.MovementVO
import com.android.data.local.model.UserVO
import com.android.domain.result.Result

class RoomDataSource(private val context: Context) {
    suspend fun getUser(): Result<UserVO> {
        return Result.Success(AppDatabase.getDatabase(context).userDao().getUser())
    }

    suspend fun insertUser(user: UserVO) {
        return AppDatabase.getDatabase(context).userDao().insert(user)
    }

    suspend fun getBalance(): Result<BalanceVO> {
        return Result.Success(AppDatabase.getDatabase(context).balanceDAO().getBalance())
    }

    suspend fun insertBalance(balance: BalanceVO) {
        return AppDatabase.getDatabase(context).balanceDAO().insert(balance)
    }

    suspend fun insertMovements(movements: List<MovementVO>) {
        AppDatabase.getDatabase(context).movementDAO().insertAll(movements)
    }

    suspend fun getMovements(): Result<List<MovementVO>> {
        return Result.Success(AppDatabase.getDatabase(context).movementDAO().getAll())
    }
}
