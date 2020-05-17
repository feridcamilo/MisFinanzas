package com.example.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Place",
    foreignKeys = [ForeignKey(
        entity = ClientVO::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("clientId")
    )]
)
class PlaceVO(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val clientId: Int,
    val name: String
)
