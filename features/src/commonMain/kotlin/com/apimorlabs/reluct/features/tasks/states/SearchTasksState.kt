package com.apimorlabs.reluct.features.tasks.states

import com.apimorlabs.reluct.common.models.domain.tasks.Task
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SearchTasksState(
    val searchData: SearchData = SearchData.Loading(),
    val searchQuery: String = "",
    val shouldUpdateData: Boolean = true,
)

sealed class SearchData(
    val tasksData: ImmutableList<Task>,
) {
    data class Data(
        private val tasks: ImmutableList<Task>,
    ) : SearchData(tasks)

    class Loading(
        tasks: ImmutableList<Task> = persistentListOf(),
    ) : SearchData(tasks)

    data object Empty : SearchData(tasksData = persistentListOf())
}
