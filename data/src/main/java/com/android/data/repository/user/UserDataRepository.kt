package com.android.data.repository.user

import com.android.data.repository.user.datasource.UserCloudDataSource
import com.android.data.repository.user.datasource.UserRoomDataSource
import com.android.data.repository.user.mappers.UserDataMapper
import com.android.domain.model.User
import com.android.domain.repository.UserRepository

class UserDataRepository(
    private val roomDataSource: UserRoomDataSource,
    private val cloudDataSource: UserCloudDataSource,
    private val mapper: UserDataMapper
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

}
