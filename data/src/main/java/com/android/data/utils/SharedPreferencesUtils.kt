package com.android.data.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtils {
    companion object {
        private const val FILE_NAME = "kotlinsharedpreference"
        private const val AUTO_SYNC_ON_OPEN = "AUTO_SYNC_ON_OPEN"

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        }

        private fun getEditor(context: Context): SharedPreferences.Editor {
            return getSharedPreferences(context).edit()
        }

        fun setAutoSyncConfig(context: Context, value: Boolean) {
            getEditor(context).putBoolean(AUTO_SYNC_ON_OPEN, value).commit()
        }

        fun getAutoSyncConfig(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(AUTO_SYNC_ON_OPEN, false)
        }
    }
}
