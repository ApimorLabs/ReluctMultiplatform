package com.apimorlabs.reluct.features.screenTime.states

import com.apimorlabs.reluct.common.models.domain.usageStats.UsageStats
import com.apimorlabs.reluct.common.models.util.time.Week
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

data class ScreenTimeStatsState(
    val selectedInfo: ScreenTimeStatsSelectedInfo = ScreenTimeStatsSelectedInfo(),
    val weeklyData: WeeklyUsageStatsState = WeeklyUsageStatsState.Loading(),
    val dailyData: DailyUsageStatsState = DailyUsageStatsState.Loading(),
    val appTimeLimit: AppTimeLimitState = AppTimeLimitState.Nothing
)

sealed class WeeklyUsageStatsState(
    val usageStats: ImmutableMap<Week, UsageStats>,
    val formattedTotalTime: String
) {
    data class Data(
        private val weeklyUsageStats: ImmutableMap<Week, UsageStats>,
        private val weeklyFormattedTotalTime: String
    ) : WeeklyUsageStatsState(weeklyUsageStats, weeklyFormattedTotalTime)

    class Loading(
        weeklyUsageStats: ImmutableMap<Week, UsageStats> = persistentMapOf(),
        weeklyFormattedTotalTime: String = "..."
    ) : WeeklyUsageStatsState(weeklyUsageStats, weeklyFormattedTotalTime)

    data object Empty : WeeklyUsageStatsState(persistentMapOf(), "...")
}

sealed class DailyUsageStatsState(
    val usageStat: UsageStats,
    val dayText: String
) {
    data class Data(
        private val dailyUsageStats: UsageStats
    ) : DailyUsageStatsState(dailyUsageStats, dailyUsageStats.dateFormatted)

    class Loading(
        dailyUsageStats: UsageStats = UsageStats()
    ) : DailyUsageStatsState(dailyUsageStats, dailyUsageStats.dateFormatted)

    data object Empty : DailyUsageStatsState(UsageStats(), "...")
}
