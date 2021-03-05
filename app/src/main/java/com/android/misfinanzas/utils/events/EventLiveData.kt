package com.android.misfinanzas.utils.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class EventLiveData(
    private val uid: String
) : LiveData<Any>() {

    fun update(any: Any) {
        postValue(any)
    }

    override fun removeObserver(observer: Observer<in Any>) {
        super.removeObserver(observer)
        if (!hasObservers()) {
            EventBus.unregister(uid)
        }
    }

}
