package com.apimorlabs.reluct.features.screenTime.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageStats
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.common.models.util.time.Week
import com.apimorlabs.reluct.common.models.util.time.WeekUtils
import com.apimorlabs.reluct.domain.usecases.appUsage.GetAppUsageInfo
import com.apimorlabs.reluct.domain.usecases.limits.ManageAppTimeLimit
import com.apimorlabs.reluct.domain.usecases.limits.ManageDistractingApps
import com.apimorlabs.reluct.domain.usecases.limits.ManagePausedApps
import com.apimorlabs.reluct.domain.usecases.time.GetWeekRangeFromOffset
import com.apimorlabs.reluct.features.screenTime.states.AppScreenTimeStatsState
import com.apimorlabs.reluct.features.screenTime.states.AppSettingsState
import com.apimorlabs.reluct.features.screenTime.states.DailyAppUsageStatsState
import com.apimorlabs.reluct.features.screenTime.states.ScreenTimeStatsSelectedInfo
import com.apimorlabs.reluct.features.screenTime.states.WeeklyAppUsageStatsState
import com.apimorlabs.reluct.features.screenTime.states.events.ScreenTimeStatsEvents
import com.apimorlabs.reluct.features.util.Constants
import kotlinx.collections.immutable.ImmutableMap
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

/**
 * Remember to pass the packageName in Koin `parametersOf` when getting an instance
 * of this class using Koin injection.
 */
class AppScreenTimeStatsViewModel(
    private val packageName: String,
    private val getAppUsageInfo: GetAppUsageInfo,
    private val manageAppTimeLimit: ManageAppTimeLimit,
    private val manageDistractingApps: ManageDistractingApps,
    private val managePausedApps: ManagePausedApps,
    private val getWeekRangeFromOffset: GetWeekRangeFromOffset
) : ViewModel() {

    private val selectedInfo: MutableStateFlow<ScreenTimeStatsSelectedInfo> =
        MutableStateFlow(ScreenTimeStatsSelectedInfo())
    private val weeklyData: MutableStateFlow<WeeklyAppUsageStatsState> =
        MutableStateFlow(WeeklyAppUsageStatsState.Empty)
    private val dailyData: MutableStateFlow<DailyAppUsageStatsState> =
        MutableStateFlow(DailyAppUsageStatsState.Empty)
    private val appSettings: MutableStateFlow<AppSettingsState> =
        MutableStateFlow(AppSettingsState.Nothing)

    val uiState: StateFlow<AppScreenTimeStatsState> = combine(
        selectedInfo,
        weeklyData,
        dailyData,
        appSettings
    ) { selectedInfo, weeklyData, dailyData, appSettings ->
        AppScreenTimeStatsState(
            selectedInfo = selectedInfo,
            weeklyData = weeklyData,
            dailyData = dailyData,
            appSettingsState = appSettings
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = AppScreenTimeStatsState(),
        started = SharingStarted.WhileSubscribed(5_000L)
    )

    private val _events = Channel<ScreenTimeStatsEvents>(capacity = Channel.UNLIMITED)
    val events: Flow<ScreenTimeStatsEvents> = _events.receiveAsFlow()

    private var weeklyDataJob: Job? = null
    private var appTimeLimitJob: Job? = null
    private var appSettingsJob: Job? = null

    init {
        initialize()
    }

    private fun initialize() {
        val todayIsoNumber = WeekUtils.currentDayOfWeek().isoDayNumber
        selectedInfo.update { it.copy(selectedDay = todayIsoNumber) }
        getData()
        loadAppSettings()
    }

    fun toggleDistractingState(value: Boolean) {
        viewModelScope.launch {
            if (value) {
                manageDistractingApps.markAsDistracting(packageName)
                _events.send(
                    ScreenTimeStatsEvents.ShowMessageDone(
                        true,
                        Constants.MARK_DISTRACTING
                    )
                )
            } else {
                manageDistractingApps.markAsNotDistracting(packageName)
                _events.send(
                    ScreenTimeStatsEvents.ShowMessageDone(
                        false,
                        Constants.UN_MARK_DISTRACTING
                    )
                )
            }
        }
    }

    fun togglePausedState(value: Boolean) {
        viewModelScope.launch {
            if (value) {
                managePausedApps.pauseApp(packageName)
                _events.send(
                    ScreenTimeStatsEvents.ShowMessageDone(
                        true,
                        Constants.MARK_PAUSED
                    )
                )
            } else {
                managePausedApps.unPauseApp(packageName)
                _events.send(
                    ScreenTimeStatsEvents.ShowMessageDone(
                        false,
                        Constants.UN_MARK_PAUSED
                    )
                )
            }
        }
    }

    fun saveTimeLimit(hours: Int, minutes: Int) {
        viewModelScope.launch {
            val appSettingsState = appSettings.value
            appTimeLimitJob?.cancel()
            if (appSettingsState is AppSettingsState.Data) {
                val newLimit = appSettingsState.appTimeLimit.copy(hours = hours, minutes = minutes)
                manageAppTimeLimit.setTimeLimit(newLimit)
                _events.send(ScreenTimeStatsEvents.TimeLimitChange(newLimit))
            }
        }
    }

    fun selectDay(selectedDayIsoNumber: Int) {
        selectedInfo.update { it.copy(selectedDay = selectedDayIsoNumber) }
        val currentWeekData = weeklyData.value
        getDailyData(selectedDayIsoNumber, currentWeekData.usageStats)
    }

    fun updateWeekOffset(weekOffsetValue: Int) {
        weeklyData.update {
            WeeklyAppUsageStatsState.Loading(weeklyUsageStats = it.usageStats)
        }
        selectedInfo.update { it.copy(weekOffset = weekOffsetValue) }
        weeklyDataJob?.cancel()
        getData()
    }

    private fun getData() {
        getWeeklyData()
    }

    private fun loadAppSettings() {
        appSettingsJob?.cancel()
        appSettings.update { AppSettingsState.Loading }
        appSettingsJob = viewModelScope.launch {
            manageAppTimeLimit.getLimit(packageName).collectLatest { app ->
                val isDistracting = manageDistractingApps.isDistractingApp(app.appInfo.packageName)
                val isPaused = managePausedApps.isPaused(app.appInfo.packageName)
                appSettings.update {
                    AppSettingsState.Data(
                        appTimeLimit = app,
                        isDistractingApp = isDistracting,
                        isPaused = isPaused
                    )
                }
            }
        }
    }

    private fun getWeeklyData() {
        weeklyDataJob?.cancel()
        weeklyData.update { WeeklyAppUsageStatsState.Loading() }
        weeklyDataJob = viewModelScope.launch {
            val weekOffset = selectedInfo.value.weekOffset
            val selectedDay = selectedInfo.value.selectedDay
            val weekOffsetText = getWeekRangeFromOffset.getOffset(weekOffset)
            selectedInfo.update { it.copy(selectedWeekText = weekOffsetText) }
            val weekData = getAppUsageInfo.weeklyUsage(
                weekOffset = weekOffset,
                packageName = packageName
            )
            if (weekData.isEmpty()) {
                weeklyData.update { WeeklyAppUsageStatsState.Empty }
            } else {
                val totalTimeMillis = weekData.values.sumOf { it.appUsageInfo.timeInForeground }
                val formattedTime = TimeUtils.getFormattedTimeDurationString(totalTimeMillis)
                weeklyData.update {
                    WeeklyAppUsageStatsState.Data(
                        weeklyUsageStats = weekData,
                        weeklyFormattedTotalTime = formattedTime
                    )
                }
            }

            getDailyData(selectedDay, weekData)
        }
    }

    private fun getDailyData(selectedDayIso: Int, weekData: ImmutableMap<Week, AppUsageStats>) {
        val dayData = weekData.getValue(getWeek(selectedDayIso))
        dailyData.update {
            DailyAppUsageStatsState.Data(
                usageStat = dayData
            )
        }
    }

    private fun getWeek(dayIsoNumber: Int) = if (dayIsoNumber <= 0) {
        Week.MONDAY
    } else {
        Week.entries[dayIsoNumber - 1]
    }
}
