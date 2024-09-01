package com.apimorlabs.reluct.data.source.util

import com.apimorlabs.reluct.data.source.appUsageStats.models.DataAppUsageInfo

fun Collection<DataAppUsageInfo>.sortByHighestForegroundTime(): List<DataAppUsageInfo> {
    return this.sortedByDescending { appInfo ->
        appInfo.timeInForeground
    }
}
