package com.android.domain.repository

import com.android.domain.model.User

interface UserRepository {
    suspend fun getUser(): User?
    suspend fun getCloudUser(user: String, password: String): User?
    suspend fun insertUser(user: User)
    suspend fun getServerDateTime(): String

    fun setDiffTimeWithServer(diff: String)
    fun getDiffTimeWithServer(): String

    fun setAutoSyncOnOpen(value: Boolean)
    fun isAutoSyncOnOpen(): Boolean

    fun setAutoSyncOnEdit(value: Boolean)
    fun isAutoSyncOnEdit(): Boolean
}
