package com.example.databasetest.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.databasetest.model.Task

@Dao
interface TaskDao {

    //Function to insert task into database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTask(task: Task)

    //Function to update task
    @Update
    fun updateTask(task: Task): Int

    //Function to delete single task
    @Delete
    fun deleteTask(task: Task): Int

    //Function to delete all tasks marked as completed
    @Query("DELETE FROM task_table WHERE status=1")
    fun deleteAllTasks(): Void

    //Function to order all tasks based on date and time
    @Query("SELECT * FROM task_table ORDER BY date, time ASC")
    fun readAllData(): LiveData<List<Task>>
}