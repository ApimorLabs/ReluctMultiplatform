package com.apimorlabs.reluct.domain.usecases.tasks.impl

import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import com.apimorlabs.reluct.data.source.database.dao.tasks.TasksDao
import com.apimorlabs.reluct.domain.mappers.asTaskLabel
import com.apimorlabs.reluct.domain.mappers.asTaskLabelDbObject
import com.apimorlabs.reluct.domain.usecases.tasks.ManageTaskLabels
import com.apimorlabs.reluct.system.services.haptics.HapticFeedback
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

internal class ManageTaskLabelsImpl(
    private val dao: TasksDao,
    private val haptics: HapticFeedback,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ManageTaskLabels {

    override suspend fun addLabel(label: TaskLabel) = withContext(dispatcher) {
        dao.addTaskLabel(label = label.asTaskLabelDbObject())
        haptics.click()
    }

    override suspend fun addLabels(labels: ImmutableList<TaskLabel>) = withContext(dispatcher) {
        dao.addAllTaskLabels(labels = labels.map { it.asTaskLabelDbObject() })
        haptics.click()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun allLabels(): Flow<ImmutableList<TaskLabel>> = dao.getAllTaskLabels()
        .mapLatest { list -> list.map { it.asTaskLabel() }.toImmutableList() }
        .flowOn(dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLabel(id: String): Flow<TaskLabel?> = dao.getTaskLabel(id)
        .mapLatest { item -> item?.asTaskLabel() }
        .flowOn(dispatcher)

    override suspend fun deleteLabel(id: String) = withContext(dispatcher) {
        dao.deleteTaskLabel(id)
        haptics.heavyClick()
    }

    override suspend fun deleteAllLabels() = withContext(dispatcher) {
        dao.deleteAllTaskLabels()
        haptics.heavyClick()
    }
}