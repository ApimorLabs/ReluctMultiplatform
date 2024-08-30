package com.apimorlabs.reluct.data.model.domain.usageStats

import com.apimorlabs.reluct.data.model.domain.appInfo.Icon

data class AppUsageInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Icon,
    val timeInForeground: Long = 0,
    val formattedTimeInForeground: String = "",
    val appLaunchCount: Int = 0,
)
