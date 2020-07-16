package com.android.misfinanzas.ui.movementDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.data.local.model.MovementVO
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment

class MovementDetailFragment : BaseFragment() {

    companion object {
        val MOVEMENT_DATA: String = "Movement"
    }

    private val TAG = this.javaClass.name
    private lateinit var movement: MovementVO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movement = it.getParcelable(MOVEMENT_DATA)!!
            Log.d(TAG, "$movement")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movement_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
