package com.apimorlabs.reluct.common.models.domain.usageStats

data class AppUsageStats(
    val appUsageInfo: AppUsageInfo,
    val dateFormatted: String = "...",
    val dayIsoNumber: Int = 0
)
