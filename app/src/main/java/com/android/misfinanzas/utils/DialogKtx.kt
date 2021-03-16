package com.android.misfinanzas.utils

import android.content.Context
import android.view.Gravity
import com.android.misfinanzas.R
import com.android.misfinanzas.utils.dialogs.CustomDialog

fun Context.showRationaleDialog(title: String, message: String, action: () -> Unit) {
    CustomDialog.Builder(this)
        .dialogType(CustomDialog.DialogType.ALERT_DIALOG)
        .title(title)
        .description(message)
        .actionText(getString(R.string.accept))
        .action(action)
        .screenPosition(Gravity.CENTER)
        .build()
        .show()
}