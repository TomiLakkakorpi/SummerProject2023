package com.todoapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Task::class],
    version = 1
)
abstract class taskDatabase: RoomDatabase() {

    abstract val dao: TaskDao
}