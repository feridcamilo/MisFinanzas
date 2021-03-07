package com.android.data.local.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.android.data.local.model.BalanceVO
import com.android.data.local.model.MovementDetailedVO
import com.android.data.local.model.MovementVO
import java.util.*

@Dao
interface MovementDAO {

    @Query("SELECT * FROM Movement ORDER BY date desc, dateEntry desc")
    suspend fun getAll(): List<MovementVO>

    @Query(
        """
        SELECT 
            mov.*,
            per.name as personName,
            pla.name as placeName,
            cat.name as categoryName,
            debt.name as debtName
        FROM Movement as mov            	        
            LEFT JOIN Person as per on mov.personId = per.Id	        
            LEFT JOIN Place as pla on mov.placeId = pla.Id
            LEFT JOIN Category as cat on mov.categoryId = cat.Id
            LEFT JOIN Debt as debt on mov.debtId = debt.Id	        
        ORDER BY date desc, dateEntry desc
        """
    )
    suspend fun getAllDetailed(): List<MovementDetailedVO>

    @Query("SELECT * FROM Movement WHERE dateEntry > :lastSync OR dateLastUpd > :lastSync")
    suspend fun getAllToSync(lastSync: Date): List<MovementVO>

    @RawQuery(observedEntities = [MovementVO::class])
    suspend fun getBalance(query: SupportSQLiteQuery): BalanceVO

    @Query(
        """
        SELECT
            *,
            Totales.[TengoEfectivo] + Totales.[TengoElectronico] as [TengoTotal],
            Totales.[IngresosElectronico] - Totales.[IngresosEfectivo] as [DiferenciaIngresos],
            Totales.[EgresosElectronico] - Totales.[EgresosEfectivo] as [DiferenciaEgresos],
            Totales.[IngresosEfectivo] + Totales.[IngresosElectronico] as [TotalIngresos],
            Totales.[EgresosEfectivo] + Totales.[EgresosElectronico] [TotalEgresos]
        FROM (
            SELECT
                IEF.[IngresosEfectivo], EEF.[EgresosEfectivo], IEL.[IngresosElectronico], EEL.[EgresosElectronico], R.Retiros, CTC.[ComprasTC],
                (IEF.[IngresosEfectivo] - EEF.[EgresosEfectivo]) + R.Retiros as [TengoEfectivo],
                (IEL.[IngresosElectronico] - EEL.[EgresosElectronico]) - R.Retiros as [TengoElectronico]
            FROM (
                SELECT
                    COALESCE(SUM(value), 0) as [IngresosEfectivo]
                FROM Movement WHERE idType = 1
            ) AS IEF,
            (SELECT
                    COALESCE(SUM(value), 0) as [EgresosEfectivo]
                FROM Movement
                WHERE idType = 2
            ) AS EEF,
            (SELECT
                    COALESCE(SUM(value), 0) as [IngresosElectronico]
                FROM Movement
                WHERE idType = 3
            ) AS IEL,
            (SELECT
                    COALESCE(SUM(value), 0) as [EgresosElectronico]
                FROM Movement
                WHERE idType = 4
            ) AS EEL,
            (SELECT
                    COALESCE(SUM(value), 0) as [Retiros]
                FROM Movement
                WHERE idType = 5
            ) AS R,
            (SELECT
                    COALESCE(SUM(value), 0) as [ComprasTC]
                FROM Movement
                WHERE idType = 6
            ) AS CTC
        ) AS Totales
    """
    )
    suspend fun getBalance(): BalanceVO

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
