package com.android.data.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.*

fun SharedPreferences.getStringOrNull(key: String): String? {
    return getString(key, null)
}

fun SharedPreferences.saveString(key: String, value: String?) {
    return edit { putString(key, value) }
}

fun SharedPreferences.getBoolean(key: String): Boolean {
    return getBoolean(key, false)
}

fun SharedPreferences.saveBoolean(key: String, value: Boolean) {
    return edit { putBoolean(key, value) }
}

fun SharedPreferences.getLong(key: String): Long {
    return getLong(key, 0)
}

fun SharedPreferences.getLongOrNull(key: String): Long? {
    val value = getLong(key)
    return if (value == 0L) null else value
}

fun SharedPreferences.saveLong(key: String, value: Long) {
    return edit { putLong(key, value) }
}

fun SharedPreferences.getDateOrNull(key: String): Date? {
    val value = getLongOrNull(key)
    value?.let { return Date(value) }
    return null
}

fun SharedPreferences.saveDate(key: String, value: Date?) {
    value?.let {
        return saveLong(key, it.time)
    }
}

fun SharedPreferences.clearAll() {
    return edit { clear() }
}
