package com.android.misfinanzas.ui.movements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.android.data.remote.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository
import com.android.domain.result.Result
import kotlinx.coroutines.Dispatchers

class MovementsViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {

    val getMovements = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            emit(webRepo.getMovements("1"))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
