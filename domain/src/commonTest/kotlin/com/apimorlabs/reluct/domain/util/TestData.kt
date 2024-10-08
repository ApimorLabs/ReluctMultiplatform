package com.apimorlabs.reluct.domain.util

import com.apimorlabs.reluct.common.models.domain.tasks.EditTask
import com.apimorlabs.reluct.data.source.database.models.TaskDbObject
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

internal object TestData {
    val taskDbObjects = listOf(
        TaskDbObject(
            id = "1L",
            title = "Task 1",
            description = null,
            done = false,
            overdue = false,
            dueDateLocalDateTime = forwardTimeBy(months = 2),
            completedLocalDateTime = null,
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        ),
        TaskDbObject(
            id = "2L",
            title = "Task 2",
            description = null,
            done = false,
            overdue = false,
            dueDateLocalDateTime = forwardTimeBy(months = 3, days = 5),
            completedLocalDateTime = null,
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        ),
        TaskDbObject(
            id = "3L",
            title = "Task 3",
            description = "Some description",
            done = false,
            overdue = false,
            dueDateLocalDateTime = forwardTimeBy(months = 4, days = 2),
            completedLocalDateTime = null,
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        ),
        TaskDbObject(
            id = "4L",
            title = "Task 4",
            description = "Desc4",
            done = true,
            overdue = false,
            dueDateLocalDateTime = forwardTimeBy(days = -2),
            completedLocalDateTime = forwardTimeBy(days = -2),
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        ),
        TaskDbObject(
            id = "5L",
            title = "Task 5",
            description = "Desc5",
            done = true,
            overdue = false,
            dueDateLocalDateTime = forwardTimeBy(months = -1, days = -2),
            completedLocalDateTime = forwardTimeBy(months = -1, days = -3),
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        )
    )

    val editTask = EditTask(
        id = "2L",
        title = "Task 2",
        description = null,
        done = false,
        overdue = false,
        dueDateLocalDateTime = forwardTimeBy(months = 3, days = 5),
        completedLocalDateTime = null,
        reminderLocalDateTime = null,
        timeZoneId = TimeZone.currentSystemDefault().id
    )

    private fun forwardTimeBy(years: Int = 0, months: Int = 0, days: Int = 0): String {
        val dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val date = LocalDate(dateTime.year, dateTime.monthNumber, dateTime.dayOfMonth)
        val datePeriod = DatePeriod(years, months, days)
        val newDate = date.plus(datePeriod)
        return LocalDateTime(
            newDate.year,
            newDate.monthNumber,
            newDate.dayOfMonth,
            dateTime.hour,
            dateTime.minute
        )
            .toString()
    }
}