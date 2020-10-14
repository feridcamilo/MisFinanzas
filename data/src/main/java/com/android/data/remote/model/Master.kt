package com.android.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Master(
    val Id: Int,
    val Nombre: String,
    val Activo: Boolean
) {
    companion object {
        const val TYPE_CATEGORY = 1
        const val TYPE_PLACE = 2
        const val TYPE_PERSON = 3
        const val TYPE_DEBT = 4
    }
}

data class SendMastersDTO(
    @SerializedName("recibirMaestrosResult")
    val results: Boolean
) : Serializable

data class SendMaster(
    val idCliente: Int,
    val tipo: Int,
    val maestros: List<Master>
)
