package com.android.misfinanzas.ui.logged.balance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.BalanceRepository
import com.android.domain.repository.MovementRepository
import kotlinx.coroutines.launch

class BalanceViewModel(
    private val balanceRepository: BalanceRepository,
    private val movementRepository: MovementRepository
) : ViewModel() {

    val viewState: LiveData<BalanceViewState> get() = _viewState
    private val _viewState = MutableLiveData<BalanceViewState>()

    fun getBalance(query: String) {
        viewModelScope.launch {
            val balance = balanceRepository.getBalance(query)
            _viewState.postValue(BalanceViewState.BalanceLoaded(balance))
        }
    }

    fun getDiscardedMovements() {
        viewModelScope.launch {
            val discMovs = movementRepository.getDiscardedMovements()
            _viewState.postValue(BalanceViewState.DiscardedMovsLoaded(discMovs))
        }
    }

    fun insertDiscardedMovement(id: Int) {
        viewModelScope.launch {
            movementRepository.discardMovement(id)
            _viewState.postValue(BalanceViewState.MovementDiscarded)
        }
    }
}
