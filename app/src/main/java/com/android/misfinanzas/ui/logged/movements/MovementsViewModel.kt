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
import com.android.misfinanzas.models.MasterModel
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

    var people: List<MasterModel> = emptyList()
    val peopleActive: List<MasterModel> get() = people.filter { it.enabled }

    var places: List<MasterModel> = emptyList()
    val placesActive: List<MasterModel> get() = people.filter { it.enabled }

    var categories: List<MasterModel> = emptyList()
    val categoriesActive: List<MasterModel> get() = categories.filter { it.enabled }

    var debts: List<MasterModel> = emptyList()
    val debtsActive: List<MasterModel> get() = debts.filter { it.enabled }

    fun getLocalMovements() {
        viewModelScope.launch {
            val data = movementRepository.getMovements()
            movements = data.map { movementMapper.map(it) }
            _viewState.postValue(MovementsViewState.MovementsLoaded(movements))
        }
    }

    fun filter(text: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (text.isNotEmpty()) {
                if (movements.size > MAX_MOVEMENTS_SIZE //if the list is big
                    && text.length <= MIN_LENGTH_SEARCH //and if text length has less than MIN_LENGTH_SEARCH
                ) {
                    return@launch  //it not going to search in the firsts MIN_LENGTH_SEARCH letters written
                }

                val textToCompare = text.toLowerCase(Locale.getDefault())
                val valueToCompare = MoneyUtils.getBigDecimalStringValue(text)
                val personToCompare = people.firstOrNull { it.name.toLowerCase(Locale.getDefault()).contains(textToCompare) }
                val placeToCompare = places.firstOrNull { it.name.toLowerCase(Locale.getDefault()).contains(textToCompare) }
                val categoryToCompare = categories.firstOrNull { it.name.toLowerCase(Locale.getDefault()).contains(textToCompare) }
                val debtToCompare = debts.firstOrNull { it.name.toLowerCase(Locale.getDefault()).contains(textToCompare) }

                val movementsFiltered = movements.filter {
                    it.description.toLowerCase(Locale.getDefault()).contains(textToCompare) ||
                            (valueToCompare.isNotEmpty() && it.value.toString().contains(valueToCompare)) ||
                            DateUtils.getDateFormat().format(it.date!!).toString().contains(textToCompare) ||
                            (personToCompare != null && it.personId == personToCompare.id) ||
                            (placeToCompare != null && it.placeId == placeToCompare.id) ||
                            (categoryToCompare != null && it.categoryId == categoryToCompare.id) ||
                            (debtToCompare != null && it.debtId == debtToCompare.id)
                }
                _viewState.postValue(MovementsViewState.MovementsFiltered(movementsFiltered))
            } else {
                _viewState.postValue(MovementsViewState.MovementsFiltered(movements))
            }
        }
    }

}
