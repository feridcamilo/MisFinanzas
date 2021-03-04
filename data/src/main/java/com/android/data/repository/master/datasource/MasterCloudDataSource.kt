package com.android.data.repository.master.datasource

import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.MasterDTO
import com.android.data.remote.model.SendMasterDTO
import com.android.data.utils.ParamsUtils
import com.android.domain.UserSesion
import com.android.domain.model.Master
import java.util.*

class MasterCloudDataSource {

    suspend fun getCategories(lastSync: Date?): List<MasterDTO> {
        return ApiClient.service.getCategories(UserSesion.getClientId(), ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun getDebts(lastSync: Date?): List<MasterDTO> {
        return ApiClient.service.getDebts(UserSesion.getClientId(), ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun getPlaces(lastSync: Date?): List<MasterDTO> {
        return ApiClient.service.getPlaces(UserSesion.getClientId(), ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun getPeople(lastSync: Date?): List<MasterDTO> {
        return ApiClient.service.getPeople(UserSesion.getClientId(), ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun sendCategories(masters: List<MasterDTO>): Boolean {
        return ApiClient.service.sendMasters(SendMasterDTO(Integer.parseInt(UserSesion.getClientId()), Master.TYPE_CATEGORY, masters)).results
    }

    suspend fun sendPlaces(masters: List<MasterDTO>): Boolean {
        return ApiClient.service.sendMasters(SendMasterDTO(Integer.parseInt(UserSesion.getClientId()), Master.TYPE_PLACE, masters)).results
    }

    suspend fun sendPeople(masters: List<MasterDTO>): Boolean {
        return ApiClient.service.sendMasters(SendMasterDTO(Integer.parseInt(UserSesion.getClientId()), Master.TYPE_PERSON, masters)).results
    }

    suspend fun sendDebts(masters: List<MasterDTO>): Boolean {
        return ApiClient.service.sendMasters(SendMasterDTO(Integer.parseInt(UserSesion.getClientId()), Master.TYPE_DEBT, masters)).results
    }

}
