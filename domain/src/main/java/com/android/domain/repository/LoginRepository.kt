package com.android.domain.repository

import model.User

interface LoginRepository {
    suspend fun getUser(): User
    suspend fun insertUser(user: User)
}
