package com.android.data.local.dao

import androidx.room.*
import com.android.data.local.model.MovementVO

@Dao
interface MovementDAO {

    @Query("SELECT * FROM Movement ORDER BY date desc, dateEntry desc")
    suspend fun getAll(): List<MovementVO>

    @Query("SELECT * FROM Movement WHERE synced = 0")
    suspend fun getAllToSync(): List<MovementVO>

    @Query("UPDATE Movement SET synced = 1")
    suspend fun updateAllSynced()
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movement: MovementVO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movements: List<MovementVO>)

    @Delete
    suspend fun delete(movement: MovementVO)
}
