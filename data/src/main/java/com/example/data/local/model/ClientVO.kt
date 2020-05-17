package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Client")
data class ClientVO(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val lastName: String
)