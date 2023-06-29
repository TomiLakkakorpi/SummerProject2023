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

    // Queries for specific category
    @Query("SELECT * FROM task_table WHERE category LIKE 'Liikunta' ORDER BY date, time ASC")
    fun readCategory1Data(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE category LIKE 'Hyvinvointi' ORDER BY date, time ASC")
    fun readCategory2Data(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE category LIKE 'Terveys' ORDER BY date, time ASC")
    fun readCategory3Data(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE category LIKE 'Koulu' ORDER BY date, time ASC")
    fun readCategory4Data(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE category LIKE 'Työ' ORDER BY date, time ASC")
    fun readCategory5Data(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE category LIKE 'Harrastus' ORDER BY date, time ASC")
    fun readCategory6Data(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE category LIKE 'Askare' ORDER BY date, time ASC")
    fun readCategory7Data(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE category LIKE 'Tapaaminen' ORDER BY date, time ASC")
    fun readCategory8Data(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE category LIKE 'Muu' ORDER BY date, time ASC")
    fun readCategory9Data(): LiveData<List<Task>>

    //@Query("SELECT * FROM task_table WHERE category LIKE 'Liikunta' ORDER BY date, time ASC")
    //fun readCategory1Data(): LiveData<List<Task>>
}