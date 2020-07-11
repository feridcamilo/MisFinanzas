package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.local.model.CategoryVO

@Dao
interface CategoryDAO {

    @Query("SELECT * FROM Category WHERE id = :id")
    fun getById(id: Int): CategoryVO

    @Query("SELECT * FROM Category")
    fun getAll(): List<CategoryVO>

    @Query("SELECT * FROM Category WHERE enabled = 1")
    fun getAllActives(): List<CategoryVO>

    @Insert
    fun insert(vararg category: CategoryVO)

    @Delete
    fun delete(category: CategoryVO)
}
