package com.android.domain.model

data class Master(
    val id: Int,
    val name: String,
    val enabled: Boolean
) {

    companion object {
        const val TYPE_CATEGORY = 1
        const val TYPE_PLACE = 2
        const val TYPE_PERSON = 3
        const val TYPE_DEBT = 4
    }

    override fun toString(): String {
        return name
    }
}
