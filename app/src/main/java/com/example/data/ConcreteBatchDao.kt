package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConcreteBatchDao {
    @Query("SELECT * FROM concrete_batches ORDER BY castingDateEpochDay DESC, id DESC")
    fun getAllBatches(): Flow<List<ConcreteBatch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(batch: ConcreteBatch): Long

    @Delete
    suspend fun deleteBatch(batch: ConcreteBatch)

    @Query("DELETE FROM concrete_batches WHERE id = :id")
    suspend fun deleteBatchById(id: Long)
}
