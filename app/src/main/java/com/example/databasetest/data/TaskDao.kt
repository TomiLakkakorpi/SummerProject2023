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
    @Query("DELETE FROM task_table WHERE status=1") //AND notifyMinutes=0 AND notifyHour=0 AND notifyDay=0
    fun deleteAllTasks(): Void

    //Function to order all tasks based on date and time
    @Query("SELECT * FROM task_table WHERE category LIKE :filter ORDER BY date ASC, time ASC, importance DESC")
    fun readAllData(filter: String): LiveData<List<Task>>
}