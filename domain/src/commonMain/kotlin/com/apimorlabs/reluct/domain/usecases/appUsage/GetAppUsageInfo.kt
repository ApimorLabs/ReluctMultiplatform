package com.apimorlabs.reluct.domain.usecases.appUsage

import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageStats
import com.apimorlabs.reluct.common.models.util.time.Week
import kotlinx.collections.immutable.ImmutableMap

interface GetAppUsageInfo {
    suspend fun dailUsage(
        weekOffset: Int = 0,
        dayIsoNumber: Int,
        packageName: String
    ): AppUsageStats

    suspend fun weeklyUsage(weekOffset: Int, packageName: String): ImmutableMap<Week, AppUsageStats>
}
