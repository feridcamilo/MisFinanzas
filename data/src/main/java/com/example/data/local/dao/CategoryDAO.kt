package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.local.model.CategoryVO
import com.example.data.local.model.ClientVO

@Dao
interface CategoryDAO {

    @Query("SELECT * FROM Category WHERE id = :id")
    fun getById(id: Int): ClientVO

    @Query("SELECT * FROM Category WHERE clientId = :clientId")
    fun getAllByClient(clientId: Int): List<ClientVO>

    @Insert
    fun insert(vararg category: CategoryVO)

    @Delete
    fun delete(category: CategoryVO)
}
