package com.android.data.local.dao

import androidx.room.*
import com.android.data.local.model.DebtVO

@Dao
interface DebtDAO {

    @Query("SELECT * FROM Debt WHERE id = :id")
    suspend fun getById(id: Int): DebtVO

    @Query("SELECT * FROM Debt")
    suspend fun getAll(): List<DebtVO>

    @Query("SELECT * FROM Debt WHERE enabled = 1")
    suspend fun getAllActivated(): List<DebtVO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg debt: DebtVO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movements: List<DebtVO>)

    @Delete
    suspend fun delete(debt: DebtVO)
}
