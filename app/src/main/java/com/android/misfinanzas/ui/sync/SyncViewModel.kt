package com.android.misfinanzas.ui.sync

import androidx.lifecycle.*
import com.android.data.local.model.BalanceVO
import com.android.data.local.model.MovementVO
import com.android.data.local.model.UserVO
import com.android.data.local.repository.ILocalRepository
import com.android.data.local.repository.UserSesion
import com.android.data.remote.model.Balance
import com.android.data.remote.model.Movement
import com.android.data.remote.model.User
import com.android.data.remote.repository.IWebRepository
import com.android.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {

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
        val userToInsert = UserVO(user.Usuario, user.IdCliente, user.Nombres, user.Apellidos, user.Correo)

        viewModelScope.launch {
            localRepo.insertUser(userToInsert)
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

    fun syncBalance() = liveData {
        emit(Result.Loading)
        try {
            val balance = webRepo.getBalance(clientId)
            if (balance != null) {
                insertLocalBalance(balance)
            }
            emit(Result.Success(balance))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    private fun insertLocalBalance(balance: Balance) {
        val balanceToInsert = BalanceVO(
            0,
            balance.IngresosEfectivo,
            balance.EgresosEfectivo,
            balance.IngresosElectronico,
            balance.EgresosElectronico,
            balance.Retiros,
            balance.CompraTC,
            balance.TengoEfectivo,
            balance.TengoElectronico,
            balance.TengoTotal,
            balance.DiferenciaIngresos,
            balance.DiferenciaEgresos,
            balance.TotalIngresos,
            balance.TotalEgresos
        )

        viewModelScope.launch {
            localRepo.insertBalance(balanceToInsert)
        }
    }

    fun syncMovements() = liveData {
        emit(Result.Loading)
        try {
            val movements = webRepo.getMovements(clientId)
            if (movements.isNotEmpty()) {
                insertLocalMovement(movements)
            }
            emit(Result.Success(movements))
        } catch (e: Exception) {
            emit(Result.Error(e))
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
}
