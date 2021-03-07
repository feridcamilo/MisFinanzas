package com.android.misfinanzas.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class MasterModel(
    val id: Int,
    val name: String,
    val enabled: Boolean
) : Parcelable, Serializable {

    override fun toString(): String {
        return name
    }

}
