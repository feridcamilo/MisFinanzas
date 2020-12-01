package com.android.data.repository.movement

import com.android.data.repository.movement.datasource.MovementCloudDataSource
import com.android.data.repository.movement.datasource.MovementRoomDataSource
import com.android.data.repository.movement.mappers.MovementDataMapper
import com.android.domain.model.Movement
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

    override suspend fun getMovements(): List<Movement> {
        return roomDataSource.getMovements().map { mapper.map(it) }
    }

    override suspend fun insertMovement(movement: Movement) {
        roomDataSource.insertMovement(mapper.mapToVO(movement))
    }

    override suspend fun insertMovements(movements: List<Movement>) {
        roomDataSource.insertMovements(movements.map { mapper.mapToVO(it) })
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

    override suspend fun getMovementsToSync(lastSync: Date): List<Movement> {
        return roomDataSource.getMovementsToSync(lastSync).map { mapper.map(it) }
    }

    override suspend fun getMovementsToDelete(): List<Int> {
        return roomDataSource.getMovementsToDelete()
    }

    override suspend fun deleteMovementsFromWeb(ids: List<Int>) {
        roomDataSource.deleteMovementsFromWeb(ids)
    }

    override suspend fun clearSyncedMovements(lastSync: Date) {
        roomDataSource.clearSyncedMovements(lastSync)
    }

    override suspend fun clearDeletedMovements() {
        roomDataSource.clearDeletedMovements()
    }

    override suspend fun clearDiscardedMovements() {
        roomDataSource.clearDiscardedMovements()
    }

    override suspend fun getCloudMovements(clientId: String, lastSync: Date?): List<Movement> {
        return cloudDataSource.getMovements(clientId, lastSync).map { mapper.map(it) }
    }

    override suspend fun getCloudDeletedMovements(clientId: String, lastSync: Date?): List<Int> {
        return cloudDataSource.getDeletedMovements(clientId, lastSync)
    }

    override suspend fun deleteMovementsInCloud(ids: List<Int>): Boolean {
        return cloudDataSource.deleteMovements(ids)
    }

    override suspend fun sendMovementsToCloud(clientId: String, movements: List<Movement>): Boolean {
        return cloudDataSource.sendMovements(clientId, movements.map { mapper.mapToDTO(it) })
    }

}
