package com.android.misfinanzas.ui.logged.masters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.android.domain.repository.MasterRepository
import com.android.domain.result.Result

class MastersViewModel(
    private val masterRepository: MasterRepository
) : ViewModel() {

    fun getLocalPeople() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(masterRepository.getPeople()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getLocalPlaces() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(masterRepository.getPlaces()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getLocalCategories() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(masterRepository.getCategories()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getLocalDebts() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(masterRepository.getDebts()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
