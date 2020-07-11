package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "Place")
data class PlaceVO(
    @PrimaryKey
    val id: Int,
    @SerializedName("Nombre")
    val name: String,
    @SerializedName("Activo")
    val enabled: Boolean
) : Serializable

data class PlaceVOBody(
    @SerializedName("getLugaresResult")
    val results: List<PlaceVO>
) : Serializable
