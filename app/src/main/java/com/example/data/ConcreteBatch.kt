package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "concrete_batches")
data class ConcreteBatch(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectName: String,
    val elementName: String,
    val castingDateEpochDay: Long,
    val concreteGrade: String = "C30",
    val cubeCount: Int = 6,
    val notes: String = "",
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)
