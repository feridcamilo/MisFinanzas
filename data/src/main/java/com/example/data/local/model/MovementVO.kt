package com.example.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

@Entity(
    tableName = "Movement",
    foreignKeys = [ForeignKey(
        entity = PersonVO::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("personId")
    ), ForeignKey(
        entity = PlaceVO::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("placeId")
    ), ForeignKey(
        entity = CategoryVO::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId")
    ), ForeignKey(
        entity = DebtVO::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("debtId")
    )]
)
data class MovementVO(
    @PrimaryKey
    val id: Int,
    val idType: MovementType,
    val value: BigDecimal,
    val description: String,
    val personId: Int?,
    val placeId: Int?,
    val categoryId: Int,
    val date: Date,
    val debtId: Int?,
    val dateEntry: Date,
    val dateLastUpd: Date?
) : Serializable
