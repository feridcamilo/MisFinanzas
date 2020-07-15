package com.android.misfinanzas.base

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    protected lateinit var progressListener: ProgressListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setListeners(context)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        setListeners(activity)
    }

    private fun setListeners(context: Context) {
        setProgressListener(context)
    }

    private fun setProgressListener(context: Context) {
        if (context is ProgressListener) {
            progressListener = context
        } else {
            throw ClassCastException(context.toString() + " must implement ProgressListener.")
        }
    }
}