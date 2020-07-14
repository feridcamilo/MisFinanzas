package com.android.data.local.dao

import androidx.room.*
import com.android.data.local.model.MovementVO

@Dao
interface MovementDAO {

    @Query("SELECT * FROM Movement ORDER BY date desc, dateEntry desc")
    suspend fun getAll(): List<MovementVO>

    @Query("SELECT * FROM Movement WHERE id = :id")
    suspend fun getById(id: Int): MovementVO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movement: MovementVO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movements: List<MovementVO>)

    @Delete
    suspend fun delete(movement: MovementVO)
}
