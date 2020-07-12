package com.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.data.local.model.MovementVO

@Dao
interface MovementDAO {

    @Query("SELECT * FROM Movement WHERE id = :id")
    fun getById(id: Int): MovementVO

    @Insert
    fun insert(vararg movement: MovementVO)

    @Delete
    fun delete(movement: MovementVO)
}