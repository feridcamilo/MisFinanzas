package com.android.data.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

class AppUtils {
    companion object {
        fun openURL(context: Context, url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.android.chrome")
            try {
                context.startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                // Chrome browser presumably not installed so allow user to choose instead
                intent.setPackage(null)
                context.startActivity(intent)
            }
        }
    }
}
