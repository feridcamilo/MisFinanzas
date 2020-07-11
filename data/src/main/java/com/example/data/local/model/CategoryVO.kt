package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "Category")
data class CategoryVO(
    @PrimaryKey
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Nombre")
    val name: String,
    @SerializedName("Activo")
    val enabled: Boolean
) : Serializable

data class CategoryVOBody(
    @SerializedName("getCategoriasResult")
    val results: List<CategoryVO>
) : Serializable
