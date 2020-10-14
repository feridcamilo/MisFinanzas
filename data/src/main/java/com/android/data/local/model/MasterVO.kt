package com.android.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MasterVO(
    val id: Int,
    val name: String,
    val enabled: Boolean
) : Parcelable {
    override fun toString(): String {
        return name
    }
}
