package com.android.data.remote

import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.*
import com.android.data.utils.ParamsUtils
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
        return ApiClient.service.getMovements(ParamsUtils.getParamsBody(clientId, lastSync)).results
    }

    suspend fun getDeletedMovements(clientId: String, lastSync: Date?): List<Int> {
        return ApiClient.service.getDeletedMovements(ParamsUtils.getParamsBody(clientId, lastSync)).results
    }

    suspend fun deleteMovements(ids: List<Int>): Boolean {
        return ApiClient.service.deleteMovements(DeletedMovement(ids)).result
    }

    suspend fun sendMovements(clientId: String, movements: List<Movement>): Boolean {
        return ApiClient.service.sendMovements(SendMovement(Integer.parseInt(clientId), movements)).results
    }

    suspend fun getCategories(clientId: String, lastSync: Date?): List<Master> {
        return ApiClient.service.getCategories(clientId, ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun getDebts(clientId: String, lastSync: Date?): List<Master> {
        return ApiClient.service.getDebts(clientId, ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun getPlaces(clientId: String, lastSync: Date?): List<Master> {
        return ApiClient.service.getPlaces(clientId, ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun getPeople(clientId: String, lastSync: Date?): List<Master> {
        return ApiClient.service.getPeople(clientId, ParamsUtils.getLastSyncToWeb(lastSync)).results
    }
}
