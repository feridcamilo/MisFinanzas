package com.android.misfinanzas.models

import java.io.Serializable
import java.math.BigDecimal
import java.util.*

data class MovementModel(
    val idMovement: Int,
    val idType: Int,
    val value: BigDecimal,
    val description: String,
    val personId: Int?,
    val placeId: Int?,
    val categoryId: Int?,
    val date: Date?,
    val debtId: Int?,
    val dateEntry: Date?,
    val dateLastUpd: Date?
) : Serializable {

    companion object {
        fun getEmpty(): MovementModel {
            return MovementModel(0, 0, BigDecimal.valueOf(0), com.android.domain.utils.StringUtils.EMPTY, null, null, null, null, null, null, null)
        }
    }

}
