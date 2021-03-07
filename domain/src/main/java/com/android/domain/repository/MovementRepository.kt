package com.android.domain.repository

import com.android.domain.model.Movement
import com.android.domain.model.MovementDetailed
import java.util.*

interface MovementRepository {
    suspend fun updateLastSyncMovements(date: Date)
    suspend fun getLastSyncMovements(): Date?

    suspend fun download()
    suspend fun upload()

    suspend fun getMovements(): List<Movement>
    suspend fun getMovementsDetailed(): List<MovementDetailed>
    suspend fun insertMovement(movement: Movement)
    suspend fun deleteMovement(movement: Movement)

    suspend fun getDiscardedMovements(): List<Int>
    suspend fun discardMovement(id: Int)
    suspend fun clearDiscardedMovements()
}
