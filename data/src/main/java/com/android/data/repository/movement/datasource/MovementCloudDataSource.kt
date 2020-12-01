package com.android.data.repository.movement.datasource

import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.DeletedMovementDTO
import com.android.data.remote.model.MovementDTO
import com.android.data.remote.model.SendMovementDTO
import com.android.data.utils.ParamsUtils
import java.util.*

class MovementCloudDataSource {

    suspend fun getMovements(clientId: String, lastSync: Date?): List<MovementDTO> {
        return ApiClient.service.getMovements(ParamsUtils.getParamsBody(clientId, lastSync)).results
    }

    suspend fun getDeletedMovements(clientId: String, lastSync: Date?): List<Int> {
        return ApiClient.service.getDeletedMovements(ParamsUtils.getParamsBody(clientId, lastSync)).results
    }

    suspend fun deleteMovements(ids: List<Int>): Boolean {
        return ApiClient.service.deleteMovements(DeletedMovementDTO(ids)).result
    }

    suspend fun sendMovements(clientId: String, movements: List<MovementDTO>): Boolean {
        return ApiClient.service.sendMovements(SendMovementDTO(Integer.parseInt(clientId), movements)).results
    }

}
