package com.android.data.repository.user.datasource

import com.android.data.local.db.AppDatabase
import com.android.data.local.model.UserVO

class UserRoomDataSource(private val db: AppDatabase) {

    suspend fun getUser(): UserVO? {
        return db.userDao().getAll().firstOrNull()
    }

    suspend fun insertUser(user: UserVO) {
        return db.userDao().insert(user)
    }

}
