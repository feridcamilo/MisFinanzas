package com.android.misfinanzas.ui.logged.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.model.Master
import com.android.domain.model.Movement
import com.android.domain.repository.MasterRepository
import com.android.domain.repository.MovementRepository
import com.android.domain.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.*

class SyncViewModel(
    private val userRepository: UserRepository,
    private val movementRepository: MovementRepository,
    private val masterRepository: MasterRepository
) : ViewModel() {

    val viewState: LiveData<SyncViewState> get() = _viewState
    private val _viewState = MutableLiveData<SyncViewState>()

    fun getServerDateTime() {
        viewModelScope.launch {
            val serverDateTime = userRepository.getServerDateTime()
            _viewState.postValue(SyncViewState.ServerTimeLoaded(serverDateTime))
        }
    }

    fun syncAll(currentDate: Date) {
        viewModelScope.launch {
            syncMovements(false, currentDate)
            syncMasters(false, currentDate)
            _viewState.postValue(SyncViewState.AllSynced)
        }
    }

    suspend fun syncMovements(withObserve: Boolean, currentDate: Date) {
        val lastSync = movementRepository.getLastSyncMovements()

        if (lastSync != null) {
            val idsWebToDelete = movementRepository.getMovementsToDelete()
            if (sendDeletedMovements(idsWebToDelete)) {
                movementRepository.clearDeletedMovements()
            } else {
                //emit(Result.Error(Exception("Error al eliminar los movimientos en web")))
            }

            val idsLocalToDelete = movementRepository.getCloudDeletedMovements(lastSync)
            deleteLocalMovementsFromWeb(idsLocalToDelete)

            if (sendLocalMovements(lastSync)) {
                //Delete all news local movements to allow download them with the right movementId from web
                movementRepository.clearSyncedMovements(lastSync)
            } else {
                //emit(Result.Error(Exception("Error al enviar los movimientos a web")))
            }
        }

        val movements = movementRepository.getCloudMovements(lastSync)
        if (movements.isNotEmpty()) {
            insertLocalMovement(movements)
        }

        updateLastSyncMovements(currentDate)

        if (withObserve) {
            _viewState.postValue(SyncViewState.MovementsSynced)
        }
    }

    suspend fun getLastSyncMovements(): Date? {
        return movementRepository.getLastSyncMovements()
    }

    private suspend fun sendDeletedMovements(ids: List<Int>): Boolean {
        if (ids.isNotEmpty()) {
            return movementRepository.deleteMovementsInCloud(ids)
        }
        return true
    }

    private suspend fun sendLocalMovements(lastSync: Date): Boolean {
        val movements = movementRepository.getMovementsToSync(lastSync)
        if (movements.isNotEmpty()) {
            val movementsToSend = movements.map {
                Movement(
                    if (it.dateEntry!! > lastSync) 0 else it.idMovement, //if 0 is a new movement, else is a old mov updated
                    it.idType,
                    it.value,
                    it.description,
                    it.personId,
                    it.placeId,
                    it.categoryId,
                    it.date!!,
                    it.debtId,
                    it.dateEntry,
                    it.dateLastUpd
                )
            }
            return movementRepository.sendMovementsToCloud(movementsToSend)
        }

        return true
    }

    private suspend fun deleteLocalMovementsFromWeb(ids: List<Int>) {
        if (ids.isNotEmpty()) {
            movementRepository.deleteMovementsFromWeb(ids)
        }
    }

    private fun insertLocalMovement(movements: List<Movement>) {
        viewModelScope.launch {
            movementRepository.insertMovements(movements)
        }
    }

    private suspend fun updateLastSyncMovements(date: Date) {
        movementRepository.updateLastSyncMovements(date)
    }

    suspend fun syncMasters(withObserve: Boolean, currentDate: Date) {
        val dateLastSync = masterRepository.getLastSyncMasters()

        val categories = masterRepository.getCloudCategories(dateLastSync)
        if (categories.isNotEmpty()) {
            insertLocalCategories(categories)
        }

        val debts = masterRepository.getCloudDebts(dateLastSync)
        if (debts.isNotEmpty()) {
            insertLocalDebts(debts)
        }

        val places = masterRepository.getCloudPlaces(dateLastSync)
        if (places.isNotEmpty()) {
            insertLocalPlaces(places)
        }

        val people = masterRepository.getCloudPeople(dateLastSync)
        if (people.isNotEmpty()) {
            insertLocalPeople(people)
        }

        updateLastSyncMasters(currentDate)

        if (withObserve) {
            _viewState.postValue(SyncViewState.MastersSynced)
        }
    }

    suspend fun getLastSyncMasters(): Date? {
        return masterRepository.getLastSyncMasters()
    }

    private fun insertLocalCategories(categories: List<Master>) {
        viewModelScope.launch {
            masterRepository.insertCategories(categories)
        }
    }

    private fun insertLocalDebts(debts: List<Master>) {
        viewModelScope.launch {
            masterRepository.insertDebts(debts)
        }
    }

    private fun insertLocalPlaces(places: List<Master>) {
        viewModelScope.launch {
            masterRepository.insertPlaces(places)
        }
    }

    private fun insertLocalPeople(people: List<Master>) {
        viewModelScope.launch {
            masterRepository.insertPeople(people)
        }
    }

    private suspend fun updateLastSyncMasters(date: Date) {
        masterRepository.updateLastSyncMasters(date)
    }


    fun cleanDiscarded() {
        viewModelScope.launch {
            movementRepository.clearDiscardedMovements()
            _viewState.postValue(SyncViewState.DiscardedCleared)
        }
    }

}
