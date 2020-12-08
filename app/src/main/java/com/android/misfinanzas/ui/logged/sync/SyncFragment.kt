package com.android.misfinanzas.ui.logged.sync

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
import com.android.domain.utils.DateUtils
import com.android.domain.utils.StringUtils.Companion.EMPTY
import com.android.domain.utils.StringUtils.Companion.POINT
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.databinding.FragmentSyncBinding
import com.android.misfinanzas.utils.isConnected
import com.android.misfinanzas.utils.openURL
import com.android.misfinanzas.utils.showLongToast
import com.android.misfinanzas.utils.showShortToast
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class SyncFragment : BaseFragment() {

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
    private var fromMovements: Boolean = false

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
        setupViewModel()
        setupEvents()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        progressListener.show()
        if (context?.isConnected(getString(R.string.error_not_network_no_sync)) == true) {
            getServerDateTime()
        } else {
            activity?.onBackPressed()
        }
    }

    private fun setupEvents() = with(binding) {
        swAutoSync.isChecked = SharedPreferencesUtils.getAutoSyncOnOpen(requireContext())

        swAutoSync.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesUtils.setAutoSyncOnOpen(requireContext(), isChecked)
            context?.showShortToast(R.string.info_config_saved)
        }

        swSyncOnEdit.isChecked = SharedPreferencesUtils.getAutoSyncOnEdit(requireContext())

        swSyncOnEdit.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesUtils.setAutoSyncOnEdit(requireContext(), isChecked)
            context?.showShortToast(R.string.info_config_saved)
        }

        ibInfo.setOnClickListener { context?.showLongToast(R.string.info_difference_time) }

        tvLink.setOnClickListener { context?.openURL(AppConfig.BASE_URL) }

        btnSyncMovements.setOnClickListener {
            if (context?.isConnected(getString(R.string.error_not_network_no_sync)) == true) {
                progressListener.show()
                lifecycleScope.launch {
                    //Manual movements sync
                    viewModel.syncMovements(true, DateUtils.getCurrentDateTime())
                }
            }
        }

        btnSyncMasters.setOnClickListener {
            if (context?.isConnected(getString(R.string.error_not_network_no_sync)) == true) {
                progressListener.show()
                lifecycleScope.launch {
                    //Manual masters sync
                    viewModel.syncMasters(true, DateUtils.getCurrentDateTime())
                }
            }
        }

        btnCleanDiscarded.setOnClickListener {
            progressListener.show()
            viewModel.cleanDiscarded()
        }
    }

    private fun getServerDateTime() {
        viewModel.getServerDateTime()
    }

    private val viewStateObserver = Observer<SyncViewState> { state ->
        if (!autoSync && state !is SyncViewState.AllSynced) {
            progressListener.hide()
        }

        when (state) {
            is SyncViewState.ServerTimeLoaded -> {
                setServerDateTime(state.serverDateTime)
                determinateProcedure()
            }
            is SyncViewState.AllSynced -> syncAllResult()
            is SyncViewState.MovementsSynced -> syncMovementsResult()
            is SyncViewState.MastersSynced -> syncMastersResult()
            is SyncViewState.DiscardedCleared -> context?.showLongToast(R.string.info_movements_discarded_restored)

        }
    }

    private fun setServerDateTime(value: String) = with(binding) {
        val serverDateTime = DateUtils.getDateTimeFormat_AM_PM().parse(value.replace(POINT, EMPTY))!!
        //Save server date time in UserSession
        UserSesion.setServerDateTime(serverDateTime)

        val gtmDiff = UserSesion.getServerTimeZone()!!.displayName

        //Save GTM diff in shared preferences
        UserSesion.setServerDateTime(serverDateTime)
        SharedPreferencesUtils.setDiffTimeToServer(requireContext(), gtmDiff)

        val serverDateTimeFormated = DateUtils.getDateTimeFormat_AM_PM().format(serverDateTime)
        tvServerDateTime.text = getString(R.string.server_datetime_value, serverDateTimeFormated, gtmDiff)
    }

    //is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_srv_dtm, result.exception), ErrorType.TYPE_RETROFIT)

    private fun determinateProcedure() {
        if (autoSync) {
            autoSync = false
            viewModel.syncAll(DateUtils.getCurrentDateTime())
            return
        }

        showLastSyncValues()
    }

    private fun showLastSyncValues() = with(binding) {
        lifecycleScope.launch {
            val lastSyncMovements = viewModel.getLastSyncMovements()
            val lastSyncMasters = viewModel.getLastSyncMasters()

            if (lastSyncMovements != null) {
                val formattedLastSyncMovements = DateUtils.getDateTimeFormat_AM_PM().format(DateUtils.getDateTimeToWebService(lastSyncMovements))
                tvLastSyncMovements.text = getString(R.string.last_sync_movements, formattedLastSyncMovements)
            } else {
                tvLastSyncMovements.text = getString(R.string.last_sync_movements, getString(R.string.never_synced))
            }

            if (lastSyncMasters != null) {
                val formattedLastSyncMasters = DateUtils.getDateTimeFormat_AM_PM().format(DateUtils.getDateTimeToWebService(lastSyncMasters))
                tvLastSyncMasters.text = getString(R.string.last_sync_masters, formattedLastSyncMasters)
            } else {
                tvLastSyncMasters.text = getString(R.string.last_sync_masters, getString(R.string.never_synced))
            }
        }
    }

    private fun syncAllResult() {
        context?.showShortToast(R.string.info_data_synced)
        UserSesion.setFirstOpen(false)
        goToBalanceScreen()
    }

    private fun goToBalanceScreen() {
        findNavController().navigate(R.id.action_syncFragment_to_balanceFragment)
    }

    private fun syncMovementsResult() {
        getServerDateTime()
        context?.showShortToast(R.string.info_movements_synced)
    }

    //is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_movements, result.exception), ErrorType.TYPE_RETROFIT)

    private fun syncMastersResult() {
        getServerDateTime()
        context?.showShortToast(R.string.info_masters_synced)
    }

    //is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_masters, result.exception), ErrorType.TYPE_RETROFIT)

}
