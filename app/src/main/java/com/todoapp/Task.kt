package com.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Task(
    val header: String,
    val time: String,
    val date: String,
    val details: String,
    val isTaskCompleted: Boolean,
    val category: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
