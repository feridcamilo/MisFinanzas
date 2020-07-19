package com.android.data.local.repository

import com.android.data.local.model.*
import java.util.*

interface ILocalRepository {

    suspend fun getUser(): UserVO
    suspend fun insertUser(user: UserVO)
    suspend fun updateLastSync(date: Date)
    suspend fun getLastSync(): Date?

    suspend fun getBalance(query: String): BalanceVO
    suspend fun getMovements(): List<MovementVO>
    suspend fun getMovementsToSync(lastSync: Date): List<MovementVO>
    suspend fun getMovementsToDelete(): List<Int>
    suspend fun deleteMovementsFromWeb(ids: List<Int>)
    suspend fun deleteMovement(movement: MovementVO)
    suspend fun clearSyncedMovements(lastSync: Date)
    suspend fun insertMovement(movement: MovementVO)
    suspend fun insertMovements(movements: List<MovementVO>)

    suspend fun clearDeletedMovements()

    suspend fun getCategories(): List<CategoryVO>
    suspend fun insertCategories(categories: List<CategoryVO>)

    suspend fun getDebts(): List<DebtVO>
    suspend fun insertDebts(debts: List<DebtVO>)

    suspend fun getPlaces(): List<PlaceVO>
    suspend fun insertPlaces(places: List<PlaceVO>)

    suspend fun getPeople(): List<PersonVO>
    suspend fun insertPeople(people: List<PersonVO>)
}
