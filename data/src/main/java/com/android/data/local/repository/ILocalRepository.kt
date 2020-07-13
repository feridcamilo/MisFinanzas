package com.android.data.remote.repository

import com.android.data.local.model.UserVO
import com.android.domain.result.Result

interface ILocalRepository {

    suspend fun getUser(): Result<UserVO>
}
