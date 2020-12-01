package com.android.data.repository.login.datasource

import com.android.data.local.db.AppDatabase
import com.android.data.local.model.UserVO

class LoginRoomDataSource(private val db: AppDatabase) {

    suspend fun getUser(): UserVO {
        return db.userDao().getUser()
    }

    suspend fun insertUser(user: UserVO) {
        return db.userDao().insert(user)
    }
}
