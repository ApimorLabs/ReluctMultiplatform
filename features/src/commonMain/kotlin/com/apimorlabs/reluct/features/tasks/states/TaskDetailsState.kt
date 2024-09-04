package com.apimorlabs.reluct.features.tasks.states

import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TaskDetailsState(
    val taskState: TaskState = TaskState.Loading,
    val availableTaskLabels: ImmutableList<TaskLabel> = persistentListOf()
)

sealed class TaskState {
    data object Loading : TaskState()
    data object NotFound : TaskState()
    data object Deleted : TaskState()
    data class Data(val task: Task) : TaskState()
}
