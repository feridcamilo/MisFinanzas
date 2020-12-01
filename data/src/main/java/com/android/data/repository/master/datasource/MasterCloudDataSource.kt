package com.android.data.repository.master.datasource

import com.android.data.remote.api.ApiClient
import com.android.data.remote.model.MasterDTO
import com.android.data.remote.model.SendMasterDTO
import com.android.data.utils.ParamsUtils
import java.util.*

class MasterCloudDataSource {

    suspend fun getCategories(clientId: String, lastSync: Date?): List<MasterDTO> {
        return ApiClient.service.getCategories(clientId, ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun getDebts(clientId: String, lastSync: Date?): List<MasterDTO> {
        return ApiClient.service.getDebts(clientId, ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun getPlaces(clientId: String, lastSync: Date?): List<MasterDTO> {
        return ApiClient.service.getPlaces(clientId, ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun getPeople(clientId: String, lastSync: Date?): List<MasterDTO> {
        return ApiClient.service.getPeople(clientId, ParamsUtils.getLastSyncToWeb(lastSync)).results
    }

    suspend fun sendCategories(clientId: String, masters: List<MasterDTO>): Boolean {
        return ApiClient.service.sendMasters(SendMasterDTO(Integer.parseInt(clientId), MasterDTO.TYPE_CATEGORY, masters)).results
    }

    suspend fun sendPlaces(clientId: String, masters: List<MasterDTO>): Boolean {
        return ApiClient.service.sendMasters(SendMasterDTO(Integer.parseInt(clientId), MasterDTO.TYPE_PLACE, masters)).results
    }

    suspend fun sendPeople(clientId: String, masters: List<MasterDTO>): Boolean {
        return ApiClient.service.sendMasters(SendMasterDTO(Integer.parseInt(clientId), MasterDTO.TYPE_PERSON, masters)).results
    }

    suspend fun sendDebts(clientId: String, masters: List<MasterDTO>): Boolean {
        return ApiClient.service.sendMasters(SendMasterDTO(Integer.parseInt(clientId), MasterDTO.TYPE_DEBT, masters)).results
    }

}
