package com.android.misfinanzas.ui.logged.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.MasterRepository
import com.android.domain.repository.MovementRepository
import com.android.domain.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.*

class ConfigViewModel(
    private val userRepository: UserRepository,
    private val movementRepository: MovementRepository,
    private val masterRepository: MasterRepository
) : ViewModel() {

    val viewState: LiveData<ConfigViewState> get() = _viewState
    private val _viewState = MutableLiveData<ConfigViewState>()

    suspend fun getDiffTimeWithServer(): String {
        return userRepository.getDiffTimeWithServer()
    }

    suspend fun getLastSyncMovements(): Date? {
        return movementRepository.getLastSyncMovements()
    }

    suspend fun getLastSyncMasters(): Date? {
        return masterRepository.getLastSyncMasters()
    }

    suspend fun isAutoSyncOnOpen(): Boolean {
        return userRepository.isAutoSyncOnOpen()
    }

    suspend fun isAutoSyncOnEdit(): Boolean {
        return userRepository.isAutoSyncOnEdit()
    }

    fun setAutoSyncOnOpen(value: Boolean) {
        viewModelScope.launch {
            userRepository.setAutoSyncOnOpen(value)
        }
    }

    fun setAutoSyncOnEdit(value: Boolean) {
        viewModelScope.launch {
            userRepository.setAutoSyncOnEdit(value)
        }
    }

    fun cleanDiscarded() {
        viewModelScope.launch {
            movementRepository.clearDiscardedMovements()
            _viewState.postValue(ConfigViewState.DiscardedCleared)
        }
    }

}