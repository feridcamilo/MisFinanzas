package com.android.misfinanzas.ui.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.data.utils.SharedPreferencesUtils
import com.android.domain.AppConfig
import com.android.domain.UserSesion
import com.android.domain.result.Result
import com.android.domain.utils.DateUtils
import com.android.domain.utils.StringUtils.Companion.EMPTY
import com.android.domain.utils.StringUtils.Companion.POINT
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.LoginListener
import com.android.misfinanzas.databinding.FragmentSyncBinding
import com.android.misfinanzas.ui.login.LoginFragment
import com.android.misfinanzas.utils.isConnected
import com.android.misfinanzas.utils.openURL
import com.android.misfinanzas.utils.showLongToast
import com.android.misfinanzas.utils.showShortToast
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

open class SyncFragment : BaseFragment(), LoginListener {

    companion object {
        const val FROM_BALANCE: String = "FromBalance"
        const val FROM_MOVEMENTS: String = "FromMovements"
        const val AUTO_SYNC: String = "AutoSync"
    }

    private val TAG = this.javaClass.name

    private val viewModel by viewModel<SyncViewModel>()
    private lateinit var binding: FragmentSyncBinding

    private var fromBalance: Boolean = false
    private var autoSync: Boolean = false
    private var isRefresh: Boolean = false
    private var fromMovements: Boolean = false

    private lateinit var serverDateTimeObserver: Observer<Result<String>>
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSyncBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()
        if (context?.isConnected(getString(R.string.error_not_network_no_continue)) == true) {
            setupServerDateTimeObserver()
        }
    }

    private fun setupEvents() = with(binding) {
        swAutoSync.isChecked = SharedPreferencesUtils.getAutoSyncConfig(requireContext())
        swAutoSync.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesUtils.setAutoSyncConfig(requireContext(), isChecked)
            context?.showShortToast(R.string.info_config_saved)
        }

        ibInfo.setOnClickListener { context?.showLongToast(R.string.info_difference_time) }

        tvLink.setOnClickListener { context?.openURL(AppConfig.BASE_URL) }

        btnSyncMovements.setOnClickListener {
            if (context?.isConnected(getString(R.string.error_not_network_no_sync)) == true) {
                //Manual movements sync
                syncMovements()
            }
        }

        btnSyncMasters.setOnClickListener {
            if (context?.isConnected(getString(R.string.error_not_network_no_sync)) == true) {
                //Manual masters sync
                syncMasters()
            }
        }

        btnCleanDiscarded.setOnClickListener {
            cleanDiscarded()
            context?.showLongToast(R.string.info_movements_discarded_restored)
        }
    }

    private fun setupServerDateTimeObserver() = with(binding) {
        serverDateTimeObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    if (result.data == null) {
                        tvServerDateTimeTitle.visibility = View.GONE
                        tvServerDateTime.visibility = View.GONE
                        ibInfo.visibility = View.GONE
                        progressListener.hide()
                    } else {
                        tvServerDateTimeTitle.visibility = View.VISIBLE
                        tvServerDateTime.visibility = View.VISIBLE
                        ibInfo.visibility = View.VISIBLE

                        val serverDateTime = DateUtils.getDateTimeFormat_AM_PM().parse(result.data.replace(POINT, EMPTY))!!
                        UserSesion.setServerDateTime(serverDateTime)

                        val gtmDiff = UserSesion.getServerTimeZone()!!.displayName
                        //Save gtm diff in shared preferences
                        SharedPreferencesUtils.setDiffTimeToServer(requireContext(), gtmDiff)

                        val serverDateTimeFormated = DateUtils.getDateTimeFormat_AM_PM().format(serverDateTime)
                        tvServerDateTime.text = getString(R.string.server_datetime_value, serverDateTimeFormated, gtmDiff)

                        if (!isRefresh) {
                            continueSyncProcess()
                        }

                        if (!autoSync) {
                            progressListener.hide()
                        }
                    }
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_srv_dtm, result.exception), ErrorType.TYPE_RETROFIT)
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

    private fun setupLogin() {
        binding.swAutoSync.isChecked = true
        binding.loginView.visibility = View.VISIBLE
        childFragmentManager.fragments.forEach {
            if (it is LoginFragment) {
                it.setListener(this)
            }
        }
        context?.showShortToast(R.string.info_please_log_in)
    }

    override fun onLogged() {
        binding.loginView.visibility = View.GONE
        viewModel.setClientId(UserSesion.getUser()!!.clientId.toString())
        //Auto sync after login
        autoSync = true
        makeAutoSync()
    }

    private fun setupSync() = with(binding) {
        btnSyncMovements.visibility = View.VISIBLE
        btnSyncMasters.visibility = View.VISIBLE
        btnCleanDiscarded.visibility = View.VISIBLE

        setupMovementsObserver()
        setupMastersObserver()
    }

    private fun showLastSyncValues() = with(binding) {
        lifecycleScope.launch {
            val lastSyncMovements = viewModel.getLastSyncMovements()
            val lastSyncMasters = viewModel.getLastSyncMasters()

            if (lastSyncMovements != null || lastSyncMasters != null) {
                tvLastSync.visibility = View.VISIBLE
                tvLastSyncMovements.visibility = View.VISIBLE
                tvLastSyncMasters.visibility = View.VISIBLE

                if (lastSyncMovements != null) {
                    val formattedLastSyncMovements = DateUtils.getDateTimeFormat_AM_PM().format(DateUtils.getDateTimeToWebService(lastSyncMovements))
                    tvLastSyncMovements.text = getString(R.string.last_sync_movements, formattedLastSyncMovements)
                } else {
                    tvLastSyncMovements.text = getString(R.string.never_synced)
                }
                if (lastSyncMasters != null) {
                    val formattedLastSyncMasters = DateUtils.getDateTimeFormat_AM_PM().format(DateUtils.getDateTimeToWebService(lastSyncMasters))
                    tvLastSyncMasters.text = getString(R.string.last_sync_masters, formattedLastSyncMasters)
                } else {
                    tvLastSyncMasters.text = getString(R.string.never_synced)
                }
            }
        }
    }

    private fun setupMovementsObserver() {
        syncMovementsObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    viewModel.updateLastSyncMovements(DateUtils.getCurrentDateTime())
                    getServerDateTime(true)
                    showLastSyncValues()
                    if (autoSync) {
                        syncMasters()
                    } else {
                        progressListener.hide()
                        context?.showShortToast(R.string.info_movements_synced)
                    }
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_movements, result.exception), ErrorType.TYPE_RETROFIT)
            }
        }
    }

    private fun setupMastersObserver() {
        syncMastersObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    viewModel.updateLastSyncMasters(DateUtils.getCurrentDateTime())
                    getServerDateTime(true)
                    showLastSyncValues()
                    if (autoSync) {
                        UserSesion.setFirstOpen(false)
                        navigateToBalance()
                    } else {
                        progressListener.hide()
                        context?.showShortToast(R.string.info_masters_synced)
                    }
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_masters, result.exception), ErrorType.TYPE_RETROFIT)
            }
        }
    }

    private fun makeAutoSync() {
        setupSync()
        syncMovements()
    }

    private fun navigateToBalance() {
        context?.showShortToast(R.string.info_data_synced)
        findNavController().navigate(R.id.action_syncFragment_to_balanceFragment)
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
