package com.android.misfinanzas.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.android.data.remote.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository
import com.android.domain.result.Result
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {

    val getWebUser = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            emit(webRepo.getUser("", ""))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}