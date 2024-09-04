package com.apimorlabs.reluct.features.goals.states

import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.common.models.domain.goals.GoalDuration
import com.apimorlabs.reluct.common.models.domain.goals.GoalInterval
import com.apimorlabs.reluct.common.models.domain.goals.GoalType
import com.apimorlabs.reluct.common.models.util.UUIDGen
import com.apimorlabs.reluct.common.models.util.time.Week
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

object DefaultGoals {
    fun predefined() = arrayOf(
        Goal(
            id = UUIDGen.getString(),
            name = "Reduce Daily Phone Usage",
            description = "Only use my phone for not more than 5 hours everyday",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = (1.8e7).toLong(),
            currentValue = 0,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Daily,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.entries.toList().toImmutableList()
            ),
            goalType = GoalType.DeviceScreenTimeGoal
        ),
        Goal(
            id = UUIDGen.getString(),
            name = "Reduce App Usage",
            description = "Use certain app(s) for a limited time",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = (7.2e6).toLong(),
            currentValue = 0,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Daily,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.entries.toList().toImmutableList()
            ),
            goalType = GoalType.AppScreenTimeGoal
        ),
        Goal(
            id = UUIDGen.getString(),
            name = "Complete Tasks",
            description = "Complete 50 Tasks Every Week",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = 10,
            currentValue = 0,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Weekly,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.entries.toList().toImmutableList()
            ),
            goalType = GoalType.TasksGoal
        ),
        Goal(
            id = UUIDGen.getString(),
            name = "Save Money Weekly",
            description = "Save $50 every week",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = 50,
            currentValue = 0,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Weekly,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.entries.toList().toImmutableList()
            ),
            goalType = GoalType.NumeralGoal
        )
    )

    fun emptyGoal() = Goal(
        id = UUIDGen.getString(),
        name = "",
        description = "",
        isActive = true,
        relatedApps = persistentListOf(),
        targetValue = 0,
        currentValue = 0,
        goalDuration = GoalDuration(
            goalInterval = GoalInterval.Daily,
            timeRangeInMillis = null,
            formattedTimeRange = null,
            selectedDaysOfWeek = Week.entries.toList().toImmutableList()
        ),
        goalType = GoalType.NumeralGoal
    )
}
