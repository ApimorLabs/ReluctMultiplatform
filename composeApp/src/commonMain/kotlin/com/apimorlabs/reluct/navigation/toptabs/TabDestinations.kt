package com.apimorlabs.reluct.navigation.toptabs

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.apimorlabs.reluct.navigation.destinations.ActiveGoalsDestination
import com.apimorlabs.reluct.navigation.destinations.CompletedTasksDestination
import com.apimorlabs.reluct.navigation.destinations.DashboardOverviewDestination
import com.apimorlabs.reluct.navigation.destinations.DashboardStatsDestination
import com.apimorlabs.reluct.navigation.destinations.InactiveGoalsDestination
import com.apimorlabs.reluct.navigation.destinations.PendingTasksDestination
import com.apimorlabs.reluct.navigation.destinations.ScreenTimeLimitsDestination
import com.apimorlabs.reluct.navigation.destinations.ScreenTimeStatsDestination
import com.apimorlabs.reluct.navigation.destinations.TasksStatisticsDestination
import kotlinx.serialization.Serializable

@Serializable
sealed class TabDestinations<T>(val route: T) {

    // Dashboard
    @Serializable
    data object DashboardOverview :
        TabDestinations<DashboardOverviewDestination>(DashboardOverviewDestination)

    @Serializable
    data object DashboardStats :
        TabDestinations<DashboardStatsDestination>(DashboardStatsDestination)

    // Tasks
    @Serializable
    data object TasksCompleted :
        TabDestinations<CompletedTasksDestination>(CompletedTasksDestination)

    @Serializable
    data object TasksStats : TabDestinations<TasksStatisticsDestination>(TasksStatisticsDestination)

    @Serializable
    data object TasksPending : TabDestinations<PendingTasksDestination>(PendingTasksDestination)

    // Goals
    @Serializable
    data object GoalsActive : TabDestinations<ActiveGoalsDestination>(ActiveGoalsDestination)

    @Serializable
    data object GoalsInactive : TabDestinations<InactiveGoalsDestination>(InactiveGoalsDestination)

    // Screen Time
    @Serializable
    data object ScreenTimeStats :
        TabDestinations<ScreenTimeStatsDestination>(ScreenTimeStatsDestination)

    @Serializable
    data object ScreenTimeLimits :
        TabDestinations<ScreenTimeLimitsDestination>(ScreenTimeLimitsDestination)


}

@Composable
internal fun GetTabTextColor(
    tabPage: TabDestinations<out Any>,
    selectedTabPage: TabDestinations<out Any>,
): Color =
    if (tabPage.route == selectedTabPage.route) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
            .copy(alpha = .5f)
    }
