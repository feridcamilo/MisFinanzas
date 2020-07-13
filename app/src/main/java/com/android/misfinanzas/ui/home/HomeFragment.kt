package com.android.misfinanzas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.data.local.RoomDataSource
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.repository.LocalRepositoryImp
import com.android.data.remote.repository.WebRepositoryImp
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> {
        BaseViewModelFactory(
            WebRepositoryImp(RetrofitDataSource()),
            LocalRepositoryImp(RoomDataSource(requireContext()))
        )
    }
    private lateinit var cardViewLogin: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardViewLogin = loggin_card_view
    }
}
