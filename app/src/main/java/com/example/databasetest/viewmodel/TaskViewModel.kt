package com.example.databasetest.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.databasetest.data.TaskDatabase
import com.example.databasetest.repository.TaskRepository
import com.example.databasetest.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Task>>
    private val filter = MutableLiveData<String>("%")
    private val repository: TaskRepository

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)

        readAllData = Transformations.switchMap(filter) { filter ->
            repository.getTasksFiltered(filter)
        }
        //readAllData = repository.readAllData
    }

    fun setFilter(newFilter: String) {
        val f = when {
            newFilter == "Näytä kaikki" -> "%"
            else -> newFilter
        }
        filter.postValue(f)
    }

    fun addTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }
}