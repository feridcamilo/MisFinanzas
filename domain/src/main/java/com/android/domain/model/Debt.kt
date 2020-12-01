package com.android.domain.model

import java.io.Serializable

data class Debt(
    val id: Int,
    val name: String,
    val enabled: Boolean
) : Serializable {
    override fun toString(): String {
        return name
    }
}
