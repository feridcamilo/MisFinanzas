package com.android.misfinanzas.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MasterModel(
    val id: Int,
    val name: String,
    val enabled: Boolean
) : Parcelable {
    override fun toString(): String {
        return name
    }
}
