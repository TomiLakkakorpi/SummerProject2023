package com.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Task(
    val header: String,
    val time: String,
    val date: String,
    val details: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
