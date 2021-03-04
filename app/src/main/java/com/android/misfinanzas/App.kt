package com.android.misfinanzas

import androidx.multidex.MultiDexApplication
import com.android.misfinanzas.di.injectModules

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        injectModules(this)
    }
}
