package com.android.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Debt")
@Parcelize
data class DebtVO(
    @PrimaryKey
    val id: Int,
    val name: String,
    val enabled: Boolean
) : Parcelable {
    override fun toString(): String {
        return name
    }
}
