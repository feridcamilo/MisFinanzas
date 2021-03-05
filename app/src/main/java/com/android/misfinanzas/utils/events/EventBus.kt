package com.android.misfinanzas.utils.events

import android.util.Log

object EventBus {

    private const val UID_PATTERN = "%s|%s"
    private const val UID_END_PATTERN = "|%s"

    private val subjectMap = HashMap<String, EventLiveData>()

    /**
     * Get the live data or create it if it's not already in memory.
     */
    fun from(key: String, subjectCode: EventSubject): EventLiveData {
        val uid = String.format(UID_PATTERN, key, subjectCode.name)
        var liveData = subjectMap[uid]
        if (liveData == null) {
            liveData = EventLiveData(uid)
            subjectMap[uid] = liveData
        }
        Log.e("MAP", subjectMap.toString())
        return liveData
    }

    /**
     * Removes this subject when it has no observers.
     */
    internal fun unregister(uid: String) {
        subjectMap.remove(uid)
        Log.e("UNREGISTER", uid)
        Log.e("MAP", subjectMap.toString())
    }

    /**
     * Publish an object to the specified subject for all subscribers of that subject.
     */
    fun publish(subject: EventSubject, value: Any) {
        val pattern = String.format(UID_END_PATTERN, subject.name)
        val entries = subjectMap.entries.filter { it.key.endsWith(pattern) }
        entries.forEach { entry ->
            entry.value.update(value)
        }
    }

}
