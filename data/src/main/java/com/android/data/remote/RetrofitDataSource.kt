package com.android.data.remote

import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.*
import com.android.domain.result.Result

class RetrofitDataSource {

    suspend fun getUser(user: String, password: String): Result<UserDTO> {
        return Result.Success(ApiClient.service.getUser(user, password))
    }

    suspend fun getMovements(clientId: String): Result<List<Movement>> {
        val params: MutableList<APIParameter> = ArrayList()
        params.add(APIParameter("@@IdCliente", clientId))
        val paramBody = APIParameterBody(params)
        return Result.Success(ApiClient.service.getMovements(paramBody).results)
    }
}
