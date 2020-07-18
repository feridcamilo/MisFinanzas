package com.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.data.local.model.DeletedMovementVO

@Dao
interface DeletedMovementDAO {

    @Query("SELECT idMovement FROM DeletedMovement")
    suspend fun getAll(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deletedMovement: DeletedMovementVO)

    @Query("DELETE FROM DeletedMovement")
    suspend fun cleanTable()
}
