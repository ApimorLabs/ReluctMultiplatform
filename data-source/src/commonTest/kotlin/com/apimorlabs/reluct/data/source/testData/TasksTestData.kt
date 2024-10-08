package com.apimorlabs.reluct.data.source.testData

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import com.apimorlabs.reluct.data.source.database.models.TaskDbObject
import com.apimorlabs.reluct.data.source.database.models.TaskLabelDbObject

internal object TasksTestData {
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

    val taskLabels = listOf(
        TaskLabelDbObject(
            id = "label_1",
            name = "Label 1",
            description = "Description 1",
            colorHexString = "000000"
        ),
        TaskLabelDbObject(
            id = "label_2",
            name = "Label 2",
            description = "Description 2",
            colorHexString = "000000"
        ),
        TaskLabelDbObject(
            id = "label_3",
            name = "Label 3",
            description = "Description 3",
            colorHexString = "000000"
        ),
        TaskLabelDbObject(
            id = "label_4",
            name = "Label 4",
            description = "Description 4",
            colorHexString = "000000"
        )
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