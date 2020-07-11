package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "Debt")
data class DebtVO(
    @PrimaryKey
    val id: Int,
    @SerializedName("Nombre")
    val name: String,
    @SerializedName("Activo")
    val enabled: Boolean
) : Serializable

data class DebtVOBody(
    @SerializedName("getDeudasResult")
    val results: List<DebtVO>
) : Serializable
