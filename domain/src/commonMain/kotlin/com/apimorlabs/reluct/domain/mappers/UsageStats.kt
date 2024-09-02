package com.apimorlabs.reluct.domain.mappers

import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageInfo
import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageStats
import com.apimorlabs.reluct.common.models.domain.usageStats.UsageStats
import com.apimorlabs.reluct.common.models.util.time.StatisticsTimeUtils
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.data.source.appUsageStats.models.DataAppUsageInfo
import com.apimorlabs.reluct.data.source.appUsageStats.models.DataUsageStats
import com.apimorlabs.reluct.domain.usecases.appInfo.GetAppInfo
import kotlinx.collections.immutable.toImmutableList

/**
 * If showShortIntervalAsDay is true then it will show Today, Yesterday, Tomorrow
 * or a day instead of the full date when the interval is within a 3 days difference.
 * If false it will show the full date even if the interval is within a 3 days difference
 */
suspend fun DataUsageStats.asUsageStats(
    weekOffset: Int,
    dayIsoNumber: Int,
    showIntervalAsDay: Boolean = true,
    getAppInfo: GetAppInfo
): UsageStats {
    val selectedDayDateTimeString = StatisticsTimeUtils.selectedDayDateTimeString(
        weekOffset = weekOffset,
        selectedDayIsoNumber = dayIsoNumber
    )
    val totalScreenTime = appsUsageList.sumOf { it.timeInForeground }
    return UsageStats(
        appsUsageList = this.appsUsageList.map { it.asAppUsageInfo(getAppInfo) }.toImmutableList(),
        dateFormatted = TimeUtils.getFormattedDateString(
            dateTime = selectedDayDateTimeString,
            showShortIntervalAsDay = showIntervalAsDay
        ),
        totalScreenTime = totalScreenTime,
        formattedTotalScreenTime = TimeUtils
            .getFormattedTimeDurationString(totalScreenTime),
        unlockCount = this.unlockCount
    )
}

// App Usage Stats

suspend fun DataAppUsageInfo.asAppUsageStats(
    weekOffset: Int,
    dayIsoNumber: Int,
    showIntervalAsDay: Boolean = true,
    getAppInfo: GetAppInfo
): AppUsageStats {
    val selectedDayDateTimeString = StatisticsTimeUtils.selectedDayDateTimeString(
        weekOffset = weekOffset,
        selectedDayIsoNumber = dayIsoNumber
    )
    return AppUsageStats(
        appUsageInfo = this.asAppUsageInfo(getAppInfo),
        dateFormatted = TimeUtils.getFormattedDateString(
            dateTime = selectedDayDateTimeString,
            showShortIntervalAsDay = showIntervalAsDay
        )
    )
}

// App Usage Info

suspend fun DataAppUsageInfo.asAppUsageInfo(getAppInfo: GetAppInfo): AppUsageInfo = AppUsageInfo(
    packageName = this.packageName,
    appName = getAppInfo.getAppName(packageName),
    appIcon = getAppInfo.getAppIcon(packageName),
    timeInForeground = this.timeInForeground,
    formattedTimeInForeground = TimeUtils
        .getFormattedTimeDurationString(this.timeInForeground),
    appLaunchCount = this.appLaunchCount
)
