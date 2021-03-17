package com.android.data.remote.model.result

import com.android.data.remote.model.MasterDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonResult(
    @SerialName("getPersonasResult")
    val results: List<MasterDTO>
)
