package com.android.data.repository.movement

import com.android.data.repository.movement.datasource.MovementCloudDataSource
import com.android.data.repository.movement.datasource.MovementRoomDataSource
import com.android.data.repository.movement.mappers.MovementDataMapper
import com.android.domain.model.Movement
import com.android.domain.model.MovementDetailed
import com.android.domain.repository.MovementRepository
import java.util.*

class MovementDataRepository(
    private val cloudDataSource: MovementCloudDataSource,
    private val roomDataSource: MovementRoomDataSource,
    private val mapper: MovementDataMapper
) : MovementRepository {

    override suspend fun updateLastSyncMovements(date: Date) {
        return roomDataSource.updateLastSyncMovements(date)
    }

    override suspend fun getLastSyncMovements(): Date? {
        return roomDataSource.getLastSyncMovements()
    }

    override suspend fun download() {
        val lastSync = getLastSyncMovements()

        val movements = cloudDataSource.getMovements(lastSync)
        if (movements.isNotEmpty()) {
            roomDataSource.insertMovements(movements.map { mapper.mapToVO(it) })
        }

        if (lastSync != null) {
            val idsToDeleteInLocal = cloudDataSource.getDeletedMovements(lastSync)
            roomDataSource.deleteMovementsFromWeb(idsToDeleteInLocal)
        }
    }

    override suspend fun upload() {
        val lastSync = getLastSyncMovements()

        if (lastSync != null) {
            val idsToDeleteInWeb = roomDataSource.getMovementsToDelete()
            if (idsToDeleteInWeb.isNotEmpty()) {
                if (cloudDataSource.deleteMovements(idsToDeleteInWeb)) {
                    roomDataSource.clearDeletedMovements()
                }
            }

            val movementsToSend = roomDataSource.getMovementsToSync(lastSync)
            if (movementsToSend.isNotEmpty()) {
                if (cloudDataSource.sendMovements(movementsToSend.map { mapper.mapToDTO(it, lastSync) })) {
                    //Delete all news movements from local to allow download them with the right movementId from web
                    roomDataSource.clearSyncedMovements(lastSync)
                }
            }
        }
    }

    override suspend fun getMovements(): List<Movement> {
        return roomDataSource.getMovements().map { mapper.map(it) }
    }

    override suspend fun getMovementsDetailed(): List<MovementDetailed> {
        return roomDataSource.getMovementsDetailed().map { mapper.map(it) }
    }

    override suspend fun insertMovement(movement: Movement) {
        roomDataSource.insertMovement(mapper.mapToVO(movement))
    }

    override suspend fun deleteMovement(movement: Movement) {
        roomDataSource.deleteMovement(mapper.mapToVO(movement))
    }

    override suspend fun getDiscardedMovements(): List<Int> {
        return roomDataSource.getDiscardedMovements()
    }

    override suspend fun discardMovement(id: Int) {
        roomDataSource.discardMovement(id)
    }

    override suspend fun clearDiscardedMovements() {
        roomDataSource.clearDiscardedMovements()
    }

}
