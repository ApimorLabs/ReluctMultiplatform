package com.apimorlabs.reluct.features.tasks.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.common.models.util.time.WeekUtils
import com.apimorlabs.reluct.domain.usecases.tasks.GetGroupedTasksStats
import com.apimorlabs.reluct.domain.usecases.tasks.ModifyTaskUseCase
import com.apimorlabs.reluct.domain.usecases.time.GetWeekRangeFromOffset
import com.apimorlabs.reluct.features.tasks.states.DailyTasksState
import com.apimorlabs.reluct.features.tasks.states.TasksStatisticsState
import com.apimorlabs.reluct.features.tasks.states.WeeklyTasksState
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TasksStatisticsViewModel(
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val getGroupedTasksStats: GetGroupedTasksStats,
    private val getWeekRangeFromOffset: GetWeekRangeFromOffset,
) : ViewModel() {

    private val weekOffset: MutableStateFlow<Int> = MutableStateFlow(0)
    private val selectedWeekText: MutableStateFlow<String> = MutableStateFlow("...")
    private val selectedDay: MutableStateFlow<Int> =
        MutableStateFlow(WeekUtils.currentDayOfWeek().isoDayNumber)
    private val weeklyTasksState: MutableStateFlow<WeeklyTasksState> =
        MutableStateFlow(WeeklyTasksState.Loading())
    private val dailyTasksState: MutableStateFlow<DailyTasksState> =
        MutableStateFlow(DailyTasksState.Loading())

    private lateinit var collectDailyTasksJob: Job
    private lateinit var collectWeeklyTasksJob: Job

    val uiState: StateFlow<TasksStatisticsState> = combine(
        weekOffset,
        selectedWeekText,
        selectedDay,
        weeklyTasksState,
        dailyTasksState
    ) { weekOffset, selectedWeekText, selectedDay, weeklyTasksState, dailyTasksState ->
        TasksStatisticsState(
            weekOffset = weekOffset,
            selectedWeekText = selectedWeekText,
            selectedDay = selectedDay,
            weeklyTasksState = weeklyTasksState,
            dailyTasksState = dailyTasksState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TasksStatisticsState()
    )

    private val _events: Channel<TasksEvents> = Channel()
    val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    init {
        getData()
    }

    private fun getData() {
        getWeeklyData()
        getDailyData()
    }

    private fun getDailyData() {
        collectDailyTasksJob = viewModelScope.launch {
            getGroupedTasksStats.dailyTasks(
                weekOffset = weekOffset.value,
                dayIsoNumber = selectedDay.value
            ).collectLatest { tasks ->
                if (tasks.completedTasks.isNotEmpty() || tasks.pendingTasks.isNotEmpty()) {
                    dailyTasksState.update {
                        DailyTasksState.Data(tasks = tasks, dayTextValue = tasks.dateFormatted)
                    }
                } else {
                    dailyTasksState.update {
                        DailyTasksState.Empty(dayTextValue = tasks.dateFormatted)
                    }
                }
            }
        }
    }

    private fun getWeeklyData() {
        collectWeeklyTasksJob = viewModelScope.launch {
            val weekOffsetText = getWeekRangeFromOffset.getOffset(weekOffset.value)
            selectedWeekText.update { weekOffsetText }
            getGroupedTasksStats.weeklyTasks(weekOffset = weekOffset.value)
                .collectLatest { weeklyTasks ->
                    if (weeklyTasks.isNotEmpty()) {
                        var totalTasksCount = 0
                        weeklyTasks.entries.forEach {
                            totalTasksCount += (it.value.completedTasksCount + it.value.pendingTasksCount)
                        }
                        weeklyTasksState.update {
                            WeeklyTasksState.Data(
                                tasks = weeklyTasks,
                                totalTaskCount = totalTasksCount
                            )
                        }
                    } else {
                        weeklyTasksState.update { WeeklyTasksState.Empty }
                    }
                }
        }
    }

    fun selectDay(selectedDayIsoNumber: Int) {
        dailyTasksState.update { DailyTasksState.Loading(dayTextValue = it.dayText) }
        selectedDay.update { selectedDayIsoNumber }
        collectDailyTasksJob.cancel()
        getDailyData()
    }

    fun updateWeekOffset(weekOffsetValue: Int) {
        weeklyTasksState.update { WeeklyTasksState.Loading(totalTaskCount = it.totalWeekTasksCount) }
        weekOffset.update { weekOffsetValue }
        collectDailyTasksJob.cancel()
        collectWeeklyTasksJob.cancel()
        getData()
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        viewModelScope.launch {
            modifyTasksUsesCase.toggleTaskDone(task, isDone)
            _events.send(TasksEvents.ShowMessageDone(isDone, task.title))
        }
    }

    fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }
}
