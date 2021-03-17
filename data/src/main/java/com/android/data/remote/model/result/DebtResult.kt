package com.android.data.remote.model.result

import com.android.data.remote.model.MasterDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DebtResult(
    @SerialName("getDeudasResult")
    val results: List<MasterDTO>
)
