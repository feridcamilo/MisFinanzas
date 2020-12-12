package com.android.misfinanzas.ui.logged.balance

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.data.local.model.converters.MovementConverter
import com.android.data.utils.SharedPreferencesUtils
import com.android.domain.UserSesion
import com.android.domain.model.Balance
import com.android.domain.model.Movement
import com.android.domain.utils.DateUtils
import com.android.misfinanzas.R
import com.android.misfinanzas.base.*
import com.android.misfinanzas.databinding.FragmentBalanceBinding
import com.android.misfinanzas.ui.logged.movements.MovementsAdapter
import com.android.misfinanzas.ui.logged.movements.movementDetail.MovementDetailFragment
import com.android.misfinanzas.ui.logged.sync.SyncFragment
import com.android.misfinanzas.utils.isConnected
import com.android.misfinanzas.utils.showShortToast
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class BalanceFragment : BaseFragment(), MovementClickListener {

    private val TAG = this.javaClass.name
    private val REQUEST_PERMISSION_READ_SMS = 1

    private val viewModel by viewModel<BalanceViewModel>()
    private lateinit var binding: FragmentBalanceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBalanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        determinateProcedure()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    private fun determinateProcedure() {
        progressListener.show()

        //If auto sync is configured
        if (SharedPreferencesUtils.getAutoSyncOnOpen(requireContext())) {
            if (UserSesion.isFirstOpen()) {
                UserSesion.setFirstOpen(false)
                if (context?.isConnected(getString(R.string.error_not_network_no_sync)) == true) {
                    //Makes an autosync
                    goToSyncScreen()
                    return
                }
            }
        }

        if (UserSesion.getServerTimeZone() == null) {
            //get last gmt diff from shared preferences
            val gtmDiff = SharedPreferencesUtils.getDiffTimeToServer(requireContext())
            UserSesion.setServerTimeZone(gtmDiff)
        }

        viewModel.getBalance(getString(R.string.query_balance))
    }

    private val viewStateObserver = Observer<BalanceViewState> { state ->
        progressListener.hide()
        when (state) {
            is BalanceViewState.BalanceLoaded -> balanceLoaded(state.balance)
            is BalanceViewState.DiscardedMovsLoaded -> getPotentialsMovementsFromSMS(state.discardedIds)
            is BalanceViewState.ErrorLoadingBalance -> TODO()
            is BalanceViewState.MovementDiscarded -> movementDiscarded()
            is BalanceViewState.ErrorDiscardingMovement -> TODO()

        }
    }

    private fun balanceLoaded(balance: Balance) {
        binding.balanceView.showBalance(balance)
        setupDiscardedMovs()
    }

    private fun setupDiscardedMovs() {
        if (checkAndRequestPermissions()) {
            setupRecyclerView()
            progressListener.show()
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
            navigateToAddMovement(Movement.getEmpty())
        }
    }

    private fun getPotentialsMovementsFromSMS(discardedIds: List<Int>) {
        val listSms: List<Sms> = Sms.getAllSms(requireContext())
        val potentialMovements: MutableList<Movement> = ArrayList()

        listSms.forEach {
            val movementType = MovementUtils.getMovementTypeFromString(it.msg)
            val id = it.id.toInt() * -1

            if (movementType != MovementType.NOT_SELECTED && !discardedIds.contains(id)) {
                potentialMovements.add(
                    Movement(
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

    private fun goToSyncScreen() {
        val bundle = Bundle()
        bundle.putBoolean(SyncFragment.FROM_BALANCE, true)
        bundle.putBoolean(SyncFragment.AUTO_SYNC, true)
        findNavController().navigate(R.id.action_balanceFragment_to_syncFragment, bundle)
    }

    private fun setupRecyclerView() = with(binding) {
        rvPotentialMovements.layoutManager = LinearLayoutManager(requireContext())
        rvPotentialMovements.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(movements: List<Movement>) {
        binding.rvPotentialMovements.adapter = MovementsAdapter(requireContext(), movements, this)
        refreshRecyclerViewVisibility()
    }

    private fun refreshRecyclerViewVisibility() = with(binding) {
        if (rvPotentialMovements.adapter?.itemCount!! <= 0) {
            rvPotentialMovements.visibility = View.GONE
            tvPotentialMovements.visibility = View.GONE
        } else {
            rvPotentialMovements.visibility = View.VISIBLE
            tvPotentialMovements.visibility = View.VISIBLE
        }
    }

    override fun onMovementClicked(movement: Movement?) {
        navigateToAddMovement(movement)
    }

    override fun onDiscardMovementClicked(id: Int, position: Int) {
        discard(id)
    }

    private fun discard(id: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.cd_title_discard))
        builder.setMessage(getString(R.string.cd_desc_discard))

        builder.setPositiveButton(R.string.cd_yes) { _, _ ->
            progressListener.show()
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

    private fun navigateToAddMovement(movement: Movement?) {
        val bundle = Bundle()
        bundle.putString(MovementDetailFragment.MOVEMENT_DATA, MovementConverter().movementToString(movement!!))
        findNavController().navigate(R.id.action_balanceFragment_to_movementsFragment, bundle)
    }

}
