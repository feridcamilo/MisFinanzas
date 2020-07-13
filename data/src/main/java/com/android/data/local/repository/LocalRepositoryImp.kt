package com.android.data.remote.repository

import com.android.data.local.RoomDataSource
import com.android.data.local.model.UserVO
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.model.UserDTO
import com.android.domain.result.Result

class LocalRepositoryImp(private val dataSource: RoomDataSource) : ILocalRepository {

    override suspend fun getUser(): Result<UserVO> {
        return dataSource.getUser()
    }
}
