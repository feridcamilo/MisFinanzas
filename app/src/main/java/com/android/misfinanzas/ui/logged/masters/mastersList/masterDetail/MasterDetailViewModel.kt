package com.android.misfinanzas.ui.logged.masters.mastersList.masterDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.MasterRepository
import com.android.misfinanzas.mappers.MastersMapper
import com.android.misfinanzas.models.MasterModel
import com.android.misfinanzas.sync.SyncManager
import com.android.misfinanzas.sync.SyncManager.SyncType.SYNC_MASTERS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MasterDetailViewModel(
    private val masterRepository: MasterRepository,
    private val mastersMapper: MastersMapper,
    private val syncManager: SyncManager
) : ViewModel() {

    val viewState: LiveData<MasterDetailViewState> get() = _viewState
    private val _viewState = MutableLiveData<MasterDetailViewState>()


    fun saveMaster(master: MasterModel, type: Int) {
        viewModelScope.launch {
            val result = masterRepository.saveMasterOnCloud(mastersMapper.mapToDomain(master), type)
            if (result) {
                _viewState.postValue(MasterDetailViewState.MasterSaved)
            } else {
                _viewState.postValue(MasterDetailViewState.SaveFailed)
            }
        }
    }


    fun sync() {
        viewModelScope.launch(Dispatchers.IO) {
            syncManager.sync(SYNC_MASTERS)
            _viewState.postValue(MasterDetailViewState.SynchronizedData)
        }
    }

}
