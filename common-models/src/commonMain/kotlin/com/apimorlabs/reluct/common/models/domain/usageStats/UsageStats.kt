package com.apimorlabs.reluct.common.models.domain.usageStats

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UsageStats(
    val appsUsageList: ImmutableList<AppUsageInfo> = persistentListOf(),
    val dateFormatted: String = "...",
    val totalScreenTime: Long = 0L,
    val formattedTotalScreenTime: String = "",
    val unlockCount: Long = 0L
)
