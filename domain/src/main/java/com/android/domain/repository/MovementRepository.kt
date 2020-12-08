package com.android.domain.repository

import com.android.domain.model.Movement
import java.util.*

interface MovementRepository {
    suspend fun updateLastSyncMovements(date: Date)
    suspend fun getLastSyncMovements(): Date?

    suspend fun getMovements(): List<Movement>
    suspend fun insertMovement(movement: Movement)
    suspend fun insertMovements(movements: List<Movement>)
    suspend fun deleteMovement(movement: Movement)

    suspend fun getDiscardedMovements(): List<Int>
    suspend fun discardMovement(id: Int)

    suspend fun getMovementsToSync(lastSync: Date): List<Movement>
    suspend fun getMovementsToDelete(): List<Int>
    suspend fun deleteMovementsFromWeb(ids: List<Int>)
    suspend fun clearSyncedMovements(lastSync: Date)

    suspend fun clearDeletedMovements()
    suspend fun clearDiscardedMovements()

    suspend fun getCloudMovements(lastSync: Date?): List<Movement>
    suspend fun getCloudDeletedMovements(lastSync: Date?): List<Int>
    suspend fun deleteMovementsInCloud(ids: List<Int>): Boolean
    suspend fun sendMovementsToCloud(movements: List<Movement>): Boolean
}
