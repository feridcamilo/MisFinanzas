package com.android.data.repository.user.datasource

import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.UserDTO

class UserCloudDataSource {

    suspend fun getUser(user: String, password: String): UserDTO? {
        return ApiClient.service.getUser(user, password).result
    }

    suspend fun getServerDateTime(): String {
        return ApiClient.service.getServerDateTime().result
    }

}
