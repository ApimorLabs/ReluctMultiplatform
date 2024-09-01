package com.apimorlabs.reluct.domain.usecases.tasks

import com.apimorlabs.reluct.common.models.domain.tasks.Task
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

/**
 * Gets a Task or Tasks depending on the user's need
 * It should be used with respective ViewModels to perform actions
 * Implementations found in the impl folder
 */
interface GetTasksUseCase {
    /**
     * [factor] is by how much we should multiply [limitBy]
     * [limitBy] is for how much limit is applied in query. Default is 10
     * [limitBy] * [factor] produces required limit applied in query
     **/
    fun getPendingTasks(factor: Long, limitBy: Long = 10): Flow<ImmutableList<Task>>

    /**
     * [factor] is by how much we should multiply [limitBy]
     * [limitBy] is for how much limit is applied in query. Default is 10
     * [limitBy] * [factor] produces required limit applied in query
     * **/
    fun getCompletedTasks(factor: Long, limitBy: Long = 10): Flow<ImmutableList<Task>>

    /**
     * [factor] is by how much we should multiply [limitBy]
     * [limitBy] is for how much limit is applied in query. Default is 10
     * [limitBy] * [factor] produces required limit applied in query
     * **/
    fun getSearchedTasks(query: String, factor: Long, limitBy: Long = 10): Flow<ImmutableList<Task>>

    fun getTask(taskId: String): Flow<Task?>
}
