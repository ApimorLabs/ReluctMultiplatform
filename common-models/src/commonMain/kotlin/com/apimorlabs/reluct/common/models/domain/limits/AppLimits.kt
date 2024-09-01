package com.apimorlabs.reluct.common.models.domain.limits

data class AppLimits(
    val appInfo: com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo,
    val timeLimit: Long,
    val isADistractingAp: Boolean,
    val isPaused: Boolean,
    val overridden: Boolean
)
