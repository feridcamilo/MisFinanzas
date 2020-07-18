package com.android.data.local.repository

import com.android.data.local.RoomDataSource
import com.android.data.local.model.*
import java.util.*

class LocalRepositoryImp(private val dataSource: RoomDataSource) : ILocalRepository {

    override suspend fun getUser(): UserVO {
        return dataSource.getUser()
    }

    override suspend fun insertUser(user: UserVO) {
        dataSource.insertUser(user)
    }

    override suspend fun updateLastSync(date: Date) {
        dataSource.updateLastSync(date)
    }

    override suspend fun getLastSync(): Date? {
        return dataSource.getLastSync()
    }

    override suspend fun getBalance(): BalanceVO {
        return dataSource.getBalance()
    }

    override suspend fun insertBalance(balance: BalanceVO) {
        dataSource.insertBalance(balance)
    }

    override suspend fun getMovements(): List<MovementVO> {
        return dataSource.getMovements()
    }

    override suspend fun insertMovement(movement: MovementVO) {
        dataSource.insertMovement(movement)
    }

    override suspend fun insertMovements(movements: List<MovementVO>) {
        dataSource.insertMovements(movements)
    }

    override suspend fun getCategories(): List<CategoryVO> {
        return dataSource.getCategories()
    }

    override suspend fun insertCategories(categories: List<CategoryVO>) {
        dataSource.insertCategories(categories)
    }

    override suspend fun getDebts(): List<DebtVO> {
        return dataSource.getDebts()
    }

    override suspend fun insertDebts(debts: List<DebtVO>) {
        dataSource.insertDebts(debts)
    }

    override suspend fun getPlaces(): List<PlaceVO> {
        return dataSource.getPlaces()
    }

    override suspend fun insertPlaces(places: List<PlaceVO>) {
        dataSource.insertPlaces(places)
    }

    override suspend fun getPeople(): List<PersonVO> {
        return dataSource.getPeople()
    }

    override suspend fun insertPeople(people: List<PersonVO>) {
        dataSource.insertPeople(people)
    }
}
