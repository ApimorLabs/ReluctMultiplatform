package com.apimorlabs.reluct.common.models.domain.limits

data class AppTimeLimit(
    val appInfo: com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo,
    val timeInMillis: Long,
    val hours: Int,
    val minutes: Int,
    val formattedTime: String
)
