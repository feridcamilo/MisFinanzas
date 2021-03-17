package com.android.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Place")
data class PlaceVO(
    @PrimaryKey
    val id: Int,
    val name: String,
    val latLng: String?,
    val enabled: Boolean
)
