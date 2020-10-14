package com.android.data.local.dao

import androidx.room.*
import com.android.data.local.model.PersonVO

@Dao
interface PersonDAO {

    @Query("SELECT * FROM Person WHERE id = :id")
    suspend fun getById(id: Int): PersonVO

    @Query("SELECT * FROM Person ORDER BY name")
    suspend fun getAll(): List<PersonVO>

    @Query("SELECT * FROM Person WHERE enabled = 1 ORDER BY name")
    suspend fun getAllActivated(): List<PersonVO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg person: PersonVO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movements: List<PersonVO>)

    @Delete
    suspend fun delete(person: PersonVO)
}
