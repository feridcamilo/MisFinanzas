package com.android.misfinanzas.ui.logged.movements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.MovementRepository
import com.android.misfinanzas.mappers.MovementMapper
import kotlinx.coroutines.launch

class MovementsViewModel(
    private val movementRepository: MovementRepository,
    private val movementMapper: MovementMapper
) : ViewModel() {

    val viewState: LiveData<MovementsViewState> get() = _viewState
    private val _viewState = MutableLiveData<MovementsViewState>()

    fun getLocalMovements() {
        viewModelScope.launch {
            val data = movementRepository.getMovements()
            val dataMapped = data.map { movementMapper.map(it) }
            _viewState.postValue(MovementsViewState.MovementsLoaded(dataMapped))
        }
    }

}
