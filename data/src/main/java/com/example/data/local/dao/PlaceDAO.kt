package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.local.model.PlaceVO

@Dao
interface PlaceDAO {

    @Query("SELECT * FROM Place WHERE id = :id")
    fun getById(id: Int): PlaceVO

    @Query("SELECT * FROM Place")
    fun getAll(): List<PlaceVO>

    @Query("SELECT * FROM Place WHERE enabled = 1")
    fun getAllActivated(): List<PlaceVO>

    @Insert
    fun insert(vararg place: PlaceVO)

    @Delete
    fun delete(place: PlaceVO)
}
