package com.example.data

import kotlinx.coroutines.flow.Flow

class ConcreteBatchRepository(private val dao: ConcreteBatchDao) {
    val allBatches: Flow<List<ConcreteBatch>> = dao.getAllBatches()

    suspend fun insert(batch: ConcreteBatch): Long = dao.insertBatch(batch)

    suspend fun delete(batch: ConcreteBatch) = dao.deleteBatch(batch)

    suspend fun deleteById(id: Long) = dao.deleteBatchById(id)
}
