package com.android.misfinanzas.ui.logged.masters.mastersList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.MasterRepository
import com.android.misfinanzas.mappers.MastersMapper
import com.android.misfinanzas.models.MasterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MastersListViewModel(
    private val masterRepository: MasterRepository,
    private val mastersMapper: MastersMapper
) : ViewModel() {

    val viewState: LiveData<MastersListViewState> get() = _viewState
    private val _viewState = MutableLiveData<MastersListViewState>()

    private var items: List<MasterModel> = emptyList()
    val descriptions: List<String> get() = items.distinctBy { it.name }.map { it.name }

    fun getPeople() {
        viewModelScope.launch {
            val data = masterRepository.getPeople()
            items = data.map { mastersMapper.map(it) }
            _viewState.postValue(MastersListViewState.PeopleLoaded(items))
        }
    }

    fun getPlaces() {
        viewModelScope.launch {
            val data = masterRepository.getPlaces()
            items = data.map { mastersMapper.map(it) }
            _viewState.postValue(MastersListViewState.PlacesLoaded(items))
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            val data = masterRepository.getCategories()
            items = data.map { mastersMapper.map(it) }
            _viewState.postValue(MastersListViewState.CategoriesLoaded(items))
        }
    }

    fun getDebts() {
        viewModelScope.launch {
            val data = masterRepository.getDebts()
            items = data.map { mastersMapper.map(it) }
            _viewState.postValue(MastersListViewState.DebtsLoaded(items))
        }
    }

    fun filter(text: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (text.isNotEmpty()) {
                val textToCompare = text.toLowerCase(Locale.ROOT)
                val mastersFiltered = items.filter {
                    it.name.toLowerCase(Locale.getDefault()).contains(textToCompare)
                }
                _viewState.postValue(MastersListViewState.MastersFiltered(mastersFiltered))
            } else {
                _viewState.postValue(MastersListViewState.MastersFiltered(items))
            }
        }
    }

}
