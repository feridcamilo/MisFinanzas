package com.android.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class CategoryVO(
    @PrimaryKey
    val id: Int,
    val name: String,
    val enabled: Boolean
)
