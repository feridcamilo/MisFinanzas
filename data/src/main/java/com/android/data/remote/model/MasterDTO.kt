package com.android.data.remote.model

data class MasterDTO(
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
