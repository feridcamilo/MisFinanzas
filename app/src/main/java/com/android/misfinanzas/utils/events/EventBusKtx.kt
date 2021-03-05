package com.android.misfinanzas.utils.events

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun <T : AppCompatActivity> T.getEventBus(subjectCode: EventSubject): EventLiveData {
    return EventBus.from(this.hashCode().toString(), subjectCode)
}

fun <T : Fragment> T.getEventBus(subjectCode: EventSubject): EventLiveData {
    return EventBus.from(this.hashCode().toString(), subjectCode)
}
