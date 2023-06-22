package com.example.databasetest.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.databasetest.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task): Int

    @Delete
    fun deleteTask(task: Task): Int

    @Query("DELETE FROM task_table")
    fun deleteAllTasks(): Void

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Task>>
}