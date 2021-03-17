package com.android.misfinanzas.utils

import com.android.domain.utils.StringUtils.Companion.COMMA
import com.google.android.gms.maps.model.LatLng

fun String.toLatLng(): LatLng? {
    if (this.isEmpty()) return null
    val parts = this.split(COMMA)
    val lat = parts.getOrNull(0)?.toDouble() ?: 0.0
    val lng = parts.getOrNull(1)?.toDouble() ?: 0.0
    return LatLng(lat, lng)
}

fun LatLng.toLatLngString(): String {
    return "${this.latitude},${this.longitude}"
}
