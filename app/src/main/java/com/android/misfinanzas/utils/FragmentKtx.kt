package com.android.misfinanzas.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.android.misfinanzas.utils.events.EventSubject
import com.android.misfinanzas.utils.events.StateFlowBus

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showLoader() {
    hideKeyboard()
    StateFlowBus.publish(EventSubject.LOADER, true)
}

fun Fragment.hideLoader() {
    StateFlowBus.publish(EventSubject.LOADER, false)
}

fun Fragment.setActionBar(toolbar: Toolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
}