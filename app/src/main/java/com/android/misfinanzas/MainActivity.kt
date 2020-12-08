package com.android.misfinanzas

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.android.misfinanzas.base.ProgressListener
import com.android.misfinanzas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProgressListener {

    lateinit var navController: NavController
    private lateinit var loading: RelativeLayout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = binding.progressBar
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun show(hideKeyBoard: Boolean) {
        loading.visibility = View.VISIBLE
        if (hideKeyBoard) {
            hideKeyboard()
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun show() {
        show(true)
    }

    override fun hide() {
        loading.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}
