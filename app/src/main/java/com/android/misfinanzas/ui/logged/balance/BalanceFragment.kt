package com.android.misfinanzas.ui.logged.balance

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.UserSesion
import com.android.domain.utils.DateUtils
import com.android.misfinanzas.R
import com.android.misfinanzas.base.MovementType
import com.android.misfinanzas.base.MovementUtils
import com.android.misfinanzas.base.Sms
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
import com.android.misfinanzas.utils.viewbinding.viewBinding
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class BalanceFragment : Fragment(R.layout.fragment_balance) {

    private val REQUEST_PERMISSION_READ_SMS = 1

    private val viewModel by viewModel<BalanceViewModel>()
    private val configViewModel by viewModel<ConfigViewModel>()

    private val movementsAdapter by lazy { MovementsAdapter() }

    private val binding by viewBinding<FragmentBalanceBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupSyncObserver()
        checkSync()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    private fun checkSync() {
        showLoader()
        lifecycleScope.launch {
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
                viewModel.sync()
            } else {
                getBalance()
            }
        }
    }

    private fun setupSyncObserver() {
        getEventBus(EventSubject.SYNC).observe(viewLifecycleOwner, syncStateObserver)
    }

    private val syncStateObserver = Observer<Any> { state ->
        when (state) {
            SyncState.Success -> getBalance()
        }
    }

    private fun getBalance() {
        viewModel.getBalance(getString(R.string.query_balance))
    }

    private val viewStateObserver = Observer<BalanceViewState> { state ->
        hideLoader()
        when (state) {
            is BalanceViewState.BalanceLoaded -> balanceLoaded(state.balance)
            is BalanceViewState.DiscardedMovsLoaded -> getPotentialsMovementsFromSMS(state.discardedIds)
            is BalanceViewState.MovementDiscarded -> movementDiscarded()
            is BalanceViewState.SynchronizedData -> getBalance()
        }
    }

    private fun balanceLoaded(balance: BalanceModel) {
        binding.balanceView.showBalance(balance)
        setupDiscardedMovs()
    }

    private fun setupDiscardedMovs() {
        if (checkAndRequestPermissions()) {
            setupRecyclerView()
            showLoader()
            viewModel.getDiscardedMovements()
            setupEvents()
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val sms = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_SMS)
        if (sms != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_SMS), REQUEST_PERMISSION_READ_SMS)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_PERMISSION_READ_SMS -> {
                    setupDiscardedMovs()
                }
            }
        }
    }

    private fun setupEvents() = with(binding) {
        btnAddMovement.setOnClickListener {
            navigateToAddMovement(MovementModel.getEmpty())
        }
    }

    private fun getPotentialsMovementsFromSMS(discardedIds: List<Int>) {
        val listSms: List<Sms> = Sms.getAllSms(requireContext())
        val potentialMovements: MutableList<MovementModel> = ArrayList()

        listSms.forEach {
            val movementType = MovementUtils.getMovementTypeFromString(it.msg)
            val id = it.id.toInt() * -1

            if (movementType != MovementType.NOT_SELECTED && !discardedIds.contains(id)) {
                potentialMovements.add(
                    MovementModel(
                        id,//Autoincrement
                        movementType,
                        MovementUtils.getMoneyFromString(it.msg),
                        MovementUtils.getMovementDescriptionFromString(it.msg),
                        null,
                        null,
                        null,
                        DateUtils.getDateToWebService(MovementUtils.getDateFromString(it.msg) ?: DateUtils.getCurrentDateTime()),
                        null,
                        null,
                        null
                    )
                )
            }
        }

        setupRecyclerViewData(potentialMovements)
    }

    private fun setupRecyclerView() = with(binding.rvPotentialMovements) {
        adapter = movementsAdapter
        layoutManager = LinearLayoutManager(requireContext())
        movementsAdapter.setOnActionItemListener(actionListener)
        addItemDecoration(DividerItemDecoration(requireContext(), androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(movements: List<MovementModel>) {
        movementsAdapter.submitList(movements)
    }

    private fun refreshRecyclerViewVisibility() = with(binding) {
        if (rvPotentialMovements.adapter?.itemCount!! <= 0) {
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
            showLoader()
            viewModel.insertDiscardedMovement(id)
        }

        builder.setNeutralButton(R.string.cd_no) { _, _ -> }
        builder.show()
    }

    private fun movementDiscarded() {
        setupDiscardedMovs()
        refreshRecyclerViewVisibility()
        context?.showShortToast(R.string.info_movement_discarded)
    }

    private fun navigateToAddMovement(movement: MovementModel?) {
        val bundle = Bundle()
        bundle.putSerializable(MovementDetailFragment.MOVEMENT_DATA, movement)
        findNavController().navigate(R.id.action_balanceFragment_to_movementsFragment, bundle)
    }

}
