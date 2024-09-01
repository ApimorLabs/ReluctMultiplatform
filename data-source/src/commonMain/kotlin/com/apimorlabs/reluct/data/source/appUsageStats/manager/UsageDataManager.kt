package com.apimorlabs.reluct.data.source.appUsageStats.manager

import com.apimorlabs.reluct.data.source.appUsageStats.models.DataAppUsageInfo
import com.apimorlabs.reluct.data.source.appUsageStats.models.DataUsageStats

interface UsageDataManager {
    /**
     * Returns DataUsageStats that contains a list of DataAppUsageInfo sorted by
     * timeInForeground descending
     */
    suspend fun getUsageStats(startTimeMillis: Long, endTimeMillis: Long): DataUsageStats

    /**
     * Returns DataAppUsageInfo for specified app package name in specified time range
     */
    suspend fun getAppUsage(
        startTimeMillis: Long,
        endTimeMillis: Long,
        packageName: String
    ): DataAppUsageInfo
}
