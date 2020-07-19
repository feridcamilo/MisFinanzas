package com.android.data.remote

import com.android.data.UserSesion
import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.*
import java.util.*
import kotlin.collections.ArrayList

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

    suspend fun getMovements(clientId: String, lastSync: Date?): List<Movement> {
        return ApiClient.service.getMovements(getParamsBody(clientId, lastSync)).results
    }

    suspend fun getDeletedMovements(clientId: String, lastSync: Date?): List<Int> {
        return ApiClient.service.getDeletedMovements(getParamsBody(clientId, lastSync)).results
    }

    private fun getParamsBody(clientId: String, lastSync: Date?): APIParameterBody {
        val params: MutableList<APIParameter> = ArrayList()
        params.add(APIParameter("@@IdCliente", clientId))
        if (lastSync != null) {
            val stringDate = UserSesion.getDateTimeFormatWeb().format(lastSync).replace(" ", "T")
            params.add(APIParameter("@@UltimoUpdate", stringDate))
        }
        return APIParameterBody(params)
    }

    suspend fun deleteMovements(ids: List<Int>): Boolean {
        return ApiClient.service.deleteMovements(DeletedMovement(ids)).result
    }

    suspend fun sendMovements(cliendId: String, movements: List<Movement>): Boolean {
        return ApiClient.service.sendMovements(SendMovement(Integer.parseInt(cliendId), movements)).results
    }

    suspend fun getCategories(clientId: String): List<Master> {
        return ApiClient.service.getCategories(clientId).results
    }

    suspend fun getDebts(clientId: String): List<Master> {
        return ApiClient.service.getDebts(clientId).results
    }

    suspend fun getPlaces(clientId: String): List<Master> {
        return ApiClient.service.getPlaces(clientId).results
    }

    suspend fun getPeople(clientId: String): List<Master> {
        return ApiClient.service.getPeople(clientId).results
    }
}
