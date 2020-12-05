package com.android.misfinanzas.ui.movements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.android.domain.repository.MasterRepository
import com.android.domain.repository.MovementRepository
import com.android.domain.result.Result

class MovementsViewModel(
    private val movementRepository: MovementRepository,
    private val masterRepository: MasterRepository
) : ViewModel() {

    fun getLocalMovements() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(movementRepository.getMovements()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

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
