package com.android.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserVO(
    @PrimaryKey
    val user: String,
    val clientId: Int,
    val name: String,
    val lastName: String,
    val email: String
)
