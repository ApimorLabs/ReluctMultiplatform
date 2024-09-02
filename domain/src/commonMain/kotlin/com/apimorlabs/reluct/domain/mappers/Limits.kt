package com.apimorlabs.reluct.domain.mappers

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import com.apimorlabs.reluct.common.models.domain.limits.AppLimits
import com.apimorlabs.reluct.data.source.database.models.LimitsDbObject
import com.apimorlabs.reluct.domain.usecases.appInfo.GetAppInfo

suspend fun LimitsDbObject.asAppLimits(getAppInfo: GetAppInfo) = AppLimits(
    appInfo = AppInfo(
        packageName = packageName,
        appName = getAppInfo.getAppName(packageName),
        appIcon = getAppInfo.getAppIcon(packageName)
    ),
    timeLimit = this.timeLimit,
    isADistractingAp = this.isADistractingAp,
    isPaused = this.isPaused,
    overridden = this.overridden
)

fun AppLimits.asLimitsDbObject() = LimitsDbObject(
    packageName = this.appInfo.packageName,
    timeLimit = this.timeLimit,
    isADistractingAp = this.isADistractingAp,
    isPaused = this.isPaused,
    overridden = this.overridden
)
