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

    val getWebUser = credential.distinctUntilChanged().switchMap {
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                emit(webRepo.getUser(it.user, it.password))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }

    fun insertLocalUser(user: User) {
        val userToInsert = UserVO(user.Usuario, user.IdCliente, user.Nombres, user.Apellidos, user.Correo)

        viewModelScope.launch {
            localRepo.insertUser(userToInsert)
        }
    }

    val getWebBalance = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            emit(webRepo.getBalance(clientId))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun insertLocalBalance(balance: Balance) {
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

    val getWebMovements = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            emit(webRepo.getMovements(clientId))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun insertLocalMovement(movements: List<Movement>) {
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
