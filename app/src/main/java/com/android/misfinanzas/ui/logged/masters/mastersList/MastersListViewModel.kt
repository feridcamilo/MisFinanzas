package com.android.misfinanzas.ui.logged.masters.mastersList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.MasterRepository
import com.android.misfinanzas.mappers.MastersMapper
import kotlinx.coroutines.launch

class MastersListViewModel(
    private val masterRepository: MasterRepository,
    private val mastersMapper: MastersMapper
) : ViewModel() {

    val viewState: LiveData<MastersListViewState> get() = _viewState
    private val _viewState = MutableLiveData<MastersListViewState>()

    fun getPeople() {
        viewModelScope.launch {
            val data = masterRepository.getPeople()
            val dataMapped = data.map { mastersMapper.map(it) }
            _viewState.postValue(MastersListViewState.PeopleLoaded(dataMapped))
        }
    }

    fun getPlaces() {
        viewModelScope.launch {
            val data = masterRepository.getPlaces()
            val dataMapped = data.map { mastersMapper.map(it) }
            _viewState.postValue(MastersListViewState.PlacesLoaded(dataMapped))
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            val data = masterRepository.getCategories()
            val dataMapped = data.map { mastersMapper.map(it) }
            _viewState.postValue(MastersListViewState.CategoriesLoaded(dataMapped))
        }
    }

    fun getDebts() {
        viewModelScope.launch {
            val data = masterRepository.getDebts()
            val dataMapped = data.map { mastersMapper.map(it) }
            _viewState.postValue(MastersListViewState.DebtsLoaded(dataMapped))
        }
    }

}
