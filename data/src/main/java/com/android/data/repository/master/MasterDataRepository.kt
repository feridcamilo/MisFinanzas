package com.android.data.repository.master

import com.android.data.repository.master.datasource.MasterCloudDataSource
import com.android.data.repository.master.datasource.MasterRoomDataSource
import com.android.data.repository.master.mappers.*
import com.android.domain.model.*
import com.android.domain.repository.MasterRepository
import java.util.*

class MasterDataRepository(
    private val roomDataSource: MasterRoomDataSource,
    private val cloudDataSource: MasterCloudDataSource,
    private val categoryMapper: CategoryDataMapper,
    private val debtMapper: DebtDataMapper,
    private val masterMapper: MasterDataMapper,
    private val personMapper: PersonDataMapper,
    private val placeMapper: PlaceDataMapper
) : MasterRepository {

    override suspend fun updateLastSyncMasters(date: Date) {
        roomDataSource.updateLastSyncMasters(date)
    }

    override suspend fun getLastSyncMasters(): Date? {
        return roomDataSource.getLastSyncMasters()
    }

    override suspend fun getCategories(): List<Category> {
        return roomDataSource.getCategories().map { categoryMapper.map(it) }
    }

    override suspend fun insertCategories(categories: List<Master>) {
        roomDataSource.insertCategories(categories.map { categoryMapper.mapToVO(categoryMapper.map(it)) })
    }

    override suspend fun getDebts(): List<Debt> {
        return roomDataSource.getDebts().map { debtMapper.map(it) }
    }

    override suspend fun insertDebts(debts: List<Master>) {
        roomDataSource.insertDebts(debts.map { debtMapper.mapToVO(debtMapper.map(it)) })
    }

    override suspend fun getPlaces(): List<Place> {
        return roomDataSource.getPlaces().map { placeMapper.map(it) }
    }

    override suspend fun insertPlaces(places: List<Master>) {
        roomDataSource.insertPlaces(places.map { placeMapper.mapToVO(placeMapper.map(it)) })
    }

    override suspend fun getPeople(): List<Person> {
        return roomDataSource.getPeople().map { personMapper.map(it) }
    }

    override suspend fun insertPeople(people: List<Master>) {
        roomDataSource.insertPeople(people.map { personMapper.mapToVO(personMapper.map(it)) })
    }

    override suspend fun getCloudCategories(lastSync: Date?): List<Master> {
        return cloudDataSource.getCategories(lastSync).map { masterMapper.map(it) }
    }

    override suspend fun getCloudDebts(lastSync: Date?): List<Master> {
        return cloudDataSource.getDebts(lastSync).map { masterMapper.map(it) }
    }

    override suspend fun getCloudPlaces(lastSync: Date?): List<Master> {
        return cloudDataSource.getPlaces(lastSync).map { masterMapper.map(it) }
    }

    override suspend fun getCloudPeople(lastSync: Date?): List<Master> {
        return cloudDataSource.getPeople(lastSync).map { masterMapper.map(it) }
    }

    override suspend fun sendCategoriesToCloud(masters: List<Master>): Boolean {
        return cloudDataSource.sendCategories(masters.map { masterMapper.mapToDTO(it) })
    }

    override suspend fun sendPlacesToCloud(masters: List<Master>): Boolean {
        return cloudDataSource.sendPlaces(masters.map { masterMapper.mapToDTO(it) })
    }

    override suspend fun sendPeopleToCloud(masters: List<Master>): Boolean {
        return cloudDataSource.sendPeople(masters.map { masterMapper.mapToDTO(it) })
    }

    override suspend fun sendDebtsToCloud(masters: List<Master>): Boolean {
        return cloudDataSource.sendDebts(masters.map { masterMapper.mapToDTO(it) })
    }

}
