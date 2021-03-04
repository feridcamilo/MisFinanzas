package com.android.data.repository.movement.datasource

import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.DeletedMovementDTO
import com.android.data.remote.model.MovementDTO
import com.android.data.remote.model.SendMovementDTO
import com.android.data.utils.ParamsUtils
import com.android.domain.UserSesion
import java.util.*

class MovementCloudDataSource {

    suspend fun getMovements(lastSync: Date?): List<MovementDTO> {
        return ApiClient.service.getMovements(ParamsUtils.getParamsBody(UserSesion.getClientId(), lastSync)).results
    }

    suspend fun getDeletedMovements(lastSync: Date?): List<Int> {
        return ApiClient.service.getDeletedMovements(ParamsUtils.getParamsBody(UserSesion.getClientId(), lastSync)).results
    }

    suspend fun deleteMovements(ids: List<Int>): Boolean {
        return ApiClient.service.deleteMovements(DeletedMovementDTO(ids)).result
    }

    suspend fun sendMovements(movements: List<MovementDTO>): Boolean {
        return ApiClient.service.sendMovements(SendMovementDTO(Integer.parseInt(UserSesion.getClientId()), movements)).results
    }

}
