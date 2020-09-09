package com.android.misfinanzas.ui.sync

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.data.AppConfig
import com.android.data.UserSesion
import com.android.data.local.RoomDataSource
import com.android.data.local.repository.LocalRepositoryImp
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.model.User
import com.android.data.remote.repository.WebRepositoryImp
import com.android.data.utils.AppUtils
import com.android.data.utils.DateUtils
import com.android.data.utils.NetworkUtils
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.BaseViewModelFactory
import com.android.misfinanzas.ui.widgets.login.LoginView
import kotlinx.android.synthetic.main.card_view_login.view.*
import kotlinx.android.synthetic.main.fragment_sync.*

class SyncFragment : BaseFragment() {

    companion object {
        val FROM_BALANCE: String = "FromBalance"
        val AUTO_SYNC: String = "AutoSync"
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
    private var autoSync: Boolean = false
    private var fromMovements: Boolean = false

    private lateinit var syncUserObserver: Observer<Result<User>>
    private lateinit var syncMovementsObserver: Observer<Result<Boolean>>
    private lateinit var syncMastersObserver: Observer<Result<Boolean>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fromBalance = it.getBoolean(FROM_BALANCE)
            autoSync = it.getBoolean(AUTO_SYNC)
            fromMovements = it.getBoolean(FROM_MOVEMENTS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sync, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!UserSesion.hasUser()) {
            setupLogin()
        } else {
            tv_link.setOnClickListener { AppUtils.openURL(requireContext(), AppConfig.BASE_URL) }
            if (autoSync) {
                makeAutoSync()
            } else {
                setupSync()
            }
        }
    }

    private fun setupUserObserver() {
        syncUserObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    if (result.data == null) {
                        Toast.makeText(requireContext(), getString(R.string.info_wrong_user_or_password), Toast.LENGTH_SHORT).show()
                        progressListener.hide()
                    } else {
                        makeLogin(result.data)
                        Toast.makeText(requireContext(), R.string.info_user_saved, Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_user, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_retrofit, result.exception))
                }
            }
        }

        viewModel.syncUser.observe(viewLifecycleOwner, syncUserObserver)
    }

    private fun setupMovementsObserver() {
        syncMovementsObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    progressListener.hide()
                    viewModel.updateLastSyncMovements(DateUtils.getCurrentDateTime())
                    Toast.makeText(requireContext(), R.string.info_movements_synced, Toast.LENGTH_SHORT).show()
                    if (autoSync) {
                        syncMasters()
                    }
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_movements, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_retrofit, result.exception))
                }
            }
        }
    }

    private fun setupMastersObserver() {
        syncMastersObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    viewModel.updateLastSyncMasters(DateUtils.getCurrentDateTime())
                    Toast.makeText(requireContext(), R.string.info_masters_synced, Toast.LENGTH_SHORT).show()
                    if (autoSync) {
                        UserSesion.setFirstOpen(false)
                        navigateToBalance()
                    }
                    progressListener.hide()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_masters, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_retrofit, result.exception))
                }
            }
        }
    }

    private fun makeLogin(user: User) {
        UserSesion.setUser(user)
        viewModel.setClientId(user.IdCliente.toString())
        cardViewLogin.visibility = View.GONE

        //Auto sync after login
        autoSync = true
        makeAutoSync()
    }

    private fun makeAutoSync() {
        setupSync()
        syncMovements()
    }

    private fun navigateToBalance() {
        val bundle = Bundle()
        findNavController().navigate(R.id.action_syncFragment_to_balanceFragment, bundle)
    }

    private fun setupLogin() {
        setupUserObserver()

        cardViewLogin = login_view
        cardViewLogin.visibility = View.VISIBLE

        cardViewLogin.btn_login.setOnClickListener {
            if (NetworkUtils.isConnected(requireContext())) {
                val credential = cardViewLogin.getCredential()
                if (credential != null) {
                    viewModel.setCredential(credential)
                }
            } else {
                Toast.makeText(requireContext(), R.string.error_not_network_no_login, Toast.LENGTH_SHORT).show()
            }
        }
        Toast.makeText(requireContext(), R.string.info_please_log_in, Toast.LENGTH_SHORT).show()
    }

    private fun setupSync() {
        btn_sync_movements.visibility = View.VISIBLE
        btn_sync_masters.visibility = View.VISIBLE

        setupMovementsObserver()
        btn_sync_movements.setOnClickListener {
            if (NetworkUtils.isConnected(requireContext())) {
                //Manual movements sync
                syncMovements()
            } else {
                Toast.makeText(requireContext(), R.string.error_not_network_no_sync, Toast.LENGTH_SHORT).show()
            }
        }

        setupMastersObserver()
        btn_sync_masters.setOnClickListener {
            if (NetworkUtils.isConnected(requireContext())) {
                //Manual masters sync
                syncMasters()
            } else {
                Toast.makeText(requireContext(), R.string.error_not_network_no_sync, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun syncMovements() {
        viewModel.syncMovements().observe(viewLifecycleOwner, syncMovementsObserver)
    }

    private fun syncMasters() {
        viewModel.syncMasters().observe(viewLifecycleOwner, syncMastersObserver)
    }
}
