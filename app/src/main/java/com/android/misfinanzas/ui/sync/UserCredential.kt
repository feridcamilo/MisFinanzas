package com.android.misfinanzas.ui.sync

import com.android.data.utils.StringUtils.Companion.EMPTY

data class UserCredential(
    val user: String = EMPTY,
    val password: String = EMPTY
)
