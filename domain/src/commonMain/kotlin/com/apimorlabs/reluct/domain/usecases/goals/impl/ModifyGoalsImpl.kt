package com.apimorlabs.reluct.domain.usecases.goals.impl

import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.common.models.domain.goals.GoalInterval
import com.apimorlabs.reluct.common.models.domain.goals.GoalType
import com.apimorlabs.reluct.common.models.util.time.StatisticsTimeUtils
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.common.models.util.time.WeekUtils
import com.apimorlabs.reluct.data.source.appUsageStats.manager.UsageDataManager
import com.apimorlabs.reluct.data.source.database.dao.goals.GoalsDao
import com.apimorlabs.reluct.data.source.database.models.GoalDbObject
import com.apimorlabs.reluct.domain.mappers.asGoalDbObject
import com.apimorlabs.reluct.domain.usecases.goals.ModifyGoals
import com.apimorlabs.reluct.domain.usecases.tasks.GetGroupedTasksStats
import com.apimorlabs.reluct.system.services.haptics.HapticFeedback
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

internal class ModifyGoalsImpl(
    private val goalsDao: GoalsDao,
    private val haptics: HapticFeedback,
    private val usageDataManager: UsageDataManager,
    private val getGroupedTasksStats: GetGroupedTasksStats,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ModifyGoals {

    override suspend fun saveGoal(goal: Goal) = withContext(dispatcher) {
        goalsDao.insertGoal(goal.asGoalDbObject())
        haptics.click()
    }

    override suspend fun deleteGoal(id: String) = withContext(dispatcher) {
        goalsDao.deleteGoal(id)
        haptics.heavyClick()
    }

    override suspend fun toggleGoalActiveState(isActive: Boolean, id: String) =
        withContext(dispatcher) {
            goalsDao.toggleGoalActiveState(isActive = isActive, id = id)
            haptics.tick()
        }

    override suspend fun updateGoalCurrentValue(id: String, currentValue: Long) =
        withContext(dispatcher) {
            goalsDao.updateGoalCurrentValue(id = id, currentValue = currentValue)
            haptics.click()
        }

    override suspend fun syncGoals() = withContext(dispatcher) {
        val activeGoals = goalsDao.getActiveGoals(factor = 100).first()
        if (activeGoals.isNotEmpty()) {
            val updated = activeGoals.map { goal ->
                when (goal.goalType) {
                    GoalType.TasksGoal -> manageTasksGoalType(goal)
                    GoalType.DeviceScreenTimeGoal -> manageDeviceScreenTimeGoalType(goal)
                    GoalType.AppScreenTimeGoal -> manageAppScreenTimeGoalType(goal)
                    GoalType.NumeralGoal -> goal
                }
            }
            goalsDao.insertGoals(updated)
        }
    }

    private suspend fun manageTasksGoalType(goal: GoalDbObject): GoalDbObject =
        when (goal.goalInterval) {
            GoalInterval.Daily -> {
                val today = WeekUtils.currentDayOfWeek().isoDayNumber
                val checkToday = goal.daysOfWeekSelected.any { it.isoDayNumber == today }
                if (checkToday || goal.daysOfWeekSelected.isEmpty()) {
                    val todayTasks = getGroupedTasksStats.dailyTasks(dayIsoNumber = today).first()
                    goal.copy(currentValue = todayTasks.completedTasksCount.toLong())
                } else {
                    goal
                }
            }

            GoalInterval.Weekly -> {
                val weeklyTasks = getGroupedTasksStats.weeklyTasks().first()
                goal.copy(
                    currentValue = weeklyTasks.entries
                        .sumOf { it.value.completedTasksCount }
                        .toLong()
                )
            }

            GoalInterval.Custom -> {
                /**
                 * Check if the End of the Time Interval has not elapsed
                 * If elapsed, don't update the currentValue and automatically mark the
                 * Goal as not Active [isActive = false]
                 */
                when (val timeInterval = goal.timeInterval) {
                    null -> goal
                    else -> {
                        val endLdt = TimeUtils.epochMillisToLocalDateTimeString(timeInterval.last)
                        if (!TimeUtils.isDateTimeOverdue(dateTime = endLdt, overdueHours = 0)) {
                            val tasks = getGroupedTasksStats.timeRangeTasks(timeInterval).first()
                            goal.copy(currentValue = tasks.completedTasksCount.toLong())
                        } else {
                            goal.copy(isActive = false)
                        }
                    }
                }
            }
        }

    private suspend fun manageDeviceScreenTimeGoalType(goal: GoalDbObject): GoalDbObject =
        when (goal.goalInterval) {
            GoalInterval.Daily -> {
                val today = WeekUtils.currentDayOfWeek().isoDayNumber
                val checkToday = goal.daysOfWeekSelected.any { it.isoDayNumber == today }
                if (checkToday || goal.daysOfWeekSelected.isEmpty()) {
                    val selectedDayTimeRange = StatisticsTimeUtils.selectedDayTimeInMillisRange(
                        weekOffset = 0,
                        dayIsoNumber = today
                    )
                    val stats = usageDataManager.getUsageStats(
                        startTimeMillis = selectedDayTimeRange.first,
                        endTimeMillis = selectedDayTimeRange.last
                    )
                    goal.copy(currentValue = stats.appsUsageList.sumOf { it.timeInForeground })
                } else {
                    goal
                }
            }

            GoalInterval.Weekly -> {
                val weekTimeRange = StatisticsTimeUtils.weekTimeInMillisRange()
                val stats = usageDataManager.getUsageStats(
                    startTimeMillis = weekTimeRange.first,
                    endTimeMillis = weekTimeRange.last
                )
                goal.copy(currentValue = stats.appsUsageList.sumOf { it.timeInForeground })
            }

            GoalInterval.Custom -> {
                /**
                 * Check if the End of the Time Interval has not elapsed
                 * If elapsed, don't update the currentValue and automatically mark the
                 * Goal as not Active [isActive = false]
                 */
                when (val timeInterval = goal.timeInterval) {
                    null -> goal
                    else -> {
                        val endLdt = TimeUtils.epochMillisToLocalDateTimeString(timeInterval.last)
                        if (!TimeUtils.isDateTimeOverdue(dateTime = endLdt, overdueHours = 0)) {
                            val stats = usageDataManager.getUsageStats(
                                timeInterval.first,
                                timeInterval.last
                            )
                            goal.copy(
                                currentValue = stats.appsUsageList.sumOf { it.timeInForeground }
                            )
                        } else {
                            goal.copy(isActive = false)
                        }
                    }
                }
            }
        }

    private suspend fun manageAppScreenTimeGoalType(goal: GoalDbObject): GoalDbObject =
        when (goal.goalInterval) {
            GoalInterval.Daily -> {
                val today = WeekUtils.currentDayOfWeek().isoDayNumber
                val checkToday = goal.daysOfWeekSelected.any { it.isoDayNumber == today }
                if (checkToday || goal.daysOfWeekSelected.isEmpty()) {
                    val selectedDayTimeRange = StatisticsTimeUtils.selectedDayTimeInMillisRange(
                        weekOffset = 0,
                        dayIsoNumber = today
                    )
                    var screenTime = 0L
                    goal.relatedApps.forEach { packageName ->
                        screenTime += usageDataManager.getAppUsage(
                            startTimeMillis = selectedDayTimeRange.first,
                            endTimeMillis = selectedDayTimeRange.last,
                            packageName = packageName
                        ).timeInForeground
                    }
                    goal.copy(currentValue = screenTime)
                } else {
                    goal
                }
            }

            GoalInterval.Weekly -> {
                val weekTimeRange = StatisticsTimeUtils.weekTimeInMillisRange()
                var screenTime = 0L
                goal.relatedApps.forEach { packageName ->
                    screenTime += usageDataManager.getAppUsage(
                        startTimeMillis = weekTimeRange.first,
                        endTimeMillis = weekTimeRange.last,
                        packageName = packageName
                    ).timeInForeground
                }
                goal.copy(currentValue = screenTime)
            }

            GoalInterval.Custom -> {
                /**
                 * Check if the End of the Time Interval has not elapsed
                 * If elapsed, don't update the currentValue and automatically mark the
                 * Goal as not Active [isActive = false]
                 */
                when (val timeInterval = goal.timeInterval) {
                    null -> goal
                    else -> {
                        val endLdt = TimeUtils.epochMillisToLocalDateTimeString(timeInterval.last)
                        if (!TimeUtils.isDateTimeOverdue(dateTime = endLdt, overdueHours = 0)) {
                            var screenTime = 0L
                            goal.relatedApps.forEach { packageName ->
                                screenTime += usageDataManager.getAppUsage(
                                    startTimeMillis = timeInterval.first,
                                    endTimeMillis = timeInterval.last,
                                    packageName = packageName
                                ).timeInForeground
                            }
                            goal.copy(currentValue = screenTime)
                        } else {
                            goal.copy(isActive = false)
                        }
                    }
                }
            }
        }
}