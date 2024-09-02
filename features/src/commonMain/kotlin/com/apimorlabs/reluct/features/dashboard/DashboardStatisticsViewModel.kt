package com.apimorlabs.reluct.features.dashboard

import androidx.lifecycle.ViewModel
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.features.screenTime.statistics.ScreenTimeStatsViewModel
import com.apimorlabs.reluct.features.screenTime.statistics.states.ScreenTimeStatsEvents
import com.apimorlabs.reluct.features.screenTime.statistics.states.allStats.ScreenTimeStatsState
import com.apimorlabs.reluct.features.tasks.states.TasksStatisticsState
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
import com.apimorlabs.reluct.features.tasks.statistics.TasksStatisticsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class DashboardStatisticsViewModel(
    private val screenTimeStatsViewModel: ScreenTimeStatsViewModel,
    private val tasksStatsViewModel: TasksStatisticsViewModel
) : ViewModel() {

    val screenTimeUiState: StateFlow<ScreenTimeStatsState> = screenTimeStatsViewModel.uiState
    val tasksStatsUiState: StateFlow<TasksStatisticsState> = tasksStatsViewModel.uiState

    val tasksStatsEvents: Flow<TasksEvents> = tasksStatsViewModel.events
    val screenTimeEvents: Flow<ScreenTimeStatsEvents> = screenTimeStatsViewModel.events

    init {
        screenTimeStatsViewModel.permissionCheck(true)
    }

    // For Tasks
    fun tasksSelectDay(dayIsoNumber: Int) = tasksStatsViewModel.selectDay(dayIsoNumber)
    fun toggleTaskDone(task: Task, isDone: Boolean) = tasksStatsViewModel.toggleDone(task, isDone)

    // For Screen Time
    fun screenTimeSelectDay(dayIsoNumber: Int) = screenTimeStatsViewModel.selectDay(dayIsoNumber)

    fun selectAppTimeLimit(packageName: String) =
        screenTimeStatsViewModel.selectAppTimeLimit(packageName)

    fun saveAppTimeLimit(hours: Int, minutes: Int) =
        screenTimeStatsViewModel.saveTimeLimit(hours, minutes)
}
