package com.apimorlabs.reluct.features.screenTime.services

import com.apimorlabs.reluct.common.models.util.AppURI

object ScreenTimeLink {
    const val DEEP_LINK = "${AppURI.BASE_URI}/screen_time"
    fun deepLink(packageName: String) = "$DEEP_LINK?packageName=$packageName"
}
