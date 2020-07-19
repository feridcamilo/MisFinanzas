package com.android.data.local.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.android.data.local.model.BalanceVO
import com.android.data.local.model.MovementVO
import java.util.*

@Dao
interface MovementDAO {

    @Query("SELECT * FROM Movement ORDER BY date desc, dateEntry desc")
    suspend fun getAll(): List<MovementVO>

    @Query("SELECT * FROM Movement WHERE dateEntry > :lastSync OR dateLastUpd > :lastSync")
    suspend fun getAllToSync(lastSync: Date): List<MovementVO>

    @RawQuery(observedEntities = [BalanceVO::class])
    suspend fun getBalance(query: SupportSQLiteQuery): BalanceVO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movement: MovementVO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movements: List<MovementVO>)

    @Delete
    suspend fun delete(movement: MovementVO)

    @Query("DELETE FROM Movement WHERE dateEntry > :lastSync")
    suspend fun deleteAllNews(lastSync: Date)

    @Query("DELETE FROM Movement WHERE idMovement IN (:ids)")
    suspend fun deletedFromWeb(ids: List<Int>)
}
