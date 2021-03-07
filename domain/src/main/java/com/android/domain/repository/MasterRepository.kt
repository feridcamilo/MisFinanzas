package com.android.domain.repository

import com.android.domain.model.*
import java.util.*

interface MasterRepository {
    suspend fun updateLastSyncMasters(date: Date)
    suspend fun getLastSyncMasters(): Date?

    suspend fun download()
    //suspend fun upload()
    suspend fun saveMasterOnCloud(master: Master, type: Int) : Boolean

    suspend fun getCategories(): List<Category>
    suspend fun getDebts(): List<Debt>
    suspend fun getPlaces(): List<Place>
    suspend fun getPeople(): List<Person>
}
