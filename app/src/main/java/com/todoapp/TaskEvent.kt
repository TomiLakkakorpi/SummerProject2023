package com.todoapp

sealed interface TaskEvent {
    object SaveTask: TaskEvent
    data class SetHeader(val header: String): TaskEvent
    data class SetTime(val time: String): TaskEvent
    data class SetDate(val date: String): TaskEvent
    data class SetDetails(val details: String): TaskEvent
    object ShowDialog: TaskEvent
    object HideDialog: TaskEvent
    data class SortTasks(val sortType: SortType): TaskEvent
    data class DeleteTask(val task: Task): TaskEvent
}