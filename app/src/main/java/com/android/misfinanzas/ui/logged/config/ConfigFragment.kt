package com.android.misfinanzas.ui.logged.config

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.android.domain.AppConfig
import com.android.domain.UserSesion
import com.android.domain.utils.DateUtils
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentConfigBinding
import com.android.misfinanzas.utils.openURL
import com.android.misfinanzas.utils.showLongToast
import com.android.misfinanzas.utils.showShortToast
import com.android.misfinanzas.utils.viewbinding.viewBinding
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class ConfigFragment : Fragment(R.layout.fragment_config) {

    private val viewModel by viewModel<ConfigViewModel>()
    private val binding by viewBinding<FragmentConfigBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupEvents()
        showInfo()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    private fun setupEvents() = with(binding) {
        swAutoSync.setOnClickListener {
            viewModel.setAutoSyncOnOpen(swAutoSync.isChecked)
            context?.showShortToast(R.string.info_config_saved)
        }

        swSyncOnEdit.setOnClickListener {
            viewModel.setAutoSyncOnEdit(swSyncOnEdit.isChecked)
            context?.showShortToast(R.string.info_config_saved)
        }

        ibInfo.setOnClickListener { context?.showLongToast(R.string.info_difference_time) }

        tvLink.setOnClickListener { context?.openURL(AppConfig.BASE_URL) }

        btnCleanDiscarded.setOnClickListener {
            viewModel.cleanDiscarded()
        }
    }

    private val viewStateObserver = Observer<ConfigViewState> { state ->
        when (state) {
            is ConfigViewState.DiscardedCleared -> context?.showLongToast(R.string.info_movements_discarded_restored)

        }
    }

    private fun showInfo() = with(binding) {
        lifecycleScope.launch {
            val diffTimeWithServer = UserSesion.getServerDateTime()
            val serverDateTimeFormatted = DateUtils.getDateTimeFormat_AM_PM().format(diffTimeWithServer)
            val gtmDiff = UserSesion.getServerTimeZone()!!.displayName
            tvServerDateTime.text = getString(R.string.server_datetime_value, serverDateTimeFormatted, gtmDiff)


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

            swAutoSync.isChecked = viewModel.isAutoSyncOnOpen()
            swSyncOnEdit.isChecked = viewModel.isAutoSyncOnEdit()
        }
    }

}
