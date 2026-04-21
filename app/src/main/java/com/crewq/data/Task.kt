package com.crewq.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long,
    val customer: String,
    val timeWindow: String,
    val jobDescription: String
)
