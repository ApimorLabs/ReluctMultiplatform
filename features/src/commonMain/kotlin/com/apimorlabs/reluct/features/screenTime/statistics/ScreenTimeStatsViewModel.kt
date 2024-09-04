package com.apimorlabs.reluct.features.screenTime.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.common.models.util.time.WeekUtils
import com.apimorlabs.reluct.domain.usecases.appUsage.GetUsageStats
import com.apimorlabs.reluct.domain.usecases.limits.ManageAppTimeLimit
import com.apimorlabs.reluct.domain.usecases.time.GetWeekRangeFromOffset
import com.apimorlabs.reluct.features.screenTime.states.AppTimeLimitState
import com.apimorlabs.reluct.features.screenTime.states.DailyUsageStatsState
import com.apimorlabs.reluct.features.screenTime.states.ScreenTimeStatsSelectedInfo
import com.apimorlabs.reluct.features.screenTime.states.ScreenTimeStatsState
import com.apimorlabs.reluct.features.screenTime.states.WeeklyUsageStatsState
import com.apimorlabs.reluct.features.screenTime.states.events.ScreenTimeStatsEvents
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

class ScreenTimeStatsViewModel(
    private val getUsageStats: GetUsageStats,
    private val getWeekRangeFromOffset: GetWeekRangeFromOffset,
    private val manageAppTimeLimit: ManageAppTimeLimit
) : ViewModel() {

    private val selectedInfo: MutableStateFlow<ScreenTimeStatsSelectedInfo> =
        MutableStateFlow(ScreenTimeStatsSelectedInfo())
    private val weeklyUsageStatsState: MutableStateFlow<WeeklyUsageStatsState> =
        MutableStateFlow(WeeklyUsageStatsState.Loading())
    private val dailyUsageStatsState: MutableStateFlow<DailyUsageStatsState> =
        MutableStateFlow(DailyUsageStatsState.Empty)
    private val appTimeLimitState: MutableStateFlow<AppTimeLimitState> =
        MutableStateFlow(AppTimeLimitState.Nothing)

    private val isGranted = MutableStateFlow(false)

    val uiState: StateFlow<ScreenTimeStatsState> = combine(
        selectedInfo,
        weeklyUsageStatsState,
        dailyUsageStatsState,
        appTimeLimitState
    ) { selectedInfo, weeklyUsageStatsState, dailyUsageStatsState, appTimeLimitState ->
        ScreenTimeStatsState(
            selectedInfo = selectedInfo,
            weeklyData = weeklyUsageStatsState,
            dailyData = dailyUsageStatsState,
            appTimeLimit = appTimeLimitState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScreenTimeStatsState()
    )

    private val _events = Channel<ScreenTimeStatsEvents>(capacity = Channel.UNLIMITED)
    val events: Flow<ScreenTimeStatsEvents>
        get() = _events.receiveAsFlow()

    private var dailyScreenTimeStatsJob: Job? = null
    private var weeklyScreenTimeStatsJob: Job? = null
    private var getDataJob: Job? = null
    private var appTimeLimitJob: Job? = null

    init {
        val todayIsoNumber = WeekUtils.currentDayOfWeek().isoDayNumber
        selectedInfo.update { it.copy(selectedDay = todayIsoNumber) }
        getData()
    }

    private fun getData() {
        getDataJob = viewModelScope.launch {
            isGranted.collectLatest { granted ->
                if (granted) {
                    getWeeklyData()
                    getDailyData()
                }
            }
        }
    }

    private fun getDailyData() {
        dailyUsageStatsState.update { DailyUsageStatsState.Loading(dailyUsageStats = it.usageStat) }
        dailyScreenTimeStatsJob = viewModelScope.launch {
            val selected = selectedInfo.value
            val dailyData = getUsageStats.dailyUsage(
                weekOffset = selected.weekOffset,
                dayIsoNumber = selected.selectedDay
            )
            if (dailyData.appsUsageList.isEmpty()) {
                dailyUsageStatsState.update { DailyUsageStatsState.Empty }
            } else {
                dailyUsageStatsState.update {
                    DailyUsageStatsState.Data(
                        dailyUsageStats = dailyData
                    )
                }
            }
        }
    }

    private fun getWeeklyData() {
        weeklyUsageStatsState.update { WeeklyUsageStatsState.Loading() }
        weeklyScreenTimeStatsJob = viewModelScope.launch {
            val weekOffset = selectedInfo.value.weekOffset
            val weekOffsetText = getWeekRangeFromOffset.getOffset(weekOffset)
            selectedInfo.update { it.copy(selectedWeekText = weekOffsetText) }
            val weeklyData = getUsageStats.weeklyUsage(weekOffset = weekOffset)
            if (weeklyData.isEmpty()) {
                weeklyUsageStatsState.update { WeeklyUsageStatsState.Empty }
            } else {
                val totalWeeklyTimeInMillis = weeklyData.values.sumOf { it.totalScreenTime }
                val formattedTime = TimeUtils
                    .getFormattedTimeDurationString(totalWeeklyTimeInMillis)
                weeklyUsageStatsState.update {
                    WeeklyUsageStatsState.Data(
                        weeklyUsageStats = weeklyData,
                        weeklyFormattedTotalTime = formattedTime
                    )
                }
            }
        }
    }

    fun permissionCheck(isGranted: Boolean) {
        this.isGranted.update { isGranted }
    }

    fun selectAppTimeLimit(packageName: String) {
        appTimeLimitJob?.cancel()
        appTimeLimitJob = viewModelScope.launch {
            appTimeLimitState.update { AppTimeLimitState.Loading }
            val appTimeLimit = manageAppTimeLimit.getSync(packageName)
            appTimeLimitState.update { AppTimeLimitState.Data(timeLimit = appTimeLimit) }
        }
    }

    fun saveTimeLimit(hours: Int, minutes: Int) {
        viewModelScope.launch {
            val limitState = appTimeLimitState.value
            appTimeLimitJob?.cancel()
            if (limitState is AppTimeLimitState.Data) {
                val newLimit = limitState.timeLimit.copy(hours = hours, minutes = minutes)
                manageAppTimeLimit.setTimeLimit(newLimit)
                _events.send(ScreenTimeStatsEvents.TimeLimitChange(newLimit))
            }
        }
    }

    fun selectDay(selectedDayIsoNumber: Int) {
        dailyUsageStatsState.update { DailyUsageStatsState.Loading(it.usageStat) }
        selectedInfo.update { it.copy(selectedDay = selectedDayIsoNumber) }
        dailyScreenTimeStatsJob?.cancel()
        getDailyData()
    }

    fun updateWeekOffset(weekOffsetValue: Int) {
        weeklyUsageStatsState.update {
            WeeklyUsageStatsState.Loading(weeklyUsageStats = it.usageStats)
        }
        selectedInfo.update { it.copy(weekOffset = weekOffsetValue) }
        dailyScreenTimeStatsJob?.cancel()
        weeklyScreenTimeStatsJob?.cancel()
        getDataJob?.cancel()
        getData()
    }

    fun navigateToAppInfo(packageName: String) {
        _events.trySend(
            ScreenTimeStatsEvents.Navigation.NavigateToAppInfo(packageName)
        )
    }

    fun openAppTimerSettings(packageName: String) {
        _events.trySend(
            ScreenTimeStatsEvents.Navigation.OpenAppTimerSettings(packageName)
        )
    }

    override fun onCleared() {
        dailyScreenTimeStatsJob?.cancel()
        weeklyScreenTimeStatsJob?.cancel()
        getDataJob?.cancel()
        super.onCleared()
    }
}