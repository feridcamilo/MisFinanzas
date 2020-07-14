package com.android.misfinanzas.ui.movements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.android.data.local.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository
import com.android.domain.result.Result
import kotlinx.coroutines.Dispatchers

class MovementsViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {

    val getLocalMovements = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            emit(localRepo.getMovements())
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    /*
    //mutable data para buscador
    private val cliendId = MutableLiveData<String>()

    fun setClientId(cliendId: String) {
        this.cliendId.value = cliendId
    }

    val getWebMovements = cliendId.distinctUntilChanged().switchMap { clientId ->
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                emit(webRepo.getMovements(clientId))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }
    */
}
