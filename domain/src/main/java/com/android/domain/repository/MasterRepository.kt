package com.android.domain.repository

import com.android.domain.model.Category
import com.android.domain.model.Debt
import com.android.domain.model.Person
import com.android.domain.model.Place
import java.util.*

interface MasterRepository {
    suspend fun updateLastSyncMasters(date: Date)
    suspend fun getLastSyncMasters(): Date?

    suspend fun download()
    suspend fun upload()

    suspend fun getCategories(): List<Category>
    suspend fun getDebts(): List<Debt>
    suspend fun getPlaces(): List<Place>
    suspend fun getPeople(): List<Person>
}
