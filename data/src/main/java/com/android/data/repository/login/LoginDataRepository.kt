package com.android.data.repository.login

import com.android.data.repository.login.datasource.LoginCloudDataSource
import com.android.data.repository.login.datasource.LoginRoomDataSource
import com.android.data.repository.login.mappers.UserDataMapper
import com.android.domain.repository.LoginRepository
import model.User

class LoginDataRepository(
    private val roomDataSource: LoginRoomDataSource,
    private val cloudDataSource: LoginCloudDataSource,
    private val mapper: UserDataMapper
) :LoginRepository{

    override suspend fun getUser(): User {
        return mapper.map(roomDataSource.getUser())
    }

    override suspend fun insertUser(user: User) {
        roomDataSource.insertUser(mapper.mapToVO(user))
    }

}
