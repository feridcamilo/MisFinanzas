package com.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.data.local.model.UserVO

@Dao
interface UserDAO {

    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getUser(): UserVO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserVO)
}
