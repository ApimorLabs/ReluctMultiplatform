package com.apimorlabs.reluct.navigation.destinations

import com.apimorlabs.reluct.common.models.util.AppURI
import kotlinx.serialization.Serializable

@Serializable
object ScreenTimeStatsDestination

@Serializable
object ScreenTimeLimitsDestination

@Serializable
data class AppScreenTimeStatsDestination(val packageName: String?)

object AppScreenTimeStatsLink {
    const val DEEP_LINK = "${AppURI.BASE_URI}/screen_time_stats"
    fun deepLink(packageName: String) = "$DEEP_LINK?packageName=$packageName"
}
