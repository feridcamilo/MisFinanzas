package com.android.misfinanzas.ui.logged.movements.movementDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.model.Movement
import com.android.domain.repository.MovementRepository
import kotlinx.coroutines.launch

class MovementDetailViewModel(
    private val movementRepository: MovementRepository
) : ViewModel() {

    fun insertLocalMovement(movement: Movement) {
        viewModelScope.launch {
            movementRepository.insertMovement(movement)
        }
    }

    fun deleteLocalMovement(movement: Movement) {
        viewModelScope.launch {
            movementRepository.deleteMovement(movement)
        }
    }

    fun insertDiscardedMovement(id: Int) {
        viewModelScope.launch {
            movementRepository.discardMovement(id)
        }
    }
}
