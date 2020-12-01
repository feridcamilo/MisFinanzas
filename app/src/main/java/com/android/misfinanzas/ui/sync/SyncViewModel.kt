package com.android.misfinanzas.ui.sync

import androidx.lifecycle.*
import com.android.data.repository.master.mappers.CategoryDataMapper
import com.android.domain.UserSesion
import com.android.domain.model.Master
import com.android.domain.model.Movement
import com.android.domain.model.User
import com.android.domain.repository.MasterRepository
import com.android.domain.repository.MovementRepository
import com.android.domain.repository.UserRepository
import com.android.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SyncViewModel(
    private val userRepository: UserRepository,
    private val movementRepository: MovementRepository,
    private val masterRepository: MasterRepository
) : ViewModel() {

    private var clientId: String = UserSesion.getUser()?.clientId.toString()

    private val credential = MutableLiveData<UserCredential>()

    fun setCredential(credential: UserCredential) {
        this.credential.value = credential
    }

    fun setClientId(clientId: String) {
        this.clientId = clientId
    }

    val syncUser = credential.distinctUntilChanged().switchMap {
        liveData(Dispatchers.IO) {
            if (it.user.isNotEmpty() && it.password.isNotEmpty()) {
                emit(Result.Loading)
                try {
                    val user = userRepository.getCloudUser(it.user, it.password)
                    if (user != null) {
                        insertLocalUser(user)
                    }
                    emit(Result.Success(user))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
        }
    }

    private fun insertLocalUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    fun updateLastSyncMovements(date: Date) {
        viewModelScope.launch {
            movementRepository.updateLastSyncMovements(date)
        }
    }

    fun updateLastSyncMasters(date: Date) {
        viewModelScope.launch {
            masterRepository.updateLastSyncMasters(date)
        }
    }

    fun syncMovements() = liveData {
        emit(Result.Loading)
        try {
            val lastSync = movementRepository.getLastSyncMovements()

            if (lastSync != null) {
                val idsWebToDelete = movementRepository.getMovementsToDelete()
                if (sendDeletedMovements(idsWebToDelete)) {
                    movementRepository.clearDeletedMovements()
                } else {
                    emit(Result.Error(Exception("Error al eliminar los movimientos en web")))
                }

                val idsLocalToDelete = movementRepository.getCloudDeletedMovements(clientId, lastSync)
                deleteLocalMovementsFromWeb(idsLocalToDelete)

                if (sendLocalMovements(lastSync)) {
                    //Delete all news local movements to allow download them with the right movementId from web
                    movementRepository.clearSyncedMovements(lastSync)
                } else {
                    emit(Result.Error(Exception("Error al enviar los movimientos a web")))
                }
            }

            val movements = movementRepository.getCloudMovements(clientId, lastSync)
            if (movements.isNotEmpty()) {
                insertLocalMovement(movements)
            }

            emit(Result.Success(true))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
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
            return movementRepository.sendMovementsToCloud(clientId, movementsToSend)
        }

        return true
    }

    private suspend fun deleteLocalMovementsFromWeb(ids: List<Int>) {
        if (ids != null && ids.isNotEmpty()) {
            movementRepository.deleteMovementsFromWeb(ids)
        }
    }

    private fun insertLocalMovement(movements: List<Movement>) {
        viewModelScope.launch {
            movementRepository.insertMovements(movements)
        }
    }

    fun cleanDiscarded() {
        viewModelScope.launch {
            movementRepository.clearDiscardedMovements()
        }
    }

    suspend fun getLastSyncMovements(): Date? {
        return movementRepository.getLastSyncMovements()
    }

    suspend fun getLastSyncMasters(): Date? {
        return masterRepository.getLastSyncMasters()
    }

    fun getServerDateTime() = liveData {
        emit(Result.Loading)
        try {
            val serverDateTime = userRepository.getServerDateTime()
            emit(Result.Success(serverDateTime))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun syncMasters() = liveData {
        emit(Result.Loading)
        try {
            val dateLastSync = masterRepository.getLastSyncMasters()

            val categories = masterRepository.getCloudCategories(clientId, dateLastSync)
            if (categories.isNotEmpty()) {
                insertLocalCategories(categories)
            }

            val debts = masterRepository.getCloudDebts(clientId, dateLastSync)
            if (debts.isNotEmpty()) {
                insertLocalDebts(debts)
            }

            val places = masterRepository.getCloudPlaces(clientId, dateLastSync)
            if (places.isNotEmpty()) {
                insertLocalPlaces(places)
            }

            val people = masterRepository.getCloudPeople(clientId, dateLastSync)
            if (people.isNotEmpty()) {
                insertLocalPeople(people)
            }

            emit(Result.Success(true))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
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
}
