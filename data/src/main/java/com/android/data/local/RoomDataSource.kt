package com.android.data.local

import androidx.sqlite.db.SimpleSQLiteQuery
import com.android.data.UserSesion
import com.android.data.local.db.AppDatabase
import com.android.data.local.model.*
import java.util.*

class RoomDataSource(private val db: AppDatabase) {


    suspend fun updateLastSyncMovements(date: Date) {
        UserSesion.updateLastSyncMovements(date)
        return db.userDao().updateLastSyncMovements(date)
    }

    suspend fun getLastSyncMovements(): Date? {
        return db.userDao().getLastSyncMovements()
    }

    suspend fun updateLastSyncMasters(date: Date) {
        UserSesion.updateLastSyncMasters(date)
        return db.userDao().updateLastSyncMasters(date)
    }

    suspend fun getLastSyncMasters(): Date? {
        return db.userDao().getLastSyncMasters()
    }


    suspend fun getBalance(query: String): BalanceVO {
        val sqlQuery = SimpleSQLiteQuery(query)
        return db.movementDAO().getBalance(sqlQuery)
    }

    suspend fun getMovements(): List<MovementVO> {
        return db.movementDAO().getAll()
    }

    suspend fun getMovementsToSync(lastSync: Date): List<MovementVO> {
        return db.movementDAO().getAllToSync(lastSync)
    }

    suspend fun getMovementsToDelete(): List<Int> {
        return db.deletedMovementDAO().getAll()
    }

    suspend fun getdiscardedMovements(): List<Int> {
        return db.discardedMovementDAO().getAll()
    }

    suspend fun deleteMovementsFromWeb(ids: List<Int>) {
        db.movementDAO().deletedFromWeb(ids)
    }

    suspend fun deleteMovement(movement: MovementVO) {
        db.movementDAO().delete(movement)
        val isLocal = movement.dateEntry!! > UserSesion.getUser()?.lastSyncMovements
        if (!isLocal) {
            db.deletedMovementDAO().insert(DeletedMovementVO(0, movement.idMovement))
        }
    }

    suspend fun discardMovement(id: Int) {
        db.discardedMovementDAO().insert(DiscardedMovementVO(0, id))
    }

    suspend fun clearSyncedMovements(lastSync: Date) {
        db.movementDAO().deleteAllNews(lastSync)
    }

    suspend fun insertMovement(movement: MovementVO) {
        db.movementDAO().insert(movement)
    }

    suspend fun insertMovements(movements: List<MovementVO>) {
        db.movementDAO().insertAll(movements)
    }


    suspend fun clearDeletedMovements() {
        db.deletedMovementDAO().cleanTable()
    }

    suspend fun clearDiscardedMovements() {
        db.discardedMovementDAO().cleanTable()
    }


    suspend fun getCategories(): List<CategoryVO> {
        return db.categoryDAO().getAll()
    }

    suspend fun insertCategories(categories: List<CategoryVO>) {
        db.categoryDAO().insertAll(categories)
    }

    suspend fun getDebts(): List<DebtVO> {
        return db.debtDAO().getAll()
    }

    suspend fun insertDebts(debts: List<DebtVO>) {
        db.debtDAO().insertAll(debts)
    }

    suspend fun getPlaces(): List<PlaceVO> {
        return db.placeDAO().getAll()
    }

    suspend fun insertPlaces(places: List<PlaceVO>) {
        db.placeDAO().insertAll(places)
    }

    suspend fun getPeople(): List<PersonVO> {
        return db.personDAO().getAll()
    }

    suspend fun insertPeople(people: List<PersonVO>) {
        db.personDAO().insertAll(people)
    }
}
