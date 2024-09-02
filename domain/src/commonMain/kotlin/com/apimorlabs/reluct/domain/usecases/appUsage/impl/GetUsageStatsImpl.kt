package com.apimorlabs.reluct.domain.usecases.appUsage.impl

import com.apimorlabs.reluct.common.models.domain.usageStats.UsageStats
import com.apimorlabs.reluct.common.models.util.time.StatisticsTimeUtils
import com.apimorlabs.reluct.common.models.util.time.Week
import com.apimorlabs.reluct.data.source.appUsageStats.manager.UsageDataManager
import com.apimorlabs.reluct.domain.mappers.asUsageStats
import com.apimorlabs.reluct.domain.usecases.appInfo.GetAppInfo
import com.apimorlabs.reluct.domain.usecases.appUsage.GetUsageStats
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class GetUsageStatsImpl(
    private val usageManager: UsageDataManager,
    private val getAppInfo: GetAppInfo,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetUsageStats {
    private val daysOfWeek = Week.entries

    override suspend fun dailyUsage(weekOffset: Int, dayIsoNumber: Int): UsageStats =
        withContext(backgroundDispatcher) {
            val selectedDayTimeRange = StatisticsTimeUtils.selectedDayTimeInMillisRange(
                weekOffset = weekOffset,
                dayIsoNumber = dayIsoNumber
            )
            val dataUsageStats = usageManager.getUsageStats(
                startTimeMillis = selectedDayTimeRange.first,
                endTimeMillis = selectedDayTimeRange.last
            )
            dataUsageStats.asUsageStats(
                weekOffset = weekOffset,
                dayIsoNumber = dayIsoNumber,
                getAppInfo = getAppInfo
            )
        }

    override suspend fun weeklyUsage(weekOffset: Int): ImmutableMap<Week, UsageStats> =
        withContext(backgroundDispatcher) {
            persistentMapOf<Week, UsageStats>().builder().apply {
                for (day in daysOfWeek) {
                    put(
                        key = day,
                        value = dailyUsage(weekOffset, day.isoDayNumber)
                    )
                }
            }.build().toImmutableMap()
        }
}
