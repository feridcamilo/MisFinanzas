package com.android.misfinanzas.utils.permissions

import androidx.fragment.app.Fragment

fun Fragment.requestPermissions(vararg permission: Permission) =
    PermissionsRequester(this, arrayOf(*permission))
