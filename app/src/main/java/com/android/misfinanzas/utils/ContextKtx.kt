package com.android.misfinanzas.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.widget.Toast

fun Context.showShortToast(stringResId: Int) {
    showShortToast(getString(stringResId))
}

fun Context.showShortToast(text: String) {
    showToast(text, Toast.LENGTH_SHORT)
}

fun Context.showLongToast(stringResId: Int) {
    showLongToast(getString(stringResId))
}

fun Context.showLongToast(text: String) {
    showToast(text, Toast.LENGTH_LONG)
}

private fun Context.showToast(text: String, duration: Int) {
    Toast.makeText(this, text, duration).show()
}

fun Context.isConnected(errorMessage: String?): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

    val isConnected = activeNetwork?.isConnectedOrConnecting == true

    if (!isConnected && !errorMessage.isNullOrEmpty()) {
        showLongToast(errorMessage)
    }

    return isConnected
}

fun Context.openURL(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.setPackage("com.android.chrome")
    try {
        startActivity(intent)
    } catch (ex: ActivityNotFoundException) {
        // Chrome browser presumably not installed so allow user to choose instead
        intent.setPackage(null)
        startActivity(intent)
    }
}

