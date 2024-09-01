package com.apimorlabs.reluct.data.source.appUsageStats.manager

import com.apimorlabs.reluct.data.source.appUsageStats.models.DataAppUsageInfo
import com.apimorlabs.reluct.data.source.appUsageStats.models.DataUsageStats

internal class DesktopUsageDataManager : UsageDataManager {

    override suspend fun getUsageStats(startTimeMillis: Long, endTimeMillis: Long): DataUsageStats {
        return DataUsageStats(emptyList(), 0)
    }

    override suspend fun getAppUsage(
        startTimeMillis: Long,
        endTimeMillis: Long,
        packageName: String
    ): DataAppUsageInfo {
        return DataAppUsageInfo(packageName, 0, 0)
    }
}
