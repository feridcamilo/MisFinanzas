package com.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.data.local.model.BalanceVO

@Dao
interface BalanceDAO {

    @Query("SELECT * FROM Balance LIMIT 1")
    suspend fun getBalance(): BalanceVO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(balance: BalanceVO)
}
