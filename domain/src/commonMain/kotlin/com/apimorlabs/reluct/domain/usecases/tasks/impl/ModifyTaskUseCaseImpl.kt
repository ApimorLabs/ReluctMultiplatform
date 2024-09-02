package com.apimorlabs.reluct.domain.usecases.tasks.impl

import com.apimorlabs.reluct.common.models.domain.tasks.EditTask
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.data.source.database.dao.tasks.TasksDao
import com.apimorlabs.reluct.data.source.database.models.TaskDbObject
import com.apimorlabs.reluct.domain.mappers.asDatabaseModel
import com.apimorlabs.reluct.domain.mappers.asEditTask
import com.apimorlabs.reluct.domain.usecases.tasks.ManageTasksAlarms
import com.apimorlabs.reluct.domain.usecases.tasks.ModifyTaskUseCase
import com.apimorlabs.reluct.system.services.haptics.HapticFeedback
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import kotlinx.datetime.toLocalDateTime

internal class ModifyTaskUseCaseImpl(
    private val dao: TasksDao,
    private val haptics: HapticFeedback,
    private val manageTasksAlarms: ManageTasksAlarms,
    private val backgroundDispatcher: CoroutineDispatcher,
) : ModifyTaskUseCase {

    override fun getTaskToEdit(taskId: String): Flow<EditTask?> {
        haptics.doubleClick()
        return dao.getTask(taskId)
            .map { value: TaskDbObject? ->
                value?.asEditTask()
            }.flowOn(backgroundDispatcher)
            .take(1)
    }

    override suspend fun saveTask(task: EditTask) {
        withContext(backgroundDispatcher) {
            dao.insertTask(task.asDatabaseModel())
            haptics.click()
            task.reminderLocalDateTime?.let { dateTimeString ->
                manageTasksAlarms
                    .setAlarm(taskId = task.id, dateTime = dateTimeString.toLocalDateTime())
            }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        withContext(backgroundDispatcher) {
            dao.deleteTask(taskId)
            haptics.heavyClick()
            manageTasksAlarms.removeAlarm(taskId)
        }
    }

    override suspend fun toggleTaskDone(task: Task, isDone: Boolean) {
        withContext(backgroundDispatcher) {
            val completedLocalDateTime = if (isDone) TimeUtils.currentLocalDateTime() else null
            dao.toggleTaskDone(task.id, isDone, task.overdue, completedLocalDateTime)
            haptics.tick()
            if (isDone) {
                manageTasksAlarms.removeAlarm(task.id)
            } else {
                task.reminderDateAndTime?.let { dateTimeString ->
                    manageTasksAlarms
                        .setAlarm(taskId = task.id, dateTime = dateTimeString.toLocalDateTime())
                }
            }
        }
    }
}