package com.android.domain.model

import com.android.domain.utils.StringUtils.Companion.EMPTY
import java.util.*

data class User(
    val user: String,
    val clientId: Int,
    val name: String,
    val lastName: String,
    val email: String,
    val lastSyncMovements: Date?,
    val lastSyncMasters: Date?
) {
    override fun toString(): String {
        return name + EMPTY + lastName
    }
}
