package com.apimorlabs.reluct.features.tasks.states

import com.apimorlabs.reluct.common.models.domain.tasks.DailyTasksStats
import com.apimorlabs.reluct.common.models.util.time.Week
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

data class TasksStatisticsState(
    val weekOffset: Int = 0,
    val selectedWeekText: String = "...",
    val selectedDay: Int = 0,
    val weeklyTasksState: WeeklyTasksState = WeeklyTasksState.Loading(),
    val dailyTasksState: DailyTasksState = DailyTasksState.Loading(),
)

sealed class WeeklyTasksState(
    val weeklyTasks: ImmutableMap<Week, DailyTasksStats>,
    val totalWeekTasksCount: Int,
) {
    data class Data(
        val tasks: ImmutableMap<Week, DailyTasksStats>,
        val totalTaskCount: Int,
    ) : WeeklyTasksState(tasks, totalTaskCount)

    class Loading(
        tasks: ImmutableMap<Week, DailyTasksStats> = persistentMapOf(),
        totalTaskCount: Int = 0,
    ) : WeeklyTasksState(tasks, totalTaskCount)

    data object Empty : WeeklyTasksState(persistentMapOf(), 0)
}

sealed class DailyTasksState(
    val dailyTasks: DailyTasksStats,
    val dayText: String,
) {
    data class Data(
        val tasks: DailyTasksStats,
        val dayTextValue: String = tasks.dateFormatted,
    ) : DailyTasksState(dailyTasks = tasks, dayText = dayTextValue)

    class Loading(
        tasks: DailyTasksStats = DailyTasksStats(),
        dayTextValue: String = "...",
    ) : DailyTasksState(dailyTasks = tasks, dayText = dayTextValue)

    class Empty(
        tasks: DailyTasksStats = DailyTasksStats(),
        dayTextValue: String = "...",
    ) : DailyTasksState(dailyTasks = tasks, dayText = dayTextValue)
}
