package com.android.misfinanzas.ui.movements.movementDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.data.local.model.MovementVO
import com.android.data.local.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository
import kotlinx.coroutines.launch

class MovementDetailViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {

    fun insertLocalMovement(movement: MovementVO) {
        viewModelScope.launch {
            localRepo.insertMovement(movement)
        }
    }

    fun deleteLocalMovement(movement: MovementVO) {
        viewModelScope.launch {
            localRepo.deleteMovement(movement)
        }
    }

    fun insertDiscardedMovement(id: Int) {
        viewModelScope.launch {
            localRepo.discardMovement(id)
        }
    }
}
