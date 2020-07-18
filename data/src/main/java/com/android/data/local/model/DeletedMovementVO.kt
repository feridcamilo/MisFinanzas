package com.android.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "DeletedMovement")
@Parcelize
data class DeletedMovementVO(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val idMovement: Int
) : Parcelable
