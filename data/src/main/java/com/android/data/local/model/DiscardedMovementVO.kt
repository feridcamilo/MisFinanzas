package com.android.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DiscardedMovement")
data class DiscardedMovementVO(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val idMovement: Int
)
