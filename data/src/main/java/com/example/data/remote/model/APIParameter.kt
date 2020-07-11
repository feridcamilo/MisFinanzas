package com.example.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class APIParameter(
    @SerializedName("Nombre")
    var name: String,
    @SerializedName("Valor")
    var value: String
) : Serializable

data class APIParameterBody(
    @SerializedName("parametros")
    val paramList: List<APIParameter>
) : Serializable
