package com.android.misfinanzas.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.ActivityMainBinding
import com.android.misfinanzas.utils.events.EventSubject
import com.android.misfinanzas.utils.events.getEventBus
import com.android.misfinanzas.utils.invisible
import com.android.misfinanzas.utils.viewbinding.viewBinding
import com.android.misfinanzas.utils.visible

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding<ActivityMainBinding>()
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1000)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setupLoaderObserver()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setupLoaderObserver() {
        getEventBus(EventSubject.LOADER).observe(this, loaderStateObserver)
    }

    private val loaderStateObserver = Observer<Any> { state ->
        if (state == true) {
            binding.progressBar.visible()
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            binding.progressBar.invisible()
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

}
