package com.todoapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert // Inserts to database, or updates if there is already a matching ID in the database
    fun upsertTask(task: Task)

    @Delete //Deletes task
    fun deleteTask(task: Task)

    //Temporary for testing, actual get function will be ordered by date&time
    @Query("SELECT * FROM task ORDER BY header ASC")
    fun getTasksOrderedByHeader(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY time ASC, date ASC")
    fun getTasksOrderedByTimeAndDate(): Flow<List<Task>>
}