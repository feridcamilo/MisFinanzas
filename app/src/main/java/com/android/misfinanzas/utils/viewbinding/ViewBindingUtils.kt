package com.android.misfinanzas.utils.viewbinding

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.collection.ArrayMap
import androidx.viewbinding.ViewBinding
import com.android.misfinanzas.BuildConfig
import java.lang.reflect.Method

internal object MainHandler {
    private val handler = Handler(Looper.getMainLooper())

    internal fun post(action: () -> Unit): Boolean = handler.post(action)
}

@PublishedApi
internal fun ensureMainThread() = check(Looper.getMainLooper() == Looper.myLooper()) {
    "Expected to be called on the main thread but was " + Thread.currentThread().name
}

internal inline fun log(crossinline message: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.d("ViewBinding", message())
    }
}

internal object GetBindMethod {
    init {
        ensureMainThread()
    }

    private val methodSignature = View::class.java
    private val methodMap = ArrayMap<Class<out ViewBinding>, Method>()

    internal operator fun <T : ViewBinding> invoke(clazz: Class<T>): Method =
        methodMap
            .getOrPut(clazz) { clazz.getMethod("bind", methodSignature) }
            .also { log { "methodMap.size: ${methodMap.size}" } }
}
