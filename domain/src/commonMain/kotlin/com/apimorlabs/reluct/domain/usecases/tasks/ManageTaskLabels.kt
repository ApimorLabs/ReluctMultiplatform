package com.apimorlabs.reluct.domain.usecases.tasks

import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface ManageTaskLabels {
    suspend fun addLabel(label: TaskLabel)
    suspend fun addLabels(labels: ImmutableList<TaskLabel>)

    fun allLabels(): Flow<ImmutableList<TaskLabel>>
    fun getLabel(id: String): Flow<TaskLabel?>

    suspend fun deleteLabel(id: String)
    suspend fun deleteAllLabels()
}
