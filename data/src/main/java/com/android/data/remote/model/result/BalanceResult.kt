package com.android.data.remote.model.result

import com.android.data.remote.model.BalanceDTO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BalanceResult(
    @SerializedName("getSaldosResult")
    val result: BalanceDTO
) : Serializable
