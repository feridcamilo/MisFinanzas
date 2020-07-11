package com.example.data.remote.model

import com.example.data.local.model.CategoryVO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryDTO(
    @SerializedName("getCategoriasResult")
    val results: List<CategoryVO>
) : Serializable
