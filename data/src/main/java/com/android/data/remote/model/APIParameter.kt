package com.android.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class APIParameter(
    @SerialName("Nombre")
    var name: String,
    @SerialName("Valor")
    var value: String
)

@Serializable
data class APIParameterBody(
    @SerialName("parametros")
    val paramList: List<APIParameter>
)
