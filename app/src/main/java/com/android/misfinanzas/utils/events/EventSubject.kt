package com.android.misfinanzas.utils.events

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    EventSubject.SYNC,
    EventSubject.LOADER
)
internal annotation class EventSubject {

    companion object {
        const val SYNC = 0
        const val LOADER = 1
    }

}
