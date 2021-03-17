package com.android.domain.model

data class Place(
    val id: Int,
    val name: String,
    val latLng: String?,
    val enabled: Boolean
) {
    override fun toString(): String {
        return name
    }
}
