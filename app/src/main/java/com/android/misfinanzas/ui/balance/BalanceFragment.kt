package com.android.misfinanzas.ui.balance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.data.local.RoomDataSource
import com.android.data.local.model.BalanceVO
import com.android.data.local.repository.LocalRepositoryImp
import com.android.data.local.repository.UserSesion
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.repository.WebRepositoryImp
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseViewModelFactory
import com.android.misfinanzas.ui.sync.SyncFragment
import kotlinx.android.synthetic.main.fragment_balance.*

class BalanceFragment : Fragment() {

    val TAG = this.javaClass.name

    private val viewModel by viewModels<BalanceViewModel> {
        BaseViewModelFactory(
            WebRepositoryImp(RetrofitDataSource()),
            LocalRepositoryImp(RoomDataSource(requireContext()))
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_balance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocalUser()
    }

    private fun getLocalUser() {
        viewModel.getLocalUser.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    if (result.data == null) {
                        Toast.makeText(requireContext(), R.string.info_please_log_in, Toast.LENGTH_LONG).show()
                        navigateToSync()
                    } else {
                        UserSesion.setUser(result.data)
                        getLocalBalance()
                    }
                    progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), getString(R.string.error_getting_movements, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        })
    }

    private fun navigateToSync() {
        val bundle = Bundle()
        bundle.putBoolean(SyncFragment.FROM_BALANCE, true)
        findNavController().navigate(R.id.action_balanceFragment_to_syncFragment, bundle)
    }

    private fun getLocalBalance() {
        viewModel.getLocalBalance.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    if (result.data == null) {
                        Toast.makeText(requireContext(), R.string.info_no_balance, Toast.LENGTH_SHORT).show()
                        navigateToSync()
                    } else {
                        showBalance(result.data)
                    }
                    progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), getString(R.string.error_getting_balance, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        })
    }

    private fun showBalance(balance: BalanceVO) {
        val balanceView = balance_view
        balanceView.showBalance(balance)
    }
}
