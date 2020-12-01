package com.android.domain.model

import java.math.BigDecimal
import java.util.*

data class Movement(
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
) {
    companion object {
        fun getEmpty(): Movement {
            return Movement(0, 0, BigDecimal.valueOf(0), com.android.domain.utils.StringUtils.EMPTY, null, null, null, null, null, null, null)
        }
    }
}
