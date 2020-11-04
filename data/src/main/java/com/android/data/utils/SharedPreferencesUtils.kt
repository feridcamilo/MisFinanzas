package com.android.data.utils

import android.content.Context
import android.content.SharedPreferences
import com.android.data.local.model.converters.DateConverter
import java.util.*

class SharedPreferencesUtils {
    companion object {
        private const val FILE_NAME = "kotlinsharedpreference"
        private const val AUTO_SYNC_ON_OPEN = "AUTO_SYNC_ON_OPEN"
        private const val SERVER_DATETIME = "SERVER_DATETIME"

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

        fun setServerDateTime(context: Context, value: Date) {
            val dateConverter = DateConverter()
            getEditor(context).putLong(SERVER_DATETIME, dateConverter.toTimestamp(value)!!).commit()
        }

        fun getServerDateTIme(context: Context): Date {
            val dateConverter = DateConverter()
            return dateConverter.toDate(getSharedPreferences(context).getLong(SERVER_DATETIME, 0))!!
        }
    }
}
