package com.apimorlabs.reluct.features.tasks.states

import com.apimorlabs.reluct.common.models.domain.tasks.Task
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

sealed class PendingTasksState(
    val tasksData: ImmutableMap<String, ImmutableList<Task>>,
    val overdueTasksData: ImmutableList<Task>,
    val shouldUpdateData: Boolean,
) {
    data class Data(
        val tasks: ImmutableMap<String, ImmutableList<Task>> = persistentMapOf(),
        val overdueTasks: ImmutableList<Task> = persistentListOf(),
        val newDataPresent: Boolean = true,
    ) : PendingTasksState(tasks, overdueTasks, newDataPresent)

    data class Loading(
        val tasks: ImmutableMap<String, ImmutableList<Task>> = persistentMapOf(),
        val overdueTasks: ImmutableList<Task> = persistentListOf(),
        val newDataPresent: Boolean = true,
    ) : PendingTasksState(tasks, overdueTasks, newDataPresent)
}
