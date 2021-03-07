package com.android.misfinanzas.models

import com.android.domain.utils.StringUtils.Companion.EMPTY
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

data class MovementModel(
    val idMovement: Int,
    val idType: Int,
    val value: BigDecimal,
    val description: String,
    val personId: Int?,
    val personName: String?,
    val placeId: Int?,
    val placeName: String?,
    val categoryId: Int?,
    val categoryName: String?,
    val date: Date?,
    val debtId: Int?,
    val debtName: String?,
    val dateEntry: Date?,
    val dateLastUpd: Date?
) : Serializable {

    companion object {
        fun getEmpty(): MovementModel {
            return MovementModel(
                0,
                0,
                BigDecimal.valueOf(0),
                EMPTY,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        }
    }

}
