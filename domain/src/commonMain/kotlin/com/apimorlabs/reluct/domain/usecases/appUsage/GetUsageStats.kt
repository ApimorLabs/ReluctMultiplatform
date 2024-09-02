package com.apimorlabs.reluct.domain.usecases.appUsage

import com.apimorlabs.reluct.common.models.domain.usageStats.UsageStats
import com.apimorlabs.reluct.common.models.util.time.Week
import kotlinx.collections.immutable.ImmutableMap

interface GetUsageStats {
    suspend fun dailyUsage(weekOffset: Int = 0, dayIsoNumber: Int): UsageStats
    suspend fun weeklyUsage(weekOffset: Int = 0): ImmutableMap<Week, UsageStats>
}
