package com.android.data.remote

import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.*
import com.android.domain.result.Result

class RetrofitDataSource {

    suspend fun getUser(user: String, password: String): Result<User> {
        return Result.Success(ApiClient.service.getUser(user, password).result)
    }

    suspend fun getBalance(clientId: String): Result<Balance> {
        val params: MutableList<APIParameter> = ArrayList()
        params.add(APIParameter("@@IdCliente", clientId))
        val paramBody = APIParameterBody(params)
        return Result.Success(ApiClient.service.getSaldo(paramBody).result)
    }

    suspend fun getMovements(clientId: String): Result<List<Movement>> {
        val params: MutableList<APIParameter> = ArrayList()
        params.add(APIParameter("@@IdCliente", clientId))
        val paramBody = APIParameterBody(params)
        return Result.Success(ApiClient.service.getMovements(paramBody).results)
    }
}
