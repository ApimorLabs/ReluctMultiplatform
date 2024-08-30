package com.apimorlabs.reluct.data.model.domain.limits

import com.apimorlabs.reluct.data.model.domain.appInfo.AppInfo

data class AppTimeLimit(
    val appInfo: AppInfo,
    val timeInMillis: Long,
    val hours: Int,
    val minutes: Int,
    val formattedTime: String
)
