package com.android.misfinanzas.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class MasterModel(
    val id: Int,
    val name: String,
    val latLng: LatLng?,
    val enabled: Boolean
) : Parcelable, Serializable {

    override fun toString(): String {
        return name
    }

}
