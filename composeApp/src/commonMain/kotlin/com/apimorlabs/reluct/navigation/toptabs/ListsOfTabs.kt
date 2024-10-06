package com.apimorlabs.reluct.navigation.toptabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.dashboard_overview
import com.apimorlabs.reluct.compose.ui.dashboard_statistics
import com.apimorlabs.reluct.compose.ui.goals_active
import com.apimorlabs.reluct.compose.ui.goals_inactive
import com.apimorlabs.reluct.compose.ui.screen_time_limit
import com.apimorlabs.reluct.compose.ui.screen_time_stats
import com.apimorlabs.reluct.compose.ui.tasks_done
import com.apimorlabs.reluct.compose.ui.tasks_pending
import com.apimorlabs.reluct.compose.ui.tasks_stats
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun rememberDashboardTabs(): ImmutableList<TabBar<out Any>> {
    val dashboardOverviewText = stringResource(Res.string.dashboard_overview)
    val dashboardStatsText = stringResource(Res.string.dashboard_statistics)

    return remember {
        persistentListOf(
            TabBar(TabDestinations.DashboardOverview, dashboardOverviewText),
            TabBar(TabDestinations.DashboardStats, dashboardStatsText)
        )
    }
}

@Composable
internal fun rememberTasksTabs(): ImmutableList<TabBar<out Any>> {
    val pendingTasksText = stringResource(Res.string.tasks_pending)
    val completedTasksText = stringResource(Res.string.tasks_stats)
    val tasksStatsText = stringResource(Res.string.tasks_done)

    return remember {
        persistentListOf(
            TabBar(TabDestinations.TasksPending, pendingTasksText),
            TabBar(TabDestinations.TasksCompleted, completedTasksText),
            TabBar(TabDestinations.TasksStats, tasksStatsText)
        )
    }
}

@Composable
internal fun rememberGoalsTabs(): ImmutableList<TabBar<out Any>> {
    val goalsActiveText = stringResource(Res.string.goals_active)
    val goalsInactiveText = stringResource(Res.string.goals_inactive)

    return remember {
        persistentListOf(
            TabBar(TabDestinations.TasksPending, goalsActiveText),
            TabBar(TabDestinations.TasksCompleted, goalsInactiveText)
        )
    }
}

@Composable
internal fun rememberScreenTimeTabs(): ImmutableList<TabBar<out Any>> {
    val screenTimeStatsText = stringResource(Res.string.screen_time_stats)
    val screenTimeLimitsText = stringResource(Res.string.screen_time_limit)

    return remember {
        persistentListOf(
            TabBar(TabDestinations.ScreenTimeStats, screenTimeStatsText),
            TabBar(TabDestinations.ScreenTimeLimits, screenTimeLimitsText)
        )
    }
}
