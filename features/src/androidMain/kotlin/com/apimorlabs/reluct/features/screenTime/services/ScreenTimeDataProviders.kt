package com.apimorlabs.reluct.features.screenTime.services

import android.app.KeyguardManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageStats
import com.apimorlabs.reluct.common.models.util.time.WeekUtils
import com.apimorlabs.reluct.domain.usecases.appUsage.GetAppUsageInfo
import com.apimorlabs.reluct.features.screenTime.permissions.UsageAccessPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock

internal object ScreenTimeDataProviders {
    fun getCurrentAppStats(
        getAppUsageInfo: GetAppUsageInfo,
        applicationContext: Context
    ): Flow<AppUsageStats> = flow {
        while (true) {
            delay(5000)
            if (UsageAccessPermission.isAllowed(applicationContext)) {
                val currentTime = Clock.System.now().toEpochMilliseconds()
                val startTimeMillis = currentTime - (1000 * 1800)
                var latestEvent: UsageEvents.Event? = null

                val keyguardManager = applicationContext
                    .getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                val usageStats = applicationContext
                    .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                val usageEvents = if (!keyguardManager.isKeyguardLocked) {
                    usageStats
                        .queryEvents(startTimeMillis, currentTime)
                } else {
                    null
                }

                while (usageEvents?.hasNextEvent() == true) {
                    val currentEvent = UsageEvents.Event()
                    usageEvents.getNextEvent(currentEvent)
                    if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                        latestEvent = currentEvent
                    }
                }
                latestEvent?.let { event ->
                    getAppUsageInfo.dailUsage(
                        dayIsoNumber = WeekUtils.currentDayOfWeek().isoDayNumber,
                        packageName = event.packageName
                    )
                }?.let { app -> emit(app) } // Emitting the stats for the app.
            }
        }
    }.flowOn(Dispatchers.IO)
}
