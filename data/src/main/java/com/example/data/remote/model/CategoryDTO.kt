package com.example.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryDTO(
    @SerializedName("getCategoriasResult")
    val results: List<Master>
) : Serializable
