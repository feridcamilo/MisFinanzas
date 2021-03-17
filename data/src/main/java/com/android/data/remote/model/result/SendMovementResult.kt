package com.android.data.remote.model.result

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMovementResult(
    @SerialName("recibirMovimientosResult")
    val results: Boolean
)
