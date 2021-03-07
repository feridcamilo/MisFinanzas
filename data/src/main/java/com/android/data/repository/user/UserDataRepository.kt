package com.android.data.repository.user

import com.android.data.preferences.ConfigPreferences
import com.android.data.preferences.SyncPreferences
import com.android.data.repository.user.datasource.UserCloudDataSource
import com.android.data.repository.user.datasource.UserRoomDataSource
import com.android.data.repository.user.mappers.UserDataMapper
import com.android.domain.model.User
import com.android.domain.repository.UserRepository

class UserDataRepository(
    private val roomDataSource: UserRoomDataSource,
    private val cloudDataSource: UserCloudDataSource,
    private val mapper: UserDataMapper,
    private val syncPreferences: SyncPreferences,
    private val configPreferences: ConfigPreferences
) : UserRepository {

    override suspend fun getUser(): User? {
        return roomDataSource.getUser()?.let {
            mapper.map(it)
        }
    }

    override suspend fun getCloudUser(user: String, password: String): User? {
        return cloudDataSource.getUser(user, password)?.let {
            mapper.map(it)
        }
    }

    override suspend fun insertUser(user: User) {
        roomDataSource.insertUser(mapper.mapToVO(user))
    }

    override suspend fun getServerDateTime(): String {
        return cloudDataSource.getServerDateTime()
    }

    override fun setDiffTimeWithServer(diff: String) {
        syncPreferences.diffTimeWithServer = diff
    }

    override fun getDiffTimeWithServer(): String {
        return syncPreferences.diffTimeWithServer.orEmpty()
    }

    override fun setAutoSyncOnOpen(value: Boolean) {
        configPreferences.isAutoSyncOnOpen = value
    }

    override fun isAutoSyncOnOpen(): Boolean {
        return configPreferences.isAutoSyncOnOpen
    }

    override fun setAutoSyncOnEdit(value: Boolean) {
        configPreferences.isAutoSyncOnEdit = value
    }

    override fun isAutoSyncOnEdit(): Boolean {
        return configPreferences.isAutoSyncOnEdit
    }

}
