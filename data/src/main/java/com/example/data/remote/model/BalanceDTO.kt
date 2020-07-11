package com.example.data.remote.model

import com.example.data.local.model.BalanceVO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BalanceDTO(
    @SerializedName("getSaldosResult")
    val result: BalanceVO
) : Serializable
