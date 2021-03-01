package com.android.misfinanzas.ui.logged.movements.movementDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.MovementRepository
import com.android.misfinanzas.mappers.MovementMapper
import com.android.misfinanzas.models.MovementModel
import kotlinx.coroutines.launch

class MovementDetailViewModel(
    private val movementRepository: MovementRepository,
    private val movementMapper: MovementMapper
) : ViewModel() {

    fun insertLocalMovement(movement: MovementModel) {
        viewModelScope.launch {
            movementRepository.insertMovement(movementMapper.mapToDomain(movement))
        }
    }

    fun deleteLocalMovement(movement: MovementModel) {
        viewModelScope.launch {
            movementRepository.deleteMovement(movementMapper.mapToDomain(movement))
        }
    }

    fun insertDiscardedMovement(id: Int) {
        viewModelScope.launch {
            movementRepository.discardMovement(id)
        }
    }
}
