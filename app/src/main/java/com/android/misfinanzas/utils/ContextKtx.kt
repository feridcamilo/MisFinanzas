package com.android.misfinanzas.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.android.domain.utils.StringUtils
import com.android.misfinanzas.R

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

fun Context.showExceptionMessage(tag: String, message: String, type: ErrorType = ErrorType.TYPE_APP) {
    showLongToast(message)
    val logMsg = when (type) {
        ErrorType.TYPE_APP -> StringUtils.EMPTY
        ErrorType.TYPE_ROOM -> getString(R.string.error_room, message)
        ErrorType.TYPE_RETROFIT -> getString(R.string.error_retrofit, message)
    }
    Log.e(tag, logMsg)
}

enum class ErrorType { TYPE_APP, TYPE_ROOM, TYPE_RETROFIT }

fun Context.isConnected(errorMessage: String?): Boolean {
    var isConnected = false
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw = connectivityManager.activeNetwork
    val actNw = connectivityManager.getNetworkCapabilities(nw)
    if (actNw != null) {
        isConnected = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    if (!isConnected && !errorMessage.isNullOrEmpty()) {
        showLongToast(errorMessage)
    }

    return isConnected
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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
