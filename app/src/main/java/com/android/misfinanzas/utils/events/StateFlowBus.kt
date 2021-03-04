package com.android.misfinanzas.utils.events

import android.util.SparseArray
import androidx.annotation.NonNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object StateFlowBus {

    private val subjectMap = SparseArray<StateFlow<Any>>()

    fun getStateFlow(@EventSubject code: Int): StateFlow<Any> {
        var stateFlow = subjectMap.get(code)
        if (stateFlow == null) {
            stateFlow = MutableStateFlow(false) //init with any value
            subjectMap.put(code, stateFlow)
        }
        return stateFlow
    }

    fun publish(@EventSubject code: Int, @NonNull value: Any) {
        (getStateFlow(code) as MutableStateFlow).value = value
    }

}
