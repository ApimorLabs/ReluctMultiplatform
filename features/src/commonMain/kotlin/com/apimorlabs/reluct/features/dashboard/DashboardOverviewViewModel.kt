package com.apimorlabs.reluct.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.common.models.util.time.WeekUtils
import com.apimorlabs.reluct.domain.usecases.appUsage.GetUsageStats
import com.apimorlabs.reluct.domain.usecases.goals.GetGoals
import com.apimorlabs.reluct.domain.usecases.goals.ModifyGoals
import com.apimorlabs.reluct.domain.usecases.tasks.GetTasksUseCase
import com.apimorlabs.reluct.domain.usecases.tasks.ModifyTaskUseCase
import com.apimorlabs.reluct.features.dashboard.states.DashboardOverviewState
import com.apimorlabs.reluct.features.dashboard.states.TodayScreenTimeState
import com.apimorlabs.reluct.features.dashboard.states.TodayTasksState
import com.apimorlabs.reluct.features.dashboard.states.events.DashboardEvents
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.apimorlabs.reluct.features.screenTime.services.ScreenTimeServices

class DashboardOverviewViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val getUsageStats: GetUsageStats,
    private val getGoals: GetGoals,
    private val modifyGoals: ModifyGoals,
    private val screenTimeServices: ScreenTimeServices
) : ViewModel() {

    private val todayScreenTimeState: MutableStateFlow<TodayScreenTimeState> =
        MutableStateFlow(TodayScreenTimeState.Loading())
    private val todayTasksState: MutableStateFlow<TodayTasksState> =
        MutableStateFlow(TodayTasksState.Loading())
    private val goals: MutableStateFlow<ImmutableList<Goal>> = MutableStateFlow(persistentListOf())

    val uiState: StateFlow<DashboardOverviewState> = combine(
        todayScreenTimeState,
        todayTasksState,
        goals
    ) { todayScreenTimeState, todayTasksState, goals ->
        DashboardOverviewState(
            todayScreenTimeState = todayScreenTimeState,
            todayTasksState = todayTasksState,
            goals = goals
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DashboardOverviewState()
    )

    private val _events = Channel<DashboardEvents>(Channel.UNLIMITED)
    val events: Flow<DashboardEvents> = _events.receiveAsFlow()

    private var dailyScreenTimeJob: Job? = null
    private var permissionsJob: Job? = null

    private val permissionGranted = MutableStateFlow(false)

    init {
        initializeScreenTimeData()
        getPendingTasks()
        initializeGoals()
    }

    fun permissionCheck(isGranted: Boolean) {
        permissionGranted.update { isGranted }
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        viewModelScope.launch {
            modifyTasksUsesCase.toggleTaskDone(task, isDone)
            _events.send(DashboardEvents.ShowMessageDone(isDone, task.title))
        }
    }

    private fun getPendingTasks() {
        todayTasksState.update { TodayTasksState.Loading(it.pending) }
        viewModelScope.launch {
            // We get only 5 pending Tasks
            getTasksUseCase.getPendingTasks(factor = 1L, limitBy = 5).collectLatest { tasks ->
                todayTasksState.update { TodayTasksState.Data(tasks = tasks) }
            }
        }
    }

    private fun initializeScreenTimeData() {
        permissionsJob?.cancel()
        permissionsJob = viewModelScope.launch {
            permissionGranted.collectLatest { isGranted ->
                if (isGranted) {
                    screenTimeServices.startLimitsService()
                    getDailyScreenTime()
                }
            }
        }
    }

    private fun getDailyScreenTime() {
        dailyScreenTimeJob?.cancel()
        todayScreenTimeState.update { TodayScreenTimeState.Loading(usageStats = it.usageStats) }
        dailyScreenTimeJob = viewModelScope.launch {
            val today = WeekUtils.currentDayOfWeek()
            val dailyData = getUsageStats.dailyUsage(dayIsoNumber = today.isoDayNumber)
            todayScreenTimeState.update {
                TodayScreenTimeState.Data(
                    dailyUsageStats = dailyData
                )
            }
        }
    }

    private fun getGoals() {
        viewModelScope.launch {
            // We get only 3 goals
            getGoals.getActiveGoals(factor = 1L, limitBy = 3).collectLatest { data ->
                goals.update { data }
            }
        }
    }

    private fun initializeGoals() {
        getGoals()
        viewModelScope.launch { modifyGoals.syncGoals() }
    }
}
