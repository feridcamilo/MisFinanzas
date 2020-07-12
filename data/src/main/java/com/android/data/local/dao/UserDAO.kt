package com.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.data.local.model.UserVO

@Dao
interface UserDAO {

    @Query("SELECT * FROM User WHERE clientId = :clientId")
    fun getByClientId(clientId: Int): UserVO

    @Insert
    fun insert(vararg user: UserVO)

    @Delete
    fun delete(user: UserVO)
}
