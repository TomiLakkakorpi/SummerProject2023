package com.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(
    private val dao: TaskDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.HEADER)
    private val _tasks = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.HEADER -> dao.getTasksOrderedByHeader()
                SortType.TIMEANDDATE -> dao.getTasksOrderedByTimeAndDate()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TaskState())
    val state = combine(_state, _sortType, _tasks) { state, sortType, tasks ->
        state.copy(
            tasks = tasks,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())

    fun onEvent(event: TaskEvent) {
        when(event) {
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    dao.deleteTask(event.task)
                }
            }

            TaskEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingTask = false
                )}
            }

            TaskEvent.SaveTask -> {
                val header = state.value.header
                val time = state.value.time
                val date = state.value.date
                val details = state.value.details
                val category = state.value.category
                val isCompleted = state.value.isCompleted

                if(header.isBlank() || time.isBlank() || date.isBlank() || details.isBlank() || category.isBlank()) {
                    return
                }

                val task = Task(
                    header = header,
                    time = time,
                    date = date,
                    details = details,
                    category = category,
                    isTaskCompleted = isCompleted
                )
                viewModelScope.launch {
                    dao.upsertTask(task)
                }
                _state.update { it.copy(
                    isAddingTask = false,
                    header = "",
                    time = "",
                    date = "",
                    details = ""
                )}
            }

            is TaskEvent.SetDate -> {
                _state.update { it.copy(
                    date = event.date
                ) }
            }

            is TaskEvent.SetDetails -> {
                _state.update { it.copy(
                    details = event.details
                ) }
            }

            is TaskEvent.SetHeader -> {
                _state.update { it.copy(
                    header = event.header
                ) }
            }

            is TaskEvent.SetTime -> {
                _state.update { it.copy(
                    time = event.time
                ) }
            }

            is TaskEvent.SetCategory -> {
                _state.update { it.copy(
                    category = event.category
                )}
            }

            is TaskEvent.SetIsCompleted -> {
                _state.update { it.copy(
                    isCompleted = event.isCompleted
                )}
            }

            TaskEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingTask = true
                )}
            }

            is TaskEvent.SortTasks -> {
                _sortType.value = event.sortType
            }
        }
    }
}