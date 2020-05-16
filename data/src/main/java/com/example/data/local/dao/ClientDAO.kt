package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.local.model.ClientVO

@Dao
interface ClientDAO {

    @Query("SELECT * FROM Client WHERE id = :id")
    fun getById(id: Int): ClientVO

    @Insert
    fun insert(vararg client: ClientVO)

    @Delete
    fun delete(client: ClientVO)
}
