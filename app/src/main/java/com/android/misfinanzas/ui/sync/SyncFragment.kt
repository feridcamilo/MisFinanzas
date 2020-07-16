package com.android.misfinanzas.ui.sync

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android.data.local.RoomDataSource
import com.android.data.local.repository.LocalRepositoryImp
import com.android.data.local.repository.UserSesion
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.model.Balance
import com.android.data.remote.model.Movement
import com.android.data.remote.model.User
import com.android.data.remote.repository.WebRepositoryImp
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.BaseViewModelFactory
import com.android.misfinanzas.ui.widgets.login.LoginView
import kotlinx.android.synthetic.main.fragment_sync.*
import kotlinx.android.synthetic.main.login_card_view.view.*

class SyncFragment : BaseFragment() {

    companion object {
        val FROM_BALANCE: String = "FromBalance"
        val FROM_MOVEMENTS: String = "FromMovements"
    }

    private val TAG = this.javaClass.name

    private val viewModel by viewModels<SyncViewModel> {
        BaseViewModelFactory(
            WebRepositoryImp(RetrofitDataSource()),
            LocalRepositoryImp(RoomDataSource(requireContext()))
        )
    }

    private lateinit var cardViewLogin: LoginView
    private var fromBalance: Boolean = false
    private var fromMovements: Boolean = false

    private lateinit var syncUserObserver: Observer<Result<User>>
    private lateinit var syncBalanceObserver: Observer<Result<Balance>>
    private lateinit var syncMovementsObserver: Observer<Result<List<Movement>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fromBalance = it.getBoolean(FROM_BALANCE)
            fromMovements = it.getBoolean(FROM_MOVEMENTS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sync, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        if (!UserSesion.hasUser()) {
            setupLogin()
        } else {
            setupSync()
        }
    }

    private fun setupObservers() {
        syncUserObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    if (result.data == null) {
                        Toast.makeText(requireContext(), getString(R.string.info_wrong_user_or_password), Toast.LENGTH_SHORT).show()
                    } else {
                        makeLogin(result.data)
                        Toast.makeText(requireContext(), R.string.info_user_saved, Toast.LENGTH_SHORT).show()
                    }
                    progressListener.hide()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_user, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_retrofit, result.exception))
                }
            }
        }

        viewModel.syncUser.observe(viewLifecycleOwner, syncUserObserver)

        syncBalanceObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    syncMovements()
                    Toast.makeText(requireContext(), R.string.info_balance_saved, Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_balance, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_retrofit, result.exception))
                }
            }
        }

        syncMovementsObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), R.string.info_movements_saved, Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_movements, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_retrofit, result.exception))
                }
            }
        }
    }

    private fun makeLogin(user: User) {
        UserSesion.setUser(user)
        viewModel.setClientId(user.IdCliente.toString())

        cardViewLogin.visibility = View.GONE
        setupSync()
    }

    private fun setupLogin() {
        cardViewLogin = login_view
        cardViewLogin.visibility = View.VISIBLE

        cardViewLogin.btn_login.setOnClickListener {
            val user = cardViewLogin.et_user.text.trim().toString()
            val password = cardViewLogin.et_password.text.trim().toString()
            if (user.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.info_enter_user_and_password), Toast.LENGTH_SHORT).show()
            } else {
                syncUser(user, password)
            }
        }
        Toast.makeText(requireContext(), R.string.info_please_log_in, Toast.LENGTH_SHORT).show()
    }

    private fun syncUser(user: String, password: String) {
        viewModel.setCredential(UserCredential(user, password))
    }

    private fun setupSync() {
        btn_sync_data.visibility = View.VISIBLE
        btn_sync_data.setOnClickListener {
            initSync()
        }
    }

    private fun initSync() {
        syncBalance()
    }

    private fun syncBalance() {
        //viewModel.getWebBalance.observe(viewLifecycleOwner, syncBalanceObserver)
        viewModel.syncBalance().observe(viewLifecycleOwner, syncBalanceObserver)
    }

    private fun syncMovements() {
        viewModel.syncMovements().observe(viewLifecycleOwner, syncMovementsObserver)
    }
}
