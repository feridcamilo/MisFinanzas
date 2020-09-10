package com.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.data.local.model.DiscardedMovementVO

@Dao
interface DiscardedMovementDAO {

    @Query("SELECT idMovement FROM DiscardedMovement")
    suspend fun getAll(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(discardedMovementVO: DiscardedMovementVO)

    @Query("DELETE FROM DiscardedMovement")
    suspend fun cleanTable()
}
