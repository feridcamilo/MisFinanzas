package com.android.misfinanzas.ui.sync

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
import com.android.data.utils.SharedPreferencesUtils
import com.android.data.utils.StringUtils.Companion.EMPTY
import com.android.data.utils.StringUtils.Companion.POINT
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.BaseViewModelFactory
import com.android.misfinanzas.ui.widgets.login.LoginView
import kotlinx.android.synthetic.main.card_view_login.view.*
import kotlinx.android.synthetic.main.fragment_sync.*
import kotlinx.coroutines.launch

class SyncFragment : BaseFragment() {

    companion object {
        const val FROM_BALANCE: String = "FromBalance"
        const val AUTO_SYNC: String = "AutoSync"
        const val FROM_MOVEMENTS: String = "FromMovements"
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
    private var isRefresh: Boolean = false
    private var fromMovements: Boolean = false

    private lateinit var serverDateTimeObserver: Observer<Result<String>>
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

        sw_auto_sync.isChecked = SharedPreferencesUtils.getAutoSyncConfig(requireContext())
        sw_auto_sync.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesUtils.setAutoSyncConfig(requireContext(), isChecked)
            Toast.makeText(requireContext(), R.string.info_config_saved, Toast.LENGTH_SHORT).show()
        }

        ib_info.setOnClickListener { Toast.makeText(requireContext(), R.string.info_difference_time, Toast.LENGTH_LONG).show() }

        tv_link.setOnClickListener { AppUtils.openURL(requireContext(), AppConfig.BASE_URL) }

        if (NetworkUtils.isConnected(requireContext(), getString(R.string.error_not_network_no_continue))) {
            setupServerDateTimeObserver()
        }
    }

    private fun setupServerDateTimeObserver() {
        serverDateTimeObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    if (result.data == null) {
                        tv_serverDateTime_title.visibility = View.GONE
                        tv_serverDateTime.visibility = View.GONE
                        ib_info.visibility = View.GONE
                        progressListener.hide()
                    } else {
                        tv_serverDateTime_title.visibility = View.VISIBLE
                        tv_serverDateTime.visibility = View.VISIBLE
                        ib_info.visibility = View.VISIBLE

                        val serverDateTime = DateUtils.getDateTimeFormat_AM_PM().parse(result.data.replace(POINT, EMPTY))!!
                        UserSesion.setServerDateTime(serverDateTime)

                        val gtmDiff = UserSesion.getServerTimeZone()!!.displayName
                        //Save gtm diff in shared preferences
                        SharedPreferencesUtils.setDiffTimeToServer(requireContext(), gtmDiff)

                        val serverDateTimeFormated = DateUtils.getDateTimeFormat_AM_PM().format(serverDateTime)
                        tv_serverDateTime.text = getString(R.string.server_datetime_value, serverDateTimeFormated, gtmDiff)

                        if (!isRefresh) {
                            continueSyncProcess()
                        }

                        if (!autoSync) {
                            progressListener.hide()
                        }
                    }
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_server_datetime, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_retrofit, result.exception))
                }
            }
        }

        getServerDateTime(false)
    }

    private fun continueSyncProcess() {
        if (!UserSesion.hasUser()) {
            setupLogin()
        } else {
            showLastSyncValues()
            if (autoSync) {
                makeAutoSync()
            } else {
                setupSync()
            }
        }
    }

    private fun showLastSyncValues() {
        lifecycleScope.launch {
            val lastSyncMovements = viewModel.getLastSyncMovements()
            val lastSyncMasters = viewModel.getLastSyncMasters()

            if (lastSyncMovements != null || lastSyncMasters != null) {
                tv_last_sync.visibility = View.VISIBLE
                tv_last_sync_movements.visibility = View.VISIBLE
                tv_last_sync_masters.visibility = View.VISIBLE

                if (lastSyncMovements != null) {
                    val formatedLastSyncMovements = DateUtils.getDateTimeFormat_AM_PM().format(DateUtils.getDateTimeToWebService(lastSyncMovements))
                    tv_last_sync_movements.text = getString(R.string.last_sync_movements, formatedLastSyncMovements)
                } else {
                    tv_last_sync_movements.text = getString(R.string.never_synced)
                }
                if (lastSyncMasters != null) {
                    val formatedLastSyncMasters = DateUtils.getDateTimeFormat_AM_PM().format(DateUtils.getDateTimeToWebService(lastSyncMasters))
                    tv_last_sync_masters.text = getString(R.string.last_sync_masters, formatedLastSyncMasters)
                } else {
                    tv_last_sync_masters.text = getString(R.string.never_synced)
                }
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
                        Toast.makeText(requireContext(), R.string.info_wrong_user_or_password, Toast.LENGTH_SHORT).show()
                        progressListener.hide()
                    } else {
                        Toast.makeText(requireContext(), R.string.info_user_saved, Toast.LENGTH_SHORT).show()
                        makeLogin(result.data)
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
                    viewModel.updateLastSyncMovements(DateUtils.getCurrentDateTime())
                    getServerDateTime(true)
                    showLastSyncValues()
                    if (autoSync) {
                        syncMasters()
                    } else {
                        progressListener.hide()
                        Toast.makeText(requireContext(), R.string.info_movements_synced, Toast.LENGTH_SHORT).show()
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
                    getServerDateTime(true)
                    showLastSyncValues()
                    if (autoSync) {
                        UserSesion.setFirstOpen(false)
                        navigateToBalance()
                    } else {
                        progressListener.hide()
                        Toast.makeText(requireContext(), R.string.info_masters_synced, Toast.LENGTH_SHORT).show()
                    }
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
        Toast.makeText(requireContext(), R.string.info_data_synced, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_syncFragment_to_balanceFragment)
    }

    private fun setupLogin() {
        setupUserObserver()
        sw_auto_sync.isChecked = true
        cardViewLogin = login_view
        cardViewLogin.visibility = View.VISIBLE

        cardViewLogin.btn_login.setOnClickListener {
            if (NetworkUtils.isConnected(requireContext(), getString(R.string.error_not_network_no_login))) {
                val credential = cardViewLogin.getCredential()
                if (credential != null) {
                    viewModel.setCredential(credential)
                }
            }
        }
        Toast.makeText(requireContext(), R.string.info_please_log_in, Toast.LENGTH_SHORT).show()
    }

    private fun setupSync() {
        btn_sync_movements.visibility = View.VISIBLE
        btn_sync_masters.visibility = View.VISIBLE
        btn_clean_discarded.visibility = View.VISIBLE

        setupMovementsObserver()
        btn_sync_movements.setOnClickListener {
            if (NetworkUtils.isConnected(requireContext(), getString(R.string.error_not_network_no_sync))) {
                //Manual movements sync
                syncMovements()
            }
        }

        setupMastersObserver()
        btn_sync_masters.setOnClickListener {
            if (NetworkUtils.isConnected(requireContext(), getString(R.string.error_not_network_no_sync))) {
                //Manual masters sync
                syncMasters()
            }
        }

        btn_clean_discarded.setOnClickListener {
            cleanDiscarded()
            Toast.makeText(requireContext(), R.string.info_movements_discarded_restored, Toast.LENGTH_LONG).show()
        }
    }

    private fun getServerDateTime(isRefresh: Boolean) {
        this.isRefresh = isRefresh
        viewModel.getServerDateTime().observe(viewLifecycleOwner, serverDateTimeObserver)
    }

    private fun syncMovements() {
        viewModel.syncMovements().observe(viewLifecycleOwner, syncMovementsObserver)
    }

    private fun syncMasters() {
        viewModel.syncMasters().observe(viewLifecycleOwner, syncMastersObserver)
    }

    private fun cleanDiscarded() {
        viewModel.cleanDiscarded()
    }
}
