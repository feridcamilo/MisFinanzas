package com.android.data.remote

import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.*

class RetrofitDataSource {

    suspend fun getUser(user: String, password: String): User {
        return ApiClient.service.getUser(user, password).result
    }

    suspend fun getBalance(clientId: String): Balance {
        val params: MutableList<APIParameter> = ArrayList()
        params.add(APIParameter("@@IdCliente", clientId))
        val paramBody = APIParameterBody(params)
        return ApiClient.service.getSaldo(paramBody).result
    }

    suspend fun getMovements(clientId: String): List<Movement> {
        val params: MutableList<APIParameter> = ArrayList()
        params.add(APIParameter("@@IdCliente", clientId))
        val paramBody = APIParameterBody(params)
        return ApiClient.service.getMovements(paramBody).results
    }
}
