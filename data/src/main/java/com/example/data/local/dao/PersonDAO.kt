package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.local.model.PersonVO

@Dao
interface PersonDAO {

    @Query("SELECT * FROM Person WHERE id = :id")
    fun getById(id: Int): PersonVO

    @Insert
    fun insert(vararg person: PersonVO)

    @Delete
    fun delete(person: PersonVO)
}
