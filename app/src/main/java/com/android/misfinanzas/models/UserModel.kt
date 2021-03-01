package com.android.misfinanzas.models

import java.util.*

data class UserModel(
    val user: String,
    val clientId: Int,
    val name: String,
    val lastName: String,
    val email: String,
    val lastSyncMovements: Date?,
    val lastSyncMasters: Date?
) {

    override fun toString(): String {
        return "${this.name} ${this.lastName}"
    }

}
