package com.android.misfinanzas.ui.balance

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
import com.android.domain.model.User
import com.android.domain.result.Result
import com.android.domain.utils.DateUtils
import com.android.misfinanzas.R
import com.android.misfinanzas.base.*
import com.android.misfinanzas.databinding.FragmentBalanceBinding
import com.android.misfinanzas.ui.movements.MovementsAdapter
import com.android.misfinanzas.ui.movements.movementDetail.MovementDetailFragment
import com.android.misfinanzas.ui.sync.SyncFragment
import com.android.misfinanzas.utils.isConnected
import com.android.misfinanzas.utils.showShortToast
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class BalanceFragment : BaseFragment(), MovementClickListener {

    private val TAG = this.javaClass.name
    private val REQUEST_PERMISSION_READ_SMS = 1

    private val viewModel by viewModel<BalanceViewModel>()
    private lateinit var binding: FragmentBalanceBinding

    private lateinit var userObserver: Observer<Result<User?>>
    private lateinit var balanceObserver: Observer<Result<Balance>>
    private lateinit var discardedObserver: Observer<Result<List<Int>>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBalanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupEvents()
        getLocalUser()
    }

    private fun setupEvents() = with(binding) {
        btnAddMovement.setOnClickListener {
            navigateToAddMovement(Movement.getEmpty())
        }
    }

    private fun setupViewModel() {
        //viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)

        userObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    if (result.data == null) {
                        progressListener.hide()
                        navigateToSync(false)
                    } else {
                        UserSesion.setUser(result.data!!)
                        determinateProcedure()
                    }
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_user, result.exception), ErrorType.TYPE_ROOM)
            }
        }

        balanceObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    binding.balanceView.showBalance(result.data)
                    progressListener.hide()
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_balance, result.exception), ErrorType.TYPE_ROOM)
            }
        }

        discardedObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    getPotentialsMovementsFromSMS(result.data)
                    progressListener.hide()
                }
                is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_discarded_movs, result.exception), ErrorType.TYPE_ROOM)
            }
        }
    }

    /*
    private val viewStateObserver = Observer<BalanceViewState> { state ->
        when (state) {
            is BalanceViewState.PageLoading -> progressListener.show()
            is BalanceViewState.UserLoaded -> TODO()
            is BalanceViewState.ErrorLoadingUser -> showExceptionMessage(getString(R.string.error_getting_user, it.))
            is BalanceViewState.BalanceLoaded -> TODO()
            is BalanceViewState.ErrorLoadingBalance -> TODO()
            is BalanceViewState.MovementDiscarded -> TODO()
            is BalanceViewState.ErrorDiscardingMovement -> TODO()
        }
    }

    private fun processUser(data: User) {
        if (data == null) {
            progressListener.hide()
            navigateToSync(false)
        } else {
            UserSesion.setUser(data)
            determinateProcedure()
        }
    }

    private fun processBalance(data: Balance) {
        if (data == null) {
            context?.showShortToast(R.string.info_no_balance)
            navigateToSync(false)
        } else {
            showBalance(data)
        }
        progressListener.hide()
    }

    private fun showExceptionMessage(message: String) {
        progressListener.hide()
        context?.showShortToast(message)
        Log.e(TAG, message)
    }
    */

    private fun determinateProcedure() {
        if (SharedPreferencesUtils.getAutoSyncConfig(requireContext())) {
            if (UserSesion.isFirstOpen()) {
                UserSesion.setFirstOpen(false)
                if (context?.isConnected(getString(R.string.error_not_network_no_sync)) == true) {
                    //Makes an autosync
                    navigateToSync(true)
                    return
                }
            }
        }

        if (UserSesion.getServerTimeZone() == null) {
            //get last gmt diff from shared preferences
            val gtmDiff = SharedPreferencesUtils.getDiffTimeToServer(requireContext())
            UserSesion.setServerTimeZone(gtmDiff)
        }

        setupRecyclerView()
        getLocalBalance()
        if (checkAndRequestPermissions()) {
            getDiscardedMovements()
        }
    }

    private fun getLocalUser() {
        viewModel.getLocalUser().observe(viewLifecycleOwner, userObserver)
    }

    private fun navigateToSync(auto: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(SyncFragment.FROM_BALANCE, true)
        bundle.putBoolean(SyncFragment.AUTO_SYNC, auto)
        findNavController().navigate(R.id.action_balanceFragment_to_syncFragment, bundle)
    }

    private fun getLocalBalance() {
        viewModel.getLocalBalance(getString(R.string.query_balance)).observe(viewLifecycleOwner, balanceObserver)
    }

    private fun getDiscardedMovements() {
        viewModel.getDiscardedMovements().observe(viewLifecycleOwner, discardedObserver)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_PERMISSION_READ_SMS -> {
                    getDiscardedMovements()
                }
            }
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

    private fun checkAndRequestPermissions(): Boolean {
        val sms = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_SMS)
        if (sms != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_SMS), REQUEST_PERMISSION_READ_SMS)
            return false
        }
        return true
    }

    private fun setupRecyclerView() = with(binding) {
        rvPotentialMovements.layoutManager = LinearLayoutManager(requireContext())
        rvPotentialMovements.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(movements: List<Movement>) {
        binding.rvPotentialMovements.adapter = MovementsAdapter(requireContext(), movements, this)
        refreshRecyclerViewVisibility()
    }

    private fun refreshRecyclerViewVisibility() = with(binding.rvPotentialMovements) {
        if (adapter?.itemCount!! <= 0) {
            visibility = View.GONE
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            visibility = View.VISIBLE
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
            viewModel.insertDiscardedMovement(id)
            getDiscardedMovements()
            refreshRecyclerViewVisibility()
            context?.showShortToast(R.string.info_movement_discarded)
        }

        builder.setNeutralButton(R.string.cd_no) { _, _ -> }
        builder.show()
    }

    private fun navigateToAddMovement(movement: Movement?) {
        val bundle = Bundle()
        bundle.putString(MovementDetailFragment.MOVEMENT_DATA, MovementConverter().movementToString(movement!!))
        findNavController().navigate(R.id.action_balanceFragment_to_movementsFragment, bundle)
    }

}
