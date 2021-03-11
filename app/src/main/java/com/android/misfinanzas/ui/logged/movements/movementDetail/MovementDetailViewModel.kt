package com.android.misfinanzas.ui.logged.movements.movementDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.MovementRepository
import com.android.misfinanzas.mappers.MovementMapper
import com.android.misfinanzas.models.MasterModel
import com.android.misfinanzas.models.MovementModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovementDetailViewModel(
    private val movementRepository: MovementRepository,
    private val movementMapper: MovementMapper
) : ViewModel() {

    val viewState: LiveData<MovementDetailViewState> get() = _viewState
    private val _viewState = MutableLiveData<MovementDetailViewState>()

    var descriptions: List<String> = emptyList()

    var people: List<MasterModel> = emptyList()
    val peopleActive: List<MasterModel> get() = people.filter { it.enabled }

    var places: List<MasterModel> = emptyList()
    val placesActive: List<MasterModel> get() = places.filter { it.enabled }

    var categories: List<MasterModel> = emptyList()
    val categoriesActive: List<MasterModel> get() = categories.filter { it.enabled }

    var debts: List<MasterModel> = emptyList()
    val debtsActive: List<MasterModel> get() = debts.filter { it.enabled }

    fun getMovementsDescriptions() {
        viewModelScope.launch(Dispatchers.IO) {
            descriptions = movementRepository.getMovementsDescriptions()
            _viewState.postValue(MovementDetailViewState.DescriptionsLoaded)
        }
    }

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
