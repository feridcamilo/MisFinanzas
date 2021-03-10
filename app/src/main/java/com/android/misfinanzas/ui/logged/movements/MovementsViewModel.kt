package com.android.misfinanzas.ui.logged.movements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.AppConfig.Companion.MAX_MOVEMENTS_SIZE
import com.android.domain.AppConfig.Companion.MIN_LENGTH_SEARCH
import com.android.domain.repository.MovementRepository
import com.android.domain.utils.DateUtils
import com.android.domain.utils.MoneyUtils
import com.android.misfinanzas.mappers.MovementMapper
import com.android.misfinanzas.models.MovementModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MovementsViewModel(
    private val movementRepository: MovementRepository,
    private val movementMapper: MovementMapper
) : ViewModel() {

    val viewState: LiveData<MovementsViewState> get() = _viewState
    private val _viewState = MutableLiveData<MovementsViewState>()

    private var movements: List<MovementModel> = emptyList()
    val descriptions: List<String> get() = movements.distinctBy { it.description }.map { it.description }

    fun getMovements() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = movementRepository.getMovementsDetailed()
            movements = data.map { movementMapper.map(it) }
            _viewState.postValue(MovementsViewState.MovementsLoaded(movements))
        }
    }

    fun filter(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (text.isNotEmpty()) {
                if (movements.size > MAX_MOVEMENTS_SIZE //if the list is big
                    && text.length <= MIN_LENGTH_SEARCH //and if text length has less than MIN_LENGTH_SEARCH
                ) {
                    return@launch  //it not going to search in the firsts MIN_LENGTH_SEARCH letters written
                }

                val textToCompare = text.toLowerCase(Locale.getDefault())
                val valueToCompare = MoneyUtils.getBigDecimalStringValue(text)

                val movementsFiltered = movements.filter {
                    it.description.toLowerCase(Locale.getDefault()).contains(textToCompare) ||
                            (valueToCompare.isNotEmpty() && it.value.toString().contains(valueToCompare)) ||
                            DateUtils.getDateFormat().format(it.date!!).toString().contains(textToCompare) ||
                            it.personName?.toLowerCase(Locale.getDefault())?.contains(textToCompare) == true ||
                            it.placeName?.toLowerCase(Locale.getDefault())?.contains(textToCompare) == true ||
                            it.categoryName?.toLowerCase(Locale.getDefault())?.contains(textToCompare) == true ||
                            it.debtName?.toLowerCase(Locale.getDefault())?.contains(textToCompare) == true
                }
                _viewState.postValue(MovementsViewState.MovementsFiltered(movementsFiltered))
            } else {
                _viewState.postValue(MovementsViewState.MovementsFiltered(movements))
            }
        }
    }

}
