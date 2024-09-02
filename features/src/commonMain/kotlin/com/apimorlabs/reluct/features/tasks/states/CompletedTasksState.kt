package com.apimorlabs.reluct.features.tasks.states

import com.apimorlabs.reluct.common.models.domain.tasks.Task
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

sealed class CompletedTasksState(
    val tasksData: ImmutableMap<String, ImmutableList<Task>>,
    val shouldUpdateData: Boolean,
) {
    data class Data(
        private val tasks: ImmutableMap<String, ImmutableList<Task>> = persistentMapOf(),
        private val newDataPresent: Boolean = true,
    ) : CompletedTasksState(tasks, newDataPresent)

    class Loading(
        tasks: ImmutableMap<String, ImmutableList<Task>> = persistentMapOf(),
        newDataPresent: Boolean = true,
    ) : CompletedTasksState(tasks, newDataPresent)
}