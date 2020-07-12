package com.android.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Person")
data class PersonVO(
    @PrimaryKey
    val id: Int,
    val name: String,
    val enabled: Boolean
)
