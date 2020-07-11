package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "User")
data class UserVO(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val clientId: Int,
    val email: String,
    val password: String
) : Serializable
