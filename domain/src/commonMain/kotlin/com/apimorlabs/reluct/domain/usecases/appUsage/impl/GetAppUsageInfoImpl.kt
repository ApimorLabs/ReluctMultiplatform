package com.apimorlabs.reluct.domain.usecases.appUsage.impl

import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageStats
import com.apimorlabs.reluct.common.models.util.time.StatisticsTimeUtils
import com.apimorlabs.reluct.common.models.util.time.Week
import com.apimorlabs.reluct.data.source.appUsageStats.manager.UsageDataManager
import com.apimorlabs.reluct.domain.mappers.asAppUsageStats
import com.apimorlabs.reluct.domain.usecases.appInfo.GetAppInfo
import com.apimorlabs.reluct.domain.usecases.appUsage.GetAppUsageInfo
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class GetAppUsageInfoImpl(
    private val usageManager: UsageDataManager,
    private val backgroundDispatcher: CoroutineDispatcher,
    private val getAppInfo: GetAppInfo
) : GetAppUsageInfo {

    private val daysOfWeek = Week.entries

    override suspend fun dailUsage(
        weekOffset: Int,
        dayIsoNumber: Int,
        packageName: String
    ): AppUsageStats = withContext(backgroundDispatcher) {
        val selectedDayTimeRange = StatisticsTimeUtils.selectedDayTimeInMillisRange(
            weekOffset = weekOffset,
            dayIsoNumber = dayIsoNumber
        )
        val appStats = usageManager.getAppUsage(
            startTimeMillis = selectedDayTimeRange.first,
            endTimeMillis = selectedDayTimeRange.last,
            packageName = packageName
        )
        appStats.asAppUsageStats(
            weekOffset = weekOffset,
            dayIsoNumber = dayIsoNumber,
            getAppInfo = getAppInfo
        )
    }

    override suspend fun weeklyUsage(
        weekOffset: Int,
        packageName: String
    ): ImmutableMap<Week, AppUsageStats> = withContext(backgroundDispatcher) {
        persistentMapOf<Week, AppUsageStats>().builder().apply {
            for (week in daysOfWeek) {
                put(
                    key = week,
                    value = dailUsage(
                        weekOffset = weekOffset,
                        dayIsoNumber = week.isoDayNumber,
                        packageName = packageName
                    )
                )
            }
        }.build().toImmutableMap()
    }
}
