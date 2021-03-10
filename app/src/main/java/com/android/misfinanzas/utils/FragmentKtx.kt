package com.android.misfinanzas.utils

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.android.misfinanzas.utils.events.EventBus
import com.android.misfinanzas.utils.events.EventSubject

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showLoader() {
    hideKeyboard()
    EventBus.publish(EventSubject.LOADER, true)
}

fun Fragment.hideLoader() {
    EventBus.publish(EventSubject.LOADER, false)
}

fun Fragment.setActionBar(toolbar: Toolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
}

fun Fragment.setToolbarTitle(@StringRes title: Int) {
    setToolbarTitle(getString(title))
}

fun Fragment.setToolbarTitle(title: String) {
    (activity as AppCompatActivity).supportActionBar?.title = title
}