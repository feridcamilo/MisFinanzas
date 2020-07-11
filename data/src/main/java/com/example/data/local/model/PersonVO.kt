package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "Person")
data class PersonVO(
    @PrimaryKey
    val id: Int,
    @SerializedName("Nombre")
    val name: String,
    @SerializedName("Activo")
    val enabled: Boolean
) : Serializable

data class PersonaVOBody(
    @SerializedName("getPersonaResult")
    val results: List<PersonVO>
) : Serializable
