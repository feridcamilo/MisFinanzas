package com.android.misfinanzas.utils

import android.view.View

fun View.visible(isVisible: Boolean = true) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.invisible(isInvisible: Boolean = true) {
    visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}
