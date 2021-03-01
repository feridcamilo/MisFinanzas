package com.android.misfinanzas.ui.logged.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.MasterRepository
import com.android.domain.repository.MovementRepository
import com.android.domain.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.*

class SyncViewModel(
    private val userRepository: UserRepository,
    private val movementRepository: MovementRepository,
    private val masterRepository: MasterRepository
) : ViewModel() {

    val viewState: LiveData<SyncViewState> get() = _viewState
    private val _viewState = MutableLiveData<SyncViewState>()

    fun getServerDateTime() {
        viewModelScope.launch {
            val serverDateTime = userRepository.getServerDateTime()
            _viewState.postValue(SyncViewState.ServerTimeLoaded(serverDateTime))
        }
    }

    fun syncAll(currentDate: Date) {
        viewModelScope.launch {
            syncMovements(false, currentDate)
            syncMasters(false, currentDate)
            _viewState.postValue(SyncViewState.AllSynced)
        }
    }

    suspend fun syncMovements(withObserve: Boolean, currentDate: Date) {
        movementRepository.upload()
        movementRepository.download()
        movementRepository.updateLastSyncMovements(currentDate)

        if (withObserve) {
            _viewState.postValue(SyncViewState.MovementsSynced)
        }
    }

    suspend fun syncMasters(withObserve: Boolean, currentDate: Date) {
        masterRepository.upload()
        masterRepository.download()
        masterRepository.updateLastSyncMasters(currentDate)

        if (withObserve) {
            _viewState.postValue(SyncViewState.MastersSynced)
        }
    }

    suspend fun getLastSyncMovements(): Date? {
        return movementRepository.getLastSyncMovements()
    }

    suspend fun getLastSyncMasters(): Date? {
        return masterRepository.getLastSyncMasters()
    }

    fun cleanDiscarded() {
        viewModelScope.launch {
            movementRepository.clearDiscardedMovements()
            _viewState.postValue(SyncViewState.DiscardedCleared)
        }
    }

}
