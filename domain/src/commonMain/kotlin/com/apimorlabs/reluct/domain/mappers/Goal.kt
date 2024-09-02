package com.apimorlabs.reluct.domain.mappers

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.common.models.domain.goals.GoalDuration
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.data.source.database.models.GoalDbObject
import com.apimorlabs.reluct.domain.usecases.appInfo.GetAppInfo
import kotlinx.collections.immutable.toImmutableList

fun Goal.asGoalDbObject() = GoalDbObject(
    id = id,
    name = name,
    description = description,
    isActive = isActive,
    relatedApps = relatedApps.map { it.packageName },
    targetValue = targetValue,
    currentValue = currentValue,
    goalInterval = goalDuration.goalInterval,
    timeInterval = goalDuration.timeRangeInMillis,
    daysOfWeekSelected = goalDuration.selectedDaysOfWeek,
    goalType = goalType
)

suspend fun GoalDbObject.asGoal(getAppInfo: GetAppInfo): Goal {
    val listedApps = relatedApps.map { packageName ->
        AppInfo(
            packageName = packageName,
            appName = getAppInfo.getAppName(packageName),
            appIcon = getAppInfo.getAppIcon(packageName)
        )
    }
    val formattedTimeRange = timeInterval?.let { range ->
        val start = TimeUtils.epochMillisToLocalDateTime(range.first).toString()
        val end = TimeUtils.epochMillisToLocalDateTime(range.last).toString()
        TimeUtils.getFormattedDateString(start)..TimeUtils.getFormattedDateString(end)
    }
    return Goal(
        id = id,
        name = name,
        description = description,
        isActive = isActive,
        relatedApps = listedApps.toImmutableList(),
        targetValue = targetValue,
        currentValue = currentValue,
        goalDuration = GoalDuration(
            goalInterval = goalInterval,
            timeRangeInMillis = timeInterval,
            formattedTimeRange = formattedTimeRange,
            selectedDaysOfWeek = daysOfWeekSelected.toImmutableList()
        ),
        goalType = goalType
    )
}
