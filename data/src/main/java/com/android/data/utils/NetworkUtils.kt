package com.android.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast

class NetworkUtils {
    companion object {
        fun isConnected(context: Context, errorMessage: String?): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

            val isConnected = activeNetwork?.isConnectedOrConnecting == true

            if (!isConnected && !errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }

            return isConnected
        }
    }
}
