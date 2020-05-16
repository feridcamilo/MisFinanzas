package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.local.model.UserVO

@Dao
interface UserDAO {

    @Query("SELECT * FROM User WHERE id = :id")
    fun getById(id: Int): UserVO

    @Query("SELECT * FROM User WHERE clientId = :clientId")
    fun getByClient(clientId: Int): UserVO

    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    fun getByEmailAndPass(email: String, password: String): UserVO

    @Insert
    fun insert(vararg user: UserVO)

    @Delete
    fun delete(user: UserVO)
}
