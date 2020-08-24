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

    @Query("UPDATE User SET lastSyncMovements = :date ")
    suspend fun updateLastSyncMovements(date: Date)

    @Query("SELECT lastSyncMovements FROM User LIMIT 1")
    suspend fun getLastSyncMovements(): Date?

    @Query("UPDATE User SET lastSyncMasters = :date ")
    suspend fun updateLastSyncMasters(date: Date)

    @Query("SELECT lastSyncMasters FROM User LIMIT 1")
    suspend fun getLastSyncMasters(): Date?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserVO)
}
