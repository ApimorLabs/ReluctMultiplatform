package com.apimorlabs.reluct.features.screenTime.services

import com.apimorlabs.reluct.common.models.util.AppURI

object ScreenTimeLink {
    val deeplink = "${AppURI.BASE_URI}/screen-time"
    fun appScreenTimeDeepLink(packageName: String) = "${deeplink}?packageName=$packageName"
}
