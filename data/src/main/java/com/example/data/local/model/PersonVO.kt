package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Person")
data class PersonVO(
    @PrimaryKey
    val id: Int,
    val name: String,
    val enabled: Boolean
) : Serializable
