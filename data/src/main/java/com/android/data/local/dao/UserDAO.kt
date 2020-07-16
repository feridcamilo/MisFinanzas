package com.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.data.local.model.UserVO
import java.util.*

@Dao
interface UserDAO {

    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getUser(): UserVO

    @Query("UPDATE User SET lastSync = :date ")
    suspend fun updateLastSync(date: Date)

    @Query("SELECT lastSync FROM User LIMIT 1")
    suspend fun getLastSync(): Date?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserVO)
}
