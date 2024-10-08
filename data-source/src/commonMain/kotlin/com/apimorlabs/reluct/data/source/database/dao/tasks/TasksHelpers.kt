package com.apimorlabs.reluct.data.source.database.dao.tasks

import com.apimorlabs.reluct.data.source.database.models.TaskDbObject
import com.apimorlabs.reluct.data.source.database.models.TaskLabelDbObject
import com.apimorlabs.reluct.data.source.database.tables.TaskLabelsTable
import com.apimorlabs.reluct.data.source.database.tables.TaskLabelsTableQueries
import com.apimorlabs.reluct.data.source.database.tables.TasksTable
import com.apimorlabs.reluct.data.source.database.tables.TasksTableQueries

internal object TasksHelpers {

    fun TasksTableQueries.replaceTasksInDb(tasks: List<TaskDbObject>) = transactionWithResult {
        deleteAll()
        for (i in tasks.indices) {
            val task = tasks[i]
            insertTask(
                TasksTable(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    done = task.done,
                    overdue = task.overdue,
                    taskLabelsId = task.taskLabels.map { it.id },
                    dueDateLocalDateTime = task.dueDateLocalDateTime,
                    completedLocalDateTime = task.completedLocalDateTime,
                    reminderLocalDateTime = task.reminderLocalDateTime,
                    timeZoneId = task.timeZoneId
                )
            )
        }
        true
    }

    fun TasksTableQueries.insertTaskToDb(task: TaskDbObject) {
        transaction {
            insertTask(
                TasksTable(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    done = task.done,
                    overdue = task.overdue,
                    taskLabelsId = task.taskLabels.map { it.id },
                    dueDateLocalDateTime = task.dueDateLocalDateTime,
                    completedLocalDateTime = task.completedLocalDateTime,
                    reminderLocalDateTime = task.reminderLocalDateTime,
                    timeZoneId = task.timeZoneId
                )
            )
        }
    }

    /**
     * Task Labels
     */
    fun TaskLabelsTableQueries.insertLabelToDb(label: TaskLabelDbObject) {
        transaction {
            insertLabel(
                TaskLabelsTable(
                    id = label.id,
                    name = label.name,
                    description = label.description,
                    colorHex = label.colorHexString
                )
            )
        }
    }

    fun TaskLabelsTableQueries.insertAllLabelsToDb(labels: List<TaskLabelDbObject>) {
        transaction {
            labels.forEach { label ->
                insertLabel(
                    TaskLabelsTable(
                        id = label.id,
                        name = label.name,
                        description = label.description,
                        colorHex = label.colorHexString
                    )
                )
            }
        }
    }

    val taskLabelsMapper: (
        id: String,
        name: String,
        description: String,
        color: String
    ) -> TaskLabelDbObject = { id, name, description, color ->
        TaskLabelDbObject(id = id, name = name, description = description, colorHexString = color)
    }

    fun TasksTable.asDbObject(allLabels: List<TaskLabelDbObject>) = TaskDbObject(
        id = id,
        title = title,
        description = description,
        done = done,
        overdue = overdue,
        taskLabels = taskLabelsId.mapNotNull { labelId ->
            allLabels.firstOrNull { labelId == it.id }
        },
        dueDateLocalDateTime = dueDateLocalDateTime,
        completedLocalDateTime = completedLocalDateTime,
        reminderLocalDateTime = reminderLocalDateTime,
        timeZoneId = timeZoneId
    )

    /**
     * Not In Use Right Now
     private fun taskDbObjectMapper(onGetLabel: (id: String) -> TaskLabelDbObject?): (
     id: String, title: String, description: String?, done: Boolean, overdue: Boolean,
     dueDateLocalDateTime: String, completedLocalDateTime: String?,
     reminderLocalDateTime: String?, timeZoneId: String, taskLabelsId: List<String>
     ) -> TaskDbObject = {
     id, title, description, done, overdue, dueDateLocalDateTime,
     completedLocalDateTime, reminderLocalDateTime, timeZoneId, taskLabelsId,
     ->
     TaskDbObject(
     id = id,
     title = title,
     description = description,
     done = done,
     overdue = overdue,
     taskLabels = taskLabelsId.mapNotNull { onGetLabel(it) },
     dueDateLocalDateTime = dueDateLocalDateTime,
     completedLocalDateTime = completedLocalDateTime,
     reminderLocalDateTime = reminderLocalDateTime,
     timeZoneId = timeZoneId
     )
     }*/
}
