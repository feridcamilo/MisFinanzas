package com.android.data.local

import android.content.Context
import com.android.data.local.db.AppDatabase
import com.android.data.local.model.*
import java.util.*

class RoomDataSource(private val context: Context) {
    suspend fun getUser(): UserVO {
        return AppDatabase.getDatabase(context).userDao().getUser()
    }

    suspend fun updateLastSync(date: Date) {
        return AppDatabase.getDatabase(context).userDao().updateLastSync(date)
    }

    suspend fun getLastSync(): Date? {
        return AppDatabase.getDatabase(context).userDao().getLastSync()
    }

    suspend fun insertUser(user: UserVO) {
        return AppDatabase.getDatabase(context).userDao().insert(user)
    }

    suspend fun getBalance(): BalanceVO {
        return AppDatabase.getDatabase(context).balanceDAO().getBalance()
    }

    suspend fun insertBalance(balance: BalanceVO) {
        return AppDatabase.getDatabase(context).balanceDAO().insert(balance)
    }

    suspend fun getMovements(): List<MovementVO> {
        return AppDatabase.getDatabase(context).movementDAO().getAll()
    }

    suspend fun insertMovements(movements: List<MovementVO>) {
        AppDatabase.getDatabase(context).movementDAO().insertAll(movements)
    }

    suspend fun getCategories(): List<CategoryVO> {
        return AppDatabase.getDatabase(context).categoryDAO().getAll()
    }

    suspend fun insertCategories(categories: List<CategoryVO>) {
        AppDatabase.getDatabase(context).categoryDAO().insertAll(categories)
    }

    suspend fun getDebts(): List<DebtVO> {
        return AppDatabase.getDatabase(context).debtDAO().getAll()
    }

    suspend fun insertDebts(debts: List<DebtVO>) {
        AppDatabase.getDatabase(context).debtDAO().insertAll(debts)
    }

    suspend fun getPlaces(): List<PlaceVO> {
        return AppDatabase.getDatabase(context).placeDAO().getAll()
    }

    suspend fun insertPlaces(places: List<PlaceVO>) {
        AppDatabase.getDatabase(context).placeDAO().insertAll(places)
    }

    suspend fun getPeople(): List<PersonVO> {
        return AppDatabase.getDatabase(context).personDAO().getAll()
    }

    suspend fun insertPeople(people: List<PersonVO>) {
        AppDatabase.getDatabase(context).personDAO().insertAll(people)
    }
}
