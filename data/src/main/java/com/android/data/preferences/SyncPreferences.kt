package com.android.data.preferences

import android.content.SharedPreferences
import com.android.data.utils.*
import java.util.*

class SyncPreferences(private val preferences: SharedPreferences) {

    companion object {
        const val PREFERENCE_NAME = "SYNC_PREFERENCES"

        private const val LAST_SYNC_MOVEMENTS = "LAST_SYNC_MOVEMENTS"
        private const val LAST_SYNC_MASTERS = "LAST_SYNC_MASTERS"
        private const val DIFF_TIME_WITH_SERVER = "DIFF_TIME_WITH_SERVER"

    }

    var lastSyncMovements: Date?
        get() = preferences.getDateOrNull(LAST_SYNC_MOVEMENTS)
        set(value) = preferences.saveDate(LAST_SYNC_MOVEMENTS, value)

    var lastSyncMasters: Date?
        get() = preferences.getDateOrNull(LAST_SYNC_MASTERS)
        set(value) = preferences.saveDate(LAST_SYNC_MASTERS, value)

    var diffTimeWithServer: String?
        get() = preferences.getStringOrNull(DIFF_TIME_WITH_SERVER)
        set(value) = preferences.saveString(DIFF_TIME_WITH_SERVER, value)

    fun clear() {
        preferences.clearAll()
    }

}
