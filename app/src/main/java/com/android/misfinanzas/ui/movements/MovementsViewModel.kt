package com.android.misfinanzas.ui.movements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.android.data.local.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository
import com.android.domain.result.Result

class MovementsViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {

    fun getLocalMovements() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(localRepo.getMovements()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getLocalPeople() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(localRepo.getPeople()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getLocalPlaces() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(localRepo.getPlaces()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getLocalCategories() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(localRepo.getCategories()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getLocalDebts() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(localRepo.getDebts()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
