package com.apimorlabs.reluct.domain.usecases.tasks.impl

import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.data.source.database.dao.tasks.TasksDao
import com.apimorlabs.reluct.domain.mappers.asTask
import com.apimorlabs.reluct.domain.usecases.tasks.GetTasksUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class GetTasksUseCaseImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) : GetTasksUseCase {
    override fun getPendingTasks(factor: Long, limitBy: Long): Flow<ImmutableList<Task>> =
        dao.getPendingTasks(factor, limitBy)
            .map { list -> list.map { it.asTask() }.toImmutableList() }
            .flowOn(backgroundDispatcher)

    override fun getCompletedTasks(factor: Long, limitBy: Long): Flow<ImmutableList<Task>> =
        dao.getCompletedTasks(factor, limitBy)
            .map { list -> list.map { it.asTask() }.toImmutableList() }
            .flowOn(backgroundDispatcher)

    override fun getSearchedTasks(
        query: String,
        factor: Long,
        limitBy: Long
    ): Flow<ImmutableList<Task>> =
        dao.searchTasks(query, factor, limitBy)
            .map { list -> list.map { it.asTask() }.toImmutableList() }
            .flowOn(backgroundDispatcher)

    override fun getTask(taskId: String): Flow<Task?> =
        dao.getTask(taskId).map { taskDbObject ->
            taskDbObject?.asTask()
        }.flowOn(backgroundDispatcher)
}
