package com.android.domain.repository

import com.android.domain.model.*
import java.util.*

interface MasterRepository {
    suspend fun updateLastSyncMasters(date: Date)
    suspend fun getLastSyncMasters(): Date?

    suspend fun getCategories(): List<Category>
    suspend fun insertCategories(categories: List<Master>)

    suspend fun getDebts(): List<Debt>
    suspend fun insertDebts(debts: List<Master>)

    suspend fun getPlaces(): List<Place>
    suspend fun insertPlaces(places: List<Master>)

    suspend fun getPeople(): List<Person>
    suspend fun insertPeople(people: List<Master>)

    suspend fun getCloudCategories(lastSync: Date?): List<Master>
    suspend fun getCloudDebts(lastSync: Date?): List<Master>
    suspend fun getCloudPlaces(lastSync: Date?): List<Master>
    suspend fun getCloudPeople(lastSync: Date?): List<Master>

    suspend fun sendCategoriesToCloud(masters: List<Master>): Boolean
    suspend fun sendPlacesToCloud(masters: List<Master>): Boolean
    suspend fun sendPeopleToCloud(masters: List<Master>): Boolean
    suspend fun sendDebtsToCloud(masters: List<Master>): Boolean
}
