package com.android.data.local.repository

import com.android.data.local.RoomDataSource
import com.android.data.local.model.*
import java.util.*

class LocalRepositoryImp(private val dataSource: RoomDataSource) : ILocalRepository {

    override suspend fun getUser(): UserVO {
        return dataSource.getUser()
    }

    override suspend fun insertUser(user: UserVO) {
        dataSource.insertUser(user)
    }

    override suspend fun updateLastSyncMovements(date: Date) {
        dataSource.updateLastSyncMovements(date)
    }

    override suspend fun getLastSyncMovements(): Date? {
        return dataSource.getLastSyncMovements()
    }

    override suspend fun updateLastSyncMasters(date: Date) {
        dataSource.updateLastSyncMasters(date)
    }

    override suspend fun getLastSyncMasters(): Date? {
        return dataSource.getLastSyncMasters()
    }


    override suspend fun getBalance(query: String): BalanceVO {
        return dataSource.getBalance(query)
    }

    override suspend fun getMovements(): List<MovementVO> {
        return dataSource.getMovements()
    }

    override suspend fun getMovementsToSync(lastSync: Date): List<MovementVO> {
        return dataSource.getMovementsToSync(lastSync)
    }

    override suspend fun getMovementsToDelete(): List<Int> {
        return dataSource.getMovementsToDelete()
    }

    override suspend fun getDiscardedMovements(): List<Int> {
        return dataSource.getdiscardedMovements()
    }

    override suspend fun deleteMovementsFromWeb(ids: List<Int>) {
        dataSource.deleteMovementsFromWeb(ids)
    }

    override suspend fun deleteMovement(movement: MovementVO) {
        dataSource.deleteMovement(movement)
    }

    override suspend fun discardMovement(id: Int) {
        dataSource.discardMovement(id)
    }

    override suspend fun clearSyncedMovements(lastSync: Date) {
        dataSource.clearSyncedMovements(lastSync)
    }

    override suspend fun insertMovement(movement: MovementVO) {
        dataSource.insertMovement(movement)
    }

    override suspend fun insertMovements(movements: List<MovementVO>) {
        dataSource.insertMovements(movements)
    }


    override suspend fun clearDeletedMovements() {
        dataSource.clearDeletedMovements()
    }


    override suspend fun getCategories(): List<CategoryVO> {
        return dataSource.getCategories()
    }

    override suspend fun insertCategories(categories: List<CategoryVO>) {
        dataSource.insertCategories(categories)
    }

    override suspend fun getDebts(): List<DebtVO> {
        return dataSource.getDebts()
    }

    override suspend fun insertDebts(debts: List<DebtVO>) {
        dataSource.insertDebts(debts)
    }

    override suspend fun getPlaces(): List<PlaceVO> {
        return dataSource.getPlaces()
    }

    override suspend fun insertPlaces(places: List<PlaceVO>) {
        dataSource.insertPlaces(places)
    }

    override suspend fun getPeople(): List<PersonVO> {
        return dataSource.getPeople()
    }

    override suspend fun insertPeople(people: List<PersonVO>) {
        dataSource.insertPeople(people)
    }
}
