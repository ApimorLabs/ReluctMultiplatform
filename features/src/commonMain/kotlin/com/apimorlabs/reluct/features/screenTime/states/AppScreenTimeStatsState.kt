package com.apimorlabs.reluct.features.screenTime.states

import com.apimorlabs.reluct.common.models.domain.limits.AppTimeLimit
import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageStats
import com.apimorlabs.reluct.common.models.util.time.Week
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

data class AppScreenTimeStatsState(
    val selectedInfo: ScreenTimeStatsSelectedInfo = ScreenTimeStatsSelectedInfo(),
    val weeklyData: WeeklyAppUsageStatsState = WeeklyAppUsageStatsState.Empty,
    val dailyData: DailyAppUsageStatsState = DailyAppUsageStatsState.Empty,
    val appSettingsState: AppSettingsState = AppSettingsState.Nothing
)

sealed class WeeklyAppUsageStatsState(
    val usageStats: ImmutableMap<Week, AppUsageStats>,
    val formattedTotalTime: String
) {
    data class Data(
        private val weeklyUsageStats: ImmutableMap<Week, AppUsageStats>,
        private val weeklyFormattedTotalTime: String
    ) : WeeklyAppUsageStatsState(weeklyUsageStats, weeklyFormattedTotalTime)

    class Loading(
        weeklyUsageStats: ImmutableMap<Week, AppUsageStats> = persistentMapOf(),
        weeklyFormattedTotalTime: String = "..."
    ) : WeeklyAppUsageStatsState(weeklyUsageStats, weeklyFormattedTotalTime)

    data object Empty : WeeklyAppUsageStatsState(persistentMapOf(), "...")
}

sealed class DailyAppUsageStatsState {
    data class Data(
        val usageStat: AppUsageStats,
        val dayText: String = usageStat.dateFormatted
    ) : DailyAppUsageStatsState()

    data object Empty : DailyAppUsageStatsState()
}

sealed class AppSettingsState {
    data object Nothing : AppSettingsState()
    data object Loading : AppSettingsState()
    data class Data(
        val appTimeLimit: AppTimeLimit,
        val isDistractingApp: Boolean,
        val isPaused: Boolean
    ) : AppSettingsState()
}