package com.android.data.local.dao

import androidx.room.*
import com.android.data.local.model.CategoryVO

@Dao
interface CategoryDAO {

    @Query("SELECT * FROM Category WHERE id = :id")
    suspend fun getById(id: Int): CategoryVO

    @Query("SELECT * FROM Category")
    suspend fun getAll(): List<CategoryVO>

    @Query("SELECT * FROM Category WHERE enabled = 1")
    suspend fun getAllActivated(): List<CategoryVO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg category: CategoryVO)

    @Delete
    suspend fun delete(category: CategoryVO)
}
