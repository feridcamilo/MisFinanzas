package com.android.data.repository.master.datasource

import com.android.data.local.db.AppDatabase
import com.android.data.local.model.CategoryVO
import com.android.data.local.model.DebtVO
import com.android.data.local.model.PersonVO
import com.android.data.local.model.PlaceVO
import com.android.domain.UserSesion
import java.util.*

class MasterRoomDataSource(private val db: AppDatabase) {

    suspend fun updateLastSyncMasters(date: Date) {
        UserSesion.updateLastSyncMasters(date)
        return db.userDao().updateLastSyncMasters(date)
    }

    suspend fun getLastSyncMasters(): Date? {
        return db.userDao().getLastSyncMasters()
    }

    suspend fun getCategories(): List<CategoryVO> {
        return db.categoryDAO().getAll()
    }

    suspend fun insertCategories(categories: List<CategoryVO>) {
        db.categoryDAO().insertAll(categories)
    }

    suspend fun getDebts(): List<DebtVO> {
        return db.debtDAO().getAll()
    }

    suspend fun insertDebts(debts: List<DebtVO>) {
        db.debtDAO().insertAll(debts)
    }

    suspend fun getPlaces(): List<PlaceVO> {
        return db.placeDAO().getAll()
    }

    suspend fun insertPlaces(places: List<PlaceVO>) {
        db.placeDAO().insertAll(places)
    }

    suspend fun getPeople(): List<PersonVO> {
        return db.personDAO().getAll()
    }

    suspend fun insertPeople(people: List<PersonVO>) {
        db.personDAO().insertAll(people)
    }

}
