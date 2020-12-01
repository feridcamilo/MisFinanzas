package com.android.data.remote.model

data class SendMasterDTO(
    val idCliente: Int,
    val tipo: Int,
    val maestros: List<MasterDTO>
)
