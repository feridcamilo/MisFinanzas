package com.android.misfinanzas.ui.sync

import androidx.lifecycle.*
import com.android.data.UserSesion
import com.android.data.local.model.*
import com.android.data.remote.model.Master
import com.android.data.remote.model.Movement
import com.android.data.remote.model.User
import com.android.domain.repository.ILocalRepository
import com.android.domain.repository.IWebRepository
import com.android.domain.result.Result
import com.android.domain.utils.StringUtils.Companion.EMPTY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SyncViewModel(
    private val webRepo: IWebRepository,
    private val localRepo: ILocalRepository
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
                    val user = webRepo.getUser(it.user, it.password)
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
        val userToInsert = UserVO(user.Usuario, user.IdCliente, user.Nombres, user.Apellidos, user.Correo, null, null)

        viewModelScope.launch {
            localRepo.insertUser(userToInsert)
        }
    }

    fun updateLastSyncMovements(date: Date) {
        viewModelScope.launch {
            localRepo.updateLastSyncMovements(date)
        }
    }

    fun updateLastSyncMasters(date: Date) {
        viewModelScope.launch {
            localRepo.updateLastSyncMasters(date)
        }
    }

    /* TODO consultar por qué al declarar como val solo se dispara la primera vez, y cuando es fun se dispara siempre que se llama
    val getWebBalance = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            emit(Result.Success(webRepo.getBalance(clientId)))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    */

    fun syncMovements() = liveData {
        emit(Result.Loading)
        try {
            val lastSync = localRepo.getLastSyncMovements()

            if (lastSync != null) {
                val idsWebToDelete = localRepo.getMovementsToDelete()
                if (sendDeletedMovements(idsWebToDelete)) {
                    localRepo.clearDeletedMovements()
                } else {
                    emit(Result.Error(Exception("Error al eliminar los movimientos en web")))
                }

                val idsLocalToDelete = webRepo.getDeletedMovements(clientId, lastSync)
                deleteLocalMovementsFromWeb(idsLocalToDelete)

                if (sendLocalMovements(lastSync)) {
                    //Delete all news local movements to allow download them with the right movementId from web
                    localRepo.clearSyncedMovements(lastSync)
                } else {
                    emit(Result.Error(Exception("Error al enviar los movimientos a web")))
                }
            }

            val movements = webRepo.getMovements(clientId, lastSync)
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
            return webRepo.deleteMovements(ids)
        }
        return true
    }

    private suspend fun sendLocalMovements(lastSync: Date): Boolean {
        val movements = localRepo.getMovementsToSync(lastSync)
        if (movements.isNotEmpty()) {
            val movementsToSend = movements.map {
                Movement(
                    if (it.dateEntry!! > lastSync) 0 else it.idMovement, //if 0 is a new movement, else is a old mov updated
                    it.value,
                    it.description,
                    it.date!!,
                    it.dateEntry,
                    it.dateLastUpd,
                    it.idType, EMPTY,
                    it.categoryId, EMPTY,
                    it.debtId, EMPTY,
                    it.personId, EMPTY,
                    it.placeId, EMPTY
                )
            }
            return webRepo.sendMovements(clientId, movementsToSend)
        }

        return true
    }

    private suspend fun deleteLocalMovementsFromWeb(ids: List<Int>) {
        if (ids != null && ids.isNotEmpty()) {
            localRepo.deleteMovementsFromWeb(ids)
        }
    }

    private fun insertLocalMovement(movements: List<Movement>) {
        val movementsToInsert = movements.map {
            MovementVO(
                it.IdMovimiento,
                it.IdTipoMovimiento,
                it.Valor,
                it.Descripcion,
                it.IdPersona,
                it.IdLugar,
                it.IdCategoria,
                it.FechaMovimiento,
                it.IdDeuda,
                it.FechaIngreso,
                it.FechaActualizacion
            )
        }

        viewModelScope.launch {
            localRepo.insertMovements(movementsToInsert)
        }
    }

    fun cleanDiscarded() {
        viewModelScope.launch {
            localRepo.clearDiscardedMovements()
        }
    }

    suspend fun getLastSyncMovements(): Date? {
        return localRepo.getLastSyncMovements()
    }

    suspend fun getLastSyncMasters(): Date? {
        return localRepo.getLastSyncMasters()
    }

    fun getServerDateTime() = liveData {
        emit(Result.Loading)
        try {
            val serverDateTime = webRepo.getServerDateTime()
            emit(Result.Success(serverDateTime))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun syncMasters() = liveData {
        emit(Result.Loading)
        try {
            val dateLastSync = localRepo.getLastSyncMasters()

            val categories = webRepo.getCategories(clientId, dateLastSync)
            if (categories.isNotEmpty()) {
                insertLocalCategories(categories)
            }

            val debts = webRepo.getDebts(clientId, dateLastSync)
            if (debts.isNotEmpty()) {
                insertLocalDebts(debts)
            }

            val places = webRepo.getPlaces(clientId, dateLastSync)
            if (places.isNotEmpty()) {
                insertLocalPlaces(places)
            }

            val people = webRepo.getPeople(clientId, dateLastSync)
            if (people.isNotEmpty()) {
                insertLocalPeople(people)
            }

            emit(Result.Success(true))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    private fun insertLocalCategories(categories: List<Master>) {
        val categoriesToInsert = categories.map {
            CategoryVO(it.Id, it.Nombre, it.Activo)
        }

        viewModelScope.launch {
            localRepo.insertCategories(categoriesToInsert)
        }
    }

    private fun insertLocalDebts(debts: List<Master>) {
        val debtsToInsert = debts.map {
            DebtVO(it.Id, it.Nombre, it.Activo)
        }

        viewModelScope.launch {
            localRepo.insertDebts(debtsToInsert)
        }
    }

    private fun insertLocalPlaces(places: List<Master>) {
        val placesToInsert = places.map {
            PlaceVO(it.Id, it.Nombre, it.Activo)
        }

        viewModelScope.launch {
            localRepo.insertPlaces(placesToInsert)
        }
    }

    private fun insertLocalPeople(people: List<Master>) {
        val peopleToInsert = people.map {
            PersonVO(it.Id, it.Nombre, it.Activo)
        }

        viewModelScope.launch {
            localRepo.insertPeople(peopleToInsert)
        }
    }
}
