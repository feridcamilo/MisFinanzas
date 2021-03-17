package com.android.misfinanzas.ui.logged.balance

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.UserSesion
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentBalanceBinding
import com.android.misfinanzas.models.BalanceModel
import com.android.misfinanzas.models.MovementModel
import com.android.misfinanzas.sync.SyncState
import com.android.misfinanzas.ui.logged.config.ConfigViewModel
import com.android.misfinanzas.ui.logged.movements.adapter.MovementsAdapter
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment
import com.android.misfinanzas.utils.*
import com.android.misfinanzas.utils.events.EventSubject
import com.android.misfinanzas.utils.events.getEventBus
import com.android.misfinanzas.utils.permissions.Permission
import com.android.misfinanzas.utils.permissions.requestPermissions
import com.android.misfinanzas.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel

class BalanceFragment : Fragment(R.layout.fragment_balance) {

    private val viewModel by viewModel<BalanceViewModel>()
    private val configViewModel by viewModel<ConfigViewModel>()

    private val movementsAdapter by lazy { MovementsAdapter() }
    private val smsPermission = requestPermissions(Permission.SmsRead())
    private val binding by viewBinding<FragmentBalanceBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupSyncObserver()
        checkSync()
        setupEvents()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    private fun checkSync() {
        if (UserSesion.getServerTimeZone() == null) {
            //get last gmt diff from shared preferences
            val gtmDiff = configViewModel.getDiffTimeWithServer()
            UserSesion.setServerTimeZone(gtmDiff)
        }

        val c1 = configViewModel.isAutoSyncOnOpen()
        val c2 = UserSesion.isFirstOpen()
        UserSesion.setFirstOpen(false)
        val c3 = context?.isConnected(getString(R.string.error_not_network_no_sync)) == true
        if (c1 && c2 && c3) {
            showLoader()
            viewModel.sync()
        } else {
            getBalance()
        }
    }

    private fun setupSyncObserver() {
        getEventBus(EventSubject.SYNC).observe(viewLifecycleOwner, syncStateObserver)
    }

    private val syncStateObserver = Observer<Any> { state ->
        when (state) {
            is SyncState.Success,
            is SyncState.Failed -> getBalance()
        }
    }

    private fun getBalance() {
        viewModel.getBalance()
    }

    private val viewStateObserver = Observer<BalanceViewState> { state ->
        hideLoader()
        when (state) {
            is BalanceViewState.BalanceLoaded -> showBalance(state.balance)
            is BalanceViewState.PotentialsMovementsLoaded -> setupRecyclerViewData(state.potentialsMovements)
            is BalanceViewState.MovementDiscarded -> movementDiscarded()
        }
    }

    private fun showBalance(balance: BalanceModel) {
        binding.balanceView.showBalance(balance)
        setupPotentialMovements()
    }

    private fun setupPotentialMovements() {
        smsPermission.runWithPermissions {
            setupRecyclerView()
            getPotentialMovements()
        }
    }

    private fun getPotentialMovements() {
        viewModel.getPotentialMovementsFromSMS()
    }

    private fun setupEvents() = with(binding) {
        btnAddMovement.setOnClickListener {
            navigateToAddMovement(null)
        }
    }

    private fun setupRecyclerView() = with(binding.rvPotentialMovements) {
        adapter = movementsAdapter
        layoutManager = LinearLayoutManager(requireContext())
        movementsAdapter.setOnActionItemListener(actionListener)
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(movements: List<MovementModel>) {
        movementsAdapter.submitList(movements)
        refreshRecyclerViewVisibility()
    }

    private fun refreshRecyclerViewVisibility() = with(binding) {
        if (movementsAdapter.itemCount <= 0) {
            rvPotentialMovements.gone()
            tvPotentialMovements.gone()
        } else {
            rvPotentialMovements.visible()
            tvPotentialMovements.visible()
        }
    }

    private val actionListener = object : MovementsAdapter.OnActionItemListener {
        override fun onMovementClicked(movement: MovementModel?) {
            navigateToAddMovement(movement)
        }

        override fun onDiscardMovementClicked(id: Int) {
            discard(id)
        }
    }

    private fun discard(id: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.cd_title_discard))
        builder.setMessage(getString(R.string.cd_desc_discard))

        builder.setPositiveButton(R.string.cd_yes) { _, _ ->
            viewModel.insertDiscardedMovement(id)
        }

        builder.setNeutralButton(R.string.cd_no) { _, _ -> }
        builder.show()
    }

    private fun movementDiscarded() {
        context?.showShortToast(R.string.info_movement_discarded)
        getPotentialMovements()
    }

    private fun navigateToAddMovement(movement: MovementModel?) {
        val bundle = Bundle()
        bundle.putSerializable(MovementDetailFragment.MOVEMENT_DATA, movement)
        findNavController().navigate(R.id.action_to_movementDetailFragment, bundle)
    }

}
