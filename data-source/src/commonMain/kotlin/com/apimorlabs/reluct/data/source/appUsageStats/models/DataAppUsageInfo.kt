package com.apimorlabs.reluct.data.source.appUsageStats.models

data class DataAppUsageInfo(
    val packageName: String,
    var timeInForeground: Long = 0,
    var appLaunchCount: Int = 0,
)
