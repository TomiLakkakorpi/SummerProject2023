package com.example.databasetest.repository

import androidx.lifecycle.LiveData
import com.example.databasetest.data.TaskDao
import com.example.databasetest.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    //val readAllData: LiveData<List<Task>> = taskDao.readAllData()

    fun addTask(task: Task){
        taskDao.addTask(task)
    }

    fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }

    fun getTasksFiltered(filter: String): LiveData<List<Task>> {
        return taskDao.readAllData(filter)
    }
}