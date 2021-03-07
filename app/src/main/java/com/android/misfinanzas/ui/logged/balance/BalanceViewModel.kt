package com.android.misfinanzas.ui.logged.balance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.BalanceRepository
import com.android.domain.repository.MovementRepository
import com.android.domain.utils.DateUtils
import com.android.misfinanzas.base.MovementType
import com.android.misfinanzas.mappers.BalanceMapper
import com.android.misfinanzas.models.MovementModel
import com.android.misfinanzas.models.Sms
import com.android.misfinanzas.sync.SyncManager
import com.android.misfinanzas.utils.MovementUtils
import com.android.misfinanzas.utils.SmsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class BalanceViewModel(
    private val balanceRepository: BalanceRepository,
    private val movementRepository: MovementRepository,
    private val balanceMapper: BalanceMapper,
    private val syncManager: SyncManager,
    private val smsUtils: SmsUtils
) : ViewModel() {

    val viewState: LiveData<BalanceViewState> get() = _viewState
    private val _viewState = MutableLiveData<BalanceViewState>()

    fun getBalance() {
        viewModelScope.launch {
            val balance = balanceRepository.getBalance()
            _viewState.postValue(BalanceViewState.BalanceLoaded(balanceMapper.map(balance)))
        }
    }

    fun getPotentialMovementsFromSMS() {
        viewModelScope.launch(Dispatchers.Default) {
            val discardedMovements = movementRepository.getDiscardedMovements()
            val listSms: List<Sms> = smsUtils.getAllSms()
            val potentialMovements: MutableList<MovementModel> = ArrayList()

            listSms.forEach {
                val movementType = MovementUtils.getMovementTypeFromString(it.msg)
                val id = it.id.toInt() * -1

                if (movementType != MovementType.NOT_SELECTED && !discardedMovements.contains(id)) {
                    potentialMovements.add(
                        MovementModel(
                            id,//Autoincrement
                            movementType,
                            MovementUtils.getMoneyFromString(it.msg),
                            MovementUtils.getMovementDescriptionFromString(it.msg),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            DateUtils.getDateToWebService(MovementUtils.getDateFromString(it.msg) ?: DateUtils.getCurrentDateTime()),
                            null,
                            null,
                            null,
                            null
                        )
                    )
                }
            }
            _viewState.postValue(BalanceViewState.PotentialsMovementsLoaded(potentialMovements))
        }
    }


    fun insertDiscardedMovement(id: Int) {
        viewModelScope.launch {
            movementRepository.discardMovement(id)
            _viewState.postValue(BalanceViewState.MovementDiscarded)
        }
    }

    fun sync() {
        viewModelScope.launch(Dispatchers.IO) {
            syncManager.sync()
        }
    }

}
