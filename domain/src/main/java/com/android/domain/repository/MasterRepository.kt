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

    suspend fun getCloudCategories(clientId: String, lastSync: Date?): List<Master>
    suspend fun getCloudDebts(clientId: String, lastSync: Date?): List<Master>
    suspend fun getCloudPlaces(clientId: String, lastSync: Date?): List<Master>
    suspend fun getCloudPeople(clientId: String, lastSync: Date?): List<Master>

    suspend fun sendCategoriesToCloud(clientId: String, masters: List<Master>): Boolean
    suspend fun sendPlacesToCloud(clientId: String, masters: List<Master>): Boolean
    suspend fun sendPeopleToCloud(clientId: String, masters: List<Master>): Boolean
    suspend fun sendDebtsToCloud(clientId: String, masters: List<Master>): Boolean
}
