package com.apimorlabs.reluct.domain.mappers

import com.apimorlabs.reluct.common.models.domain.tasks.EditTask
import com.apimorlabs.reluct.data.source.database.models.TaskDbObject
import kotlinx.collections.immutable.toImmutableList

/**
 *  Convert from EditTask to database model
 */
fun EditTask.asDatabaseModel(): TaskDbObject =
    TaskDbObject(
        id = this.id,
        title = this.title,
        description = this.description,
        done = this.done,
        overdue = this.overdue,
        taskLabels = taskLabels.map { it.asTaskLabelDbObject() },
        dueDateLocalDateTime = this.dueDateLocalDateTime,
        completedLocalDateTime = this.completedLocalDateTime,
        reminderLocalDateTime = this.reminderLocalDateTime,
        timeZoneId = this.timeZoneId
    )

// Convert a list of Task database objects to a domain model of Task

/**
 *  Convert a Task database object to a domain EditTask model
 */
fun TaskDbObject.asEditTask(): EditTask =
    EditTask(
        id = this.id,
        title = this.title,
        description = this.description,
        done = this.done,
        overdue = this.overdue,
        taskLabels = taskLabels.map { it.asTaskLabel() }.toImmutableList(),
        dueDateLocalDateTime = this.dueDateLocalDateTime,
        completedLocalDateTime = this.completedLocalDateTime,
        reminderLocalDateTime = this.reminderLocalDateTime,
        timeZoneId = this.timeZoneId
    )
