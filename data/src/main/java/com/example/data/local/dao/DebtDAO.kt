package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.local.model.DebtVO

@Dao
interface DebtDAO {

    @Query("SELECT * FROM Debt WHERE id = :id")
    fun getById(id: Int): DebtVO

    @Query("SELECT * FROM Debt WHERE clientId = :clientId")
    fun getAllByClient(clientId: Int): List<DebtVO>

    @Insert
    fun insert(vararg debt: DebtVO)

    @Delete
    fun delete(debt: DebtVO)
}
