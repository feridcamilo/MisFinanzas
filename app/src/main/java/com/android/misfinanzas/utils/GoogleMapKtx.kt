package com.android.misfinanzas.utils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

fun GoogleMap.addMarker(latLng: LatLng) {
    val marker = MarkerOptions().position(latLng).draggable(true)
    addMarker(marker)
}

fun GoogleMap.centerMapCamera(latLng: LatLng) {
    val cameraPosition = CameraPosition.Builder()
        .target(latLng)
        .zoom(17f)
        .build()
    // For zooming automatically to the location of the marker
    animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}