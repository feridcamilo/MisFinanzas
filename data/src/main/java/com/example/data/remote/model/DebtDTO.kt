package com.example.data.remote.model

import com.example.data.local.model.DebtVO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DebtDTO(
    @SerializedName("getDeudasResult")
    val results: List<DebtVO>
) : Serializable
