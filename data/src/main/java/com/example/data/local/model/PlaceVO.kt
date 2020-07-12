package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Place")
data class PlaceVO(
    @PrimaryKey
    val id: Int,
    val name: String,
    val enabled: Boolean
) : Serializable
