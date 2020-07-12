package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "User")
data class UserVO(
    @PrimaryKey
    val user: String,
    val clientId: Int,
    val name: String,
    val lastName: String,
    val mail: String
) : Serializable
