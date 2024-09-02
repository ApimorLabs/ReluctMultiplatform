package com.apimorlabs.reluct.features.tasks.states

import com.apimorlabs.reluct.common.models.domain.tasks.EditTask
import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed class AddEditTasksState {
    data class Data(
        val task: EditTask? = null,
    ) : AddEditTasksState()

    data object TaskSaved : AddEditTasksState()

    data object Loading : AddEditTasksState()
}

data class AddEditTaskState(
    val modifyTaskState: ModifyTaskState = ModifyTaskState.Loading,
    val availableTaskLabels: ImmutableList<TaskLabel> = persistentListOf()
)

sealed class ModifyTaskState {
    data class Data(
        val task: EditTask,
        val isEdit: Boolean
    ) : ModifyTaskState()

    data object Saved : ModifyTaskState()

    data object Loading : ModifyTaskState()

    data object NotFound : ModifyTaskState()
}