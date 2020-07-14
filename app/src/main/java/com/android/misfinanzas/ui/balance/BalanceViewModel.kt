package com.android.misfinanzas.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.android.data.local.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository
import com.android.domain.result.Result
import kotlinx.coroutines.Dispatchers

class BalanceViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {

    val getLocalUser = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            emit(localRepo.getUser())
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    val getLocalBalance = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            emit(localRepo.getBalance())
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
