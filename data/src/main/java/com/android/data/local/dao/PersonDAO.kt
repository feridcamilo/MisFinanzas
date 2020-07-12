package com.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.data.local.model.PersonVO

@Dao
interface PersonDAO {

    @Query("SELECT * FROM Person WHERE id = :id")
    fun getById(id: Int): PersonVO

    @Query("SELECT * FROM Person")
    fun getAll(): List<PersonVO>

    @Query("SELECT * FROM Person WHERE enabled = 1")
    fun getAllActivated(): List<PersonVO>

    @Insert
    fun insert(vararg person: PersonVO)

    @Delete
    fun delete(person: PersonVO)
}
