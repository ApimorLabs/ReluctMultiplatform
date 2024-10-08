package com.apimorlabs.reluct.features.tasks.states.events

sealed class TasksEvents {
    data object Nothing : TasksEvents()
    data class ShowMessageDone(val isDone: Boolean, val msg: String) : TasksEvents()
    data class ShowMessage(val msg: String) : TasksEvents()
    data class DisplayErrorMsg(
        val msg: String,
    ) : TasksEvents()

    sealed class Navigation : TasksEvents() {
        data class NavigateToTaskDetails(
            val taskId: String,
        ) : Navigation()

        data class NavigateToEdit(
            val taskId: String,
        ) : Navigation()

        data object GoBack : Navigation()
    }
}
