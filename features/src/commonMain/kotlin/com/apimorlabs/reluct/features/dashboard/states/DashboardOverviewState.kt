package com.apimorlabs.reluct.features.dashboard.states

import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.common.models.domain.usageStats.UsageStats
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DashboardOverviewState(
    val todayScreenTimeState: TodayScreenTimeState = TodayScreenTimeState.Loading(),
    val todayTasksState: TodayTasksState = TodayTasksState.Loading(),
    val goals: ImmutableList<Goal> = persistentListOf()
)

sealed class TodayScreenTimeState(
    val usageStats: UsageStats
) {
    data class Data(private val dailyUsageStats: UsageStats) :
        TodayScreenTimeState(usageStats = dailyUsageStats)

    class Loading(usageStats: UsageStats = UsageStats()) :
        TodayScreenTimeState(usageStats = usageStats)

    data object Nothing : TodayScreenTimeState(UsageStats())
}

sealed class TodayTasksState(
    val pending: ImmutableList<Task>
) {
    data class Data(private val tasks: ImmutableList<Task>) : TodayTasksState(pending = tasks)
    class Loading(tasks: ImmutableList<Task> = persistentListOf()) :
        TodayTasksState(pending = tasks)

    data object Nothing : TodayTasksState(pending = persistentListOf())
}
