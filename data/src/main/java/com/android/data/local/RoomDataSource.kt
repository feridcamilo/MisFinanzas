package com.android.data.local

import android.content.Context
import com.android.data.local.db.AppDatabase
import com.android.data.local.model.UserVO
import com.android.domain.result.Result

class RoomDataSource(private val context: Context) {
    suspend fun getUser(): Result<UserVO> {
        return Result.Success(AppDatabase.getDatabase(context).userDao().getUser())
    }
}
