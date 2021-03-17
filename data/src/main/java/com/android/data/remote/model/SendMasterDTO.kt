package com.android.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SendMasterDTO(
    val idCliente: Int,
    val tipo: Int,
    val maestros: List<MasterDTO>
)
