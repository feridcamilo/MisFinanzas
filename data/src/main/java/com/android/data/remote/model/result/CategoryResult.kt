package com.android.data.remote.model.result

import com.android.data.remote.model.MasterDTO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryResult(
    @SerializedName("getCategoriasResult")
    val results: List<MasterDTO>
) : Serializable
