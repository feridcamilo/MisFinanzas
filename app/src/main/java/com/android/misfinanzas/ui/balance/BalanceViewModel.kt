package com.android.misfinanzas.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.BalanceRepository
import com.android.domain.repository.MovementRepository
import com.android.domain.repository.UserRepository
import com.android.domain.result.Result
import kotlinx.coroutines.launch

class BalanceViewModel(
    private val userRepository: UserRepository,
    private val balanceRepository: BalanceRepository,
    private val movementRepository: MovementRepository
) : ViewModel() {

    //val viewState: LiveData<BalanceViewState> get() = _viewState
    //private val _viewState = MutableLiveData<BalanceViewState>()

    fun getLocalUser() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(userRepository.getUser()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getLocalBalance(query: String) = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(balanceRepository.getBalance(query)))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    /*
    suspend fun getLocalBalances(query: String) {
        _viewState.postValue(BalanceViewState.PageLoading)
        try {
            _viewState.postValue(BalanceViewState.BalanceLoaded)
            localRepo.getBalance(query)
        } catch (e: Exception) {
            _viewState.postValue(BalanceViewState.ErrorLoadingBalance(e))
        }
    }
    */

    fun getDiscardedMovements() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(movementRepository.getDiscardedMovements()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun insertDiscardedMovement(id: Int) {
        viewModelScope.launch {
            movementRepository.discardMovement(id)
        }
    }
}
