package com.apimorlabs.reluct.compose.ui.util

import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.common.models.domain.goals.GoalDuration
import com.apimorlabs.reluct.common.models.domain.goals.GoalInterval
import com.apimorlabs.reluct.common.models.domain.goals.GoalType
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.common.models.util.UUIDGen
import com.apimorlabs.reluct.common.models.util.time.Week
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

object PreviewData {
    val task1 = Task(
        id = "1L",
        title = "Task 1 Title Goes Here",
        description = """
            This is a long description. This is a long description. This is a long description. 
            This is a long description.
        """.trimIndent(),
        done = false,
        overdue = false,
        dueDate = "Thu, 2 Feb",
        dueTime = "15:30",
        timeLeftLabel = "In 3 hours",
        reminderFormatted = "",
        completedDateAndTime = "Time Here"
    )

    val task2 = Task(
        id = "2L",
        title = "Task 2 Title Goes Here",
        description = """
            This is a long description. This is a long description. This is a long description. 
            This is a long description.
        """.trimIndent(),
        done = true,
        overdue = false,
        dueDate = "Thu, 2 Feb",
        dueTime = "15:30",
        timeLeftLabel = "In 3 hours",
        reminderFormatted = "",
        completedDateAndTime = "Time Here"
    )

    val task3 = Task(
        id = "3L",
        title = "Task 3 Title Goes Here",
        description = """
            This is a long description. This is a long description. This is a long description. 
            This is a long description.
        """.trimIndent(),
        done = true,
        overdue = true,
        dueDate = "Thu, 2 Feb",
        dueTime = "15:30",
        timeLeftLabel = "In 3 hours",
        reminderFormatted = "",
        completedDateAndTime = "Time Here"
    )

    val task4 = Task(
        id = "4L",
        title = "Task 4 Title Goes Here",
        description = """
            This is a long description. This is a long description. This is a long description. 
            This is a long description.
        """.trimIndent(),
        done = false,
        overdue = true,
        dueDate = "Thu, 2 Feb",
        dueTime = "15:30",
        timeLeftLabel = "3 hours ago",
        reminderFormatted = "",
        completedDateAndTime = "Time Here"
    )

    val task5 = Task(
        id = "2L",
        title = "Task 2 Title Goes Here",
        description =
        """
            This is a long description. This is a long description. This is a long description. 
            This is a long description.
        """.trimIndent(),
        done = true,
        overdue = true,
        dueDate = "Thu, 2 Feb",
        dueTime = "15:30",
        timeLeftLabel = "In 3 hours",
        reminderFormatted = "",
        completedDateAndTime = "Time Here"
    )

    val goals = arrayOf(
        Goal(
            id = "1",
            name = "Complete Tasks",
            description = "Complete 50 Tasks Every Week",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = 10,
            currentValue = 3,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Weekly,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.values().toList().toImmutableList()
            ),
            goalType = GoalType.TasksGoal
        ),
        Goal(
            id = "2",
            name = "Save Money Weekly",
            description = "Save $250 every week",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = 250,
            currentValue = 150,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Custom,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.values().toList().toImmutableList()
            ),
            goalType = GoalType.NumeralGoal
        ),
        Goal(
            id = UUIDGen.getString(),
            name = "Reduce Daily Phone Usage",
            description = "Only use my phone for not more than 5 hours everyday",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = (1.8e7).toLong(),
            currentValue = (9.2e7).toLong(),
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Daily,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.entries.toList().toImmutableList()
            ),
            goalType = GoalType.DeviceScreenTimeGoal
        )
    )
}
