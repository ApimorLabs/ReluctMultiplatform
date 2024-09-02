package com.apimorlabs.reluct.domain.usecases.tasks

import com.apimorlabs.reluct.common.models.domain.tasks.EditTask
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import kotlinx.coroutines.flow.Flow

interface ModifyTaskUseCase {
    fun getTaskToEdit(taskId: String): Flow<EditTask?>
    suspend fun saveTask(task: EditTask)
    suspend fun deleteTask(taskId: String)
    suspend fun toggleTaskDone(task: Task, isDone: Boolean)
}
