package com.android.data.local.dao

import androidx.room.*
import com.android.data.local.model.PlaceVO

@Dao
interface PlaceDAO {

    @Query("SELECT * FROM Place WHERE id = :id")
    suspend fun getById(id: Int): PlaceVO

    @Query("SELECT * FROM Place")
    suspend fun getAll(): List<PlaceVO>

    @Query("SELECT * FROM Place WHERE enabled = 1")
    suspend fun getAllActivated(): List<PlaceVO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg place: PlaceVO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movements: List<PlaceVO>)

    @Delete
    suspend fun delete(place: PlaceVO)
}
