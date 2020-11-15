package com.android.misfinanzas.ui.balance

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.data.UserSesion
import com.android.data.local.RoomDataSource
import com.android.data.local.model.BalanceVO
import com.android.data.local.model.MovementVO
import com.android.data.local.model.UserVO
import com.android.data.local.repository.LocalRepositoryImp
import com.android.data.remote.RetrofitDataSource
import com.android.data.remote.repository.WebRepositoryImp
import com.android.data.utils.DateUtils
import com.android.data.utils.NetworkUtils
import com.android.data.utils.SharedPreferencesUtils
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.*
import com.android.misfinanzas.ui.movements.MovementsAdapter
import com.android.misfinanzas.ui.movements.movementDetail.MovementDetailFragment
import com.android.misfinanzas.ui.sync.SyncFragment
import kotlinx.android.synthetic.main.fragment_balance.*
import java.util.*


class BalanceFragment : BaseFragment(), OnMovementClickListener {

    private val TAG = this.javaClass.name
    private val REQUEST_PERMISSION_READ_SMS = 1

    private val viewModel by viewModels<BalanceViewModel> {
        BaseViewModelFactory(
            WebRepositoryImp(RetrofitDataSource()),
            LocalRepositoryImp(RoomDataSource(requireContext()))
        )
    }

    private lateinit var userObserver: Observer<Result<UserVO>>
    private lateinit var balanceObserver: Observer<Result<BalanceVO>>
    private lateinit var discardedObserver: Observer<Result<List<Int>>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_balance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        getLocalUser()

        btn_add_movement.setOnClickListener {
            navigateToAddMovement(MovementVO.getEmpty())
        }
    }

    private fun setupObservers() {
        userObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    if (result.data == null) {
                        progressListener.hide()
                        navigateToSync(false)
                    } else {
                        UserSesion.setUser(result.data)
                        determinateProcedure()
                    }
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_movements, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        }

        balanceObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    if (result.data == null) {
                        Toast.makeText(requireContext(), R.string.info_no_balance, Toast.LENGTH_SHORT).show()
                        navigateToSync(false)
                    } else {
                        showBalance(result.data)
                    }
                    progressListener.hide()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_balance, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        }

        discardedObserver = Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressListener.show()
                }
                is Result.Success -> {
                    if (result.data != null) {
                        getPotentialsMovementsFromSMS(result.data)
                    }
                    progressListener.hide()
                }
                is Result.Error -> {
                    progressListener.hide()
                    Toast.makeText(requireContext(), getString(R.string.error_getting_balance, result.exception), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, getString(R.string.error_room, result.exception))
                }
            }
        }
    }

    private fun determinateProcedure() {
        if (SharedPreferencesUtils.getAutoSyncConfig(requireContext())) {
            if (UserSesion.isFirstOpen()) {
                UserSesion.setFirstOpen(false)
                if (NetworkUtils.isConnected(requireContext(), getString(R.string.error_not_network_no_sync))) {
                    //Makes an autosync
                    navigateToSync(true)
                    return
                }
            }
        } else {
            if (UserSesion.getServerTimeZone() == null) {
                //get last gmt diff from shared preferences
                val gtmDiff = SharedPreferencesUtils.getDiffTimeToServer(requireContext())
                UserSesion.setServerTimeZone(gtmDiff)
            }
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

    private fun showBalance(balance: BalanceVO) {
        balance_view.showBalance(balance)
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
        val potentialMovements: MutableList<MovementVO> = ArrayList()

        listSms.forEach {
            val movementType = MovementUtils.getMovementTypeFromString(it.msg)
            val id = it.id.toInt() * -1

            if (movementType != MovementType.NOT_SELECTED && !discardedIds.contains(id)) {
                potentialMovements.add(
                    MovementVO(
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

    private fun setupRecyclerView() {
        rv_potential_movements.layoutManager = LinearLayoutManager(requireContext())
        rv_potential_movements.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setupRecyclerViewData(movements: List<MovementVO>) {
        rv_potential_movements.adapter = MovementsAdapter(requireContext(), movements, this)
        refreshRecyclerViewVisibility()
    }

    private fun refreshRecyclerViewVisibility() {
        if (rv_potential_movements.adapter?.itemCount!! <= 0) {
            tv_potential_movements.visibility = View.GONE
            rv_potential_movements.visibility = View.GONE
        } else {
            tv_potential_movements.visibility = View.VISIBLE
            rv_potential_movements.visibility = View.VISIBLE
        }
    }

    override fun onMovementClicked(movement: MovementVO?) {
        navigateToAddMovement(movement)
    }

    override fun onDiscardMovementClicked(id: Int, position: Int) {
        discard(id, position)
    }

    private fun discard(id: Int, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.cd_title_discard))
        builder.setMessage(getString(R.string.cd_desc_discard))

        builder.setPositiveButton(R.string.cd_yes) { dialog, which ->
            viewModel.insertDiscardedMovement(id)
            getDiscardedMovements()
            refreshRecyclerViewVisibility()
            Toast.makeText(requireContext(), R.string.info_movement_discarded, Toast.LENGTH_SHORT).show()
        }

        builder.setNeutralButton(R.string.cd_no) { dialog, which -> }
        builder.show()
    }

    private fun navigateToAddMovement(movement: MovementVO?) {
        val bundle = Bundle()
        bundle.putParcelable(MovementDetailFragment.MOVEMENT_DATA, movement)
        findNavController().navigate(R.id.action_balanceFragment_to_movementsFragment, bundle)
    }
}
