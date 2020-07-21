package com.android.misfinanzas.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.android.data.local.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository
import com.android.domain.result.Result

class BalanceViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {

    fun getLocalUser() = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(localRepo.getUser()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getLocalBalance(query: String) = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(localRepo.getBalance(query)))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
