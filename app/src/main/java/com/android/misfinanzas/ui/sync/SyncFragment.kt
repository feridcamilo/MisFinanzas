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

    val TAG = this.javaClass.name

    private val viewModel by viewModels<SyncViewModel> {
        BaseViewModelFactory(
            WebRepositoryImp(RetrofitDataSource()),
            LocalRepositoryImp(RoomDataSource(requireContext()))
        )
    }

    private lateinit var cardViewLogin: LoginView
    private var fromBalance: Boolean = false
    private var fromMovements: Boolean = false

    private lateinit var getWebUserObserver: Observer<Result<User>>
    private lateinit var getWebBalanceObserver: Observer<Result<Balance>>
    private lateinit var getWebMovementsObserver: Observer<Result<List<Movement>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fromBalance = it.getBoolean(FROM_BALANCE)
            fromMovements = it.getBoolean(FROM_MOVEMENTS)
        }
        setupObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sync, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (UserSesion.getUser() == null) {
            setupLogin()
        } else {
            setupSync()
        }
    }

    private fun setupObservers() {
        getWebUserObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    if (result.data == null) {
                        Toast.makeText(requireContext(), getString(R.string.info_wrong_user_or_password), Toast.LENGTH_SHORT).show()
                    } else {
                        insertUser(result.data)
                        UserSesion.setUser(result.data)
                        viewModel.setClientId(result.data.IdCliente.toString())
                        cardViewLogin.visibility = View.GONE
                        setupSync()
                        progressListener.hide()
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

        getWebBalanceObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    insertBalance(result.data)
                    getWebMovements()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_balance, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_retrofit, result.exception))
                }
            }
        }

        getWebMovementsObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    insertMovements(result.data)
                    progressListener.hide()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_movements, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_retrofit, result.exception))
                }
            }
        }
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
                getWebUser(user, password)
            }
        }
    }

    private fun getWebUser(user: String, password: String) {
        viewModel.setCredential(UserCredential(user, password))

        if (!viewModel.getWebUser.hasObservers()) {
            viewModel.getWebUser.observe(viewLifecycleOwner, getWebUserObserver)
        }
    }

    private fun insertUser(user: User) {
        viewModel.insertLocalUser(user)
        Toast.makeText(requireContext(), R.string.info_user_saved, Toast.LENGTH_SHORT).show()
    }

    private fun setupSync() {
        btn_sync_data.visibility = View.VISIBLE
        btn_sync_data.setOnClickListener {
            initSync()
        }
    }

    private fun initSync() {
        getWebBalance()
    }

    private fun getWebBalance() {
        if (viewModel.getWebBalance.hasObservers()) {
            viewModel.getWebBalance.removeObservers(viewLifecycleOwner)
        }
        viewModel.getWebBalance.observe(viewLifecycleOwner, getWebBalanceObserver)
    }

    private fun insertBalance(balance: Balance) {
        viewModel.insertLocalBalance(balance)
        Toast.makeText(requireContext(), R.string.info_balance_saved, Toast.LENGTH_SHORT).show()
    }

    private fun getWebMovements() {
        if (viewModel.getWebMovements.hasObservers()) {
            viewModel.getWebMovements.removeObservers(viewLifecycleOwner)
        }
        viewModel.getWebMovements.observe(viewLifecycleOwner, getWebMovementsObserver)
    }

    private fun insertMovements(movements: List<Movement>) {
        viewModel.insertLocalMovement(movements)
        Toast.makeText(requireContext(), R.string.info_movements_saved, Toast.LENGTH_SHORT).show()
    }
}
