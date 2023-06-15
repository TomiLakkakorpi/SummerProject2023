package com.todoapp

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val header: String = "",
    val time: String = "",
    val date: String = "",
    val details: String = "",
    val category: String = "",
    val isCompleted: Boolean = false,
    val isAddingTask: Boolean = false,
    val sortType: SortType = SortType.HEADER
)
