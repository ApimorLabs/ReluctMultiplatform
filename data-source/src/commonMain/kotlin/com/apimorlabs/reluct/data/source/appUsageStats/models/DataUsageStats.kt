package com.apimorlabs.reluct.data.source.appUsageStats.models

data class DataUsageStats(
    val appsUsageList: List<DataAppUsageInfo>,
    val unlockCount: Long,
)
