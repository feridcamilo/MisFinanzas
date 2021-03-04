package com.android.data.preferences

import android.content.SharedPreferences
import com.android.data.utils.getBoolean
import com.android.data.utils.saveBoolean

class ConfigPreferences(private val preferences: SharedPreferences) {

    companion object {
        const val PREFERENCE_NAME = "CONFIG_PREFERENCES"

        private const val AUTO_SYNC_ON_OPEN = "AUTO_SYNC_ON_OPEN"
        private const val AUTO_SYNC_ON_EDIT = "AUTO_SYNC_ON_EDIT"

    }

    var isAutoSyncOnOpen: Boolean
        get() = preferences.getBoolean(AUTO_SYNC_ON_OPEN)
        set(value) = preferences.saveBoolean(AUTO_SYNC_ON_OPEN, value)

    var isAutoSyncOnEdit: Boolean
        get() = preferences.getBoolean(AUTO_SYNC_ON_EDIT)
        set(value) = preferences.saveBoolean(AUTO_SYNC_ON_EDIT, value)


}
