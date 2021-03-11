package com.android.misfinanzas.ui.logged

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentMainBinding
import com.android.misfinanzas.sync.SyncState
import com.android.misfinanzas.sync.SyncType
import com.android.misfinanzas.sync.SyncWorker
import com.android.misfinanzas.ui.MainActivity
import com.android.misfinanzas.utils.*
import com.android.misfinanzas.utils.events.EventSubject
import com.android.misfinanzas.utils.events.getEventBus
import com.android.misfinanzas.utils.viewbinding.viewBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding<FragmentMainBinding>()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupEvents()
        setupSyncObserver()
    }

    private fun setupNavigation() {
        setActionBar(binding.toolbar)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.balanceFragment,
                R.id.movementsFragment,
                R.id.mastersFragment,
                R.id.configFragment
            )
        )

        val navHostFragment = childFragmentManager.findFragmentById(R.id.logged_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        (activity as MainActivity).navController = navController
        setupActionBarWithNavController((activity as AppCompatActivity), navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)
    }

    private fun setupEvents() = with(binding) {
        btnSync.setOnClickListener {
            startSync()
        }

        pbSyncLoader.setOnClickListener {
            showSyncingMessage()
        }
    }

    private fun setupSyncObserver() {
        getEventBus(EventSubject.SYNC).observe(viewLifecycleOwner, syncStateObserver)
    }

    private val syncStateObserver = Observer<Any> { state ->
        when (state) {
            is SyncState.Requested -> startSync(state.type)
            is SyncState.InProgress -> showSyncingMessage()
            is SyncState.Success -> {
                hideSyncLoader()
                context?.showShortToast(R.string.info_data_synced)
            }
            is SyncState.Failed -> {
                hideSyncLoader()
                context?.showLongToast(getString(R.string.error_synchronizing, state.exception.message))
            }
        }
    }

    private fun startSync(type: SyncType = SyncType.SYNC_ALL) {
        if (context?.isConnected(getString(R.string.error_not_network_no_sync)) == false) return
        showSyncLoader()
        SyncWorker.enqueue(context ?: return, type)
    }

    private fun showSyncLoader() = with(binding) {
        btnSync.gone()
        pbSyncLoader.visible()
    }

    private fun hideSyncLoader() = with(binding) {
        btnSync.visible()
        pbSyncLoader.gone()
    }

    private fun showSyncingMessage() {
        context?.showShortToast(R.string.synchronizing)
    }

}
