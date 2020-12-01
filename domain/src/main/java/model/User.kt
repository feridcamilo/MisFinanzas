package model

import java.io.Serializable
import java.util.*

data class User(
    val user: String,
    val clientId: Int,
    val name: String,
    val lastName: String,
    val email: String,
    val lastSyncMovements: Date?,
    val lastSyncMasters: Date?
) : Serializable