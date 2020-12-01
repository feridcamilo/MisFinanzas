package com.android.misfinanzas.base

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.android.domain.utils.StringUtils.Companion.EMPTY
import com.android.misfinanzas.R
import com.android.misfinanzas.utils.showLongToast

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
            throw ClassCastException("$context must implement ProgressListener.")
        }
    }

    protected fun showExceptionMessage(tag: String, message: String, type: ErrorType = ErrorType.TYPE_APP) {
        progressListener.hide()
        context?.showLongToast(message)
        val logMsg = when (type) {
            ErrorType.TYPE_APP -> EMPTY
            ErrorType.TYPE_ROOM -> getString(R.string.error_room, message)
            ErrorType.TYPE_RETROFIT -> getString(R.string.error_retrofit, message)
        }
        Log.e(tag, logMsg)
    }

    enum class ErrorType { TYPE_APP, TYPE_ROOM, TYPE_RETROFIT }
}
