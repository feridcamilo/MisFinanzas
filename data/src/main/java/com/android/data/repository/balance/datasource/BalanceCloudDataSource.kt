package com.android.data.repository.balance.datasource

import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.APIParameter
import com.android.data.remote.model.APIParameterBody
import com.android.data.remote.model.BalanceDTO

class BalanceCloudDataSource {

    suspend fun getBalance(clientId: String): BalanceDTO {
        val params: MutableList<APIParameter> = ArrayList()
        params.add(APIParameter("@@IdCliente", clientId))
        val paramBody = APIParameterBody(params)
        return ApiClient.service.getSaldo(paramBody).result
    }

}
