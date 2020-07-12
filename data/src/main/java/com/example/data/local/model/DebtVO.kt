package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Debt")
data class DebtVO(
    @PrimaryKey
    val id: Int,
    val name: String,
    val enabled: Boolean
) : Serializable
