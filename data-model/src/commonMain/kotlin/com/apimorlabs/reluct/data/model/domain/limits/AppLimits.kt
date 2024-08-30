package com.apimorlabs.reluct.data.model.domain.limits

import com.apimorlabs.reluct.data.model.domain.appInfo.AppInfo

data class AppLimits(
    val appInfo: com.apimorlabs.reluct.data.model.domain.appInfo.AppInfo,
    val timeLimit: Long,
    val isADistractingAp: Boolean,
    val isPaused: Boolean,
    val overridden: Boolean
)
