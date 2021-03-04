package com.android.data.repository.movement.datasource

import com.android.data.local.db.AppDatabase
import com.android.data.local.model.DeletedMovementVO
import com.android.data.local.model.DiscardedMovementVO
import com.android.data.local.model.MovementVO
import com.android.domain.UserSesion
import java.util.*

class MovementRoomDataSource(private val db: AppDatabase) {

    suspend fun updateLastSyncMovements(date: Date) {
        UserSesion.updateLastSyncMovements(date)
        return db.userDao().updateLastSyncMovements(date)
    }

    suspend fun getLastSyncMovements(): Date? {
        return db.userDao().getLastSyncMovements()
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

    suspend fun getDiscardedMovements(): List<Int> {
        return db.discardedMovementDAO().getAll()
    }

    suspend fun deleteMovementsFromWeb(ids: List<Int>) {
        db.movementDAO().deletedFromWeb(ids)
    }

    suspend fun deleteMovement(movement: MovementVO) {
        db.movementDAO().delete(movement)
        val isLocal = movement.dateEntry!! > UserSesion.getUser().lastSyncMovements
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

}
