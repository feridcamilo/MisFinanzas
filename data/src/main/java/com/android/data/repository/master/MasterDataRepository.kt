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
    private val personMapper: PersonDataMapper,
    private val placeMapper: PlaceDataMapper,
    private val masterMapper: MasterDataMapper
) : MasterRepository {

    override suspend fun updateLastSyncMasters(date: Date) {
        roomDataSource.updateLastSyncMasters(date)
    }

    override suspend fun getLastSyncMasters(): Date? {
        return roomDataSource.getLastSyncMasters()
    }

    override suspend fun download() {
        val lastSync = getLastSyncMasters()

        val people = cloudDataSource.getPeople(lastSync)
        roomDataSource.insertPeople(people.map { personMapper.mapToVO(it) })

        val places = cloudDataSource.getPlaces(lastSync)
        roomDataSource.insertPlaces(places.map { placeMapper.mapToVO(it) })

        val categories = cloudDataSource.getCategories(lastSync)
        roomDataSource.insertCategories(categories.map { categoryMapper.mapToVO(it) })

        val debts = cloudDataSource.getDebts(lastSync)
        roomDataSource.insertDebts(debts.map { debtMapper.mapToVO(it) })
    }

    override suspend fun saveMasterOnCloud(master: Master, type: Int): Boolean {
        return cloudDataSource.sendMaster(masterMapper.mapToDTO(master), type)
    }

    /*
    override suspend fun upload() {

        val categories = roomDataSource.getCategoriesToSync()
        cloudDataSource.sendCategories(categories.map { categoryMapper.mapToDTO(it) })

        val places = roomDataSource.getPlacesToSync()
        cloudDataSource.sendPlaces(masters.map { places.mapToDTO(it) })

        val people = roomDataSource.getPeopleToSync()
        cloudDataSource.sendPeople(masters.map { people.mapToDTO(it) })

        val debts = roomDataSource.getDebtsToSync()
        cloudDataSource.sendDebts(masters.map { debts.mapToDTO(it) })
    }
    */

    override suspend fun getCategories(): List<Category> {
        return roomDataSource.getCategories().map { categoryMapper.map(it) }
    }

    override suspend fun getDebts(): List<Debt> {
        return roomDataSource.getDebts().map { debtMapper.map(it) }
    }

    override suspend fun getPlaces(): List<Place> {
        return roomDataSource.getPlaces().map { placeMapper.map(it) }
    }

    override suspend fun getPeople(): List<Person> {
        return roomDataSource.getPeople().map { personMapper.map(it) }
    }

}
