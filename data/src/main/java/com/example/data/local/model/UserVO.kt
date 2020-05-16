package com.example.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "User",
    foreignKeys = [ForeignKey(
        entity = ClientVO::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("clientId")
    )]
)
data class UserVO(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val clientId: Int,
    val email: String,
    val password: String
)
