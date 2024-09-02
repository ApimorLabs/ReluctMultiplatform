package com.apimorlabs.reluct.domain.mappers

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import com.apimorlabs.reluct.common.models.domain.limits.AppLimits
import com.apimorlabs.reluct.common.models.domain.limits.AppTimeLimit
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.data.source.database.models.LimitsDbObject
import com.apimorlabs.reluct.domain.usecases.appInfo.GetAppInfo
import kotlin.math.roundToInt
import kotlin.math.roundToLong

typealias Hour = Int
typealias Minute = Int

fun AppLimits.asTimeLimit(): AppTimeLimit {
    val time = convertMillisToTime(timeLimit)
    val formatted = TimeUtils.getFormattedTimeDurationString(timeLimit)
    return AppTimeLimit(
        appInfo = appInfo,
        timeInMillis = timeLimit,
        hours = time.first,
        minutes = time.second,
        formattedTime = formatted
    )
}

suspend fun LimitsDbObject.asTimeLimit(getAppInfo: GetAppInfo): AppTimeLimit {
    val time = convertMillisToTime(timeLimit)
    val formatted = TimeUtils.getFormattedTimeDurationString(timeLimit)
    return AppTimeLimit(
        appInfo = AppInfo(
            packageName = packageName,
            appName = getAppInfo.getAppName(packageName),
            appIcon = getAppInfo.getAppIcon(packageName)
        ),
        timeInMillis = timeLimit,
        hours = time.first,
        minutes = time.second,
        formatted
    )
}

/**
 * Convert the provided time in milliseconds to Hours and Minutes Pair.
 * The first item is hours the second is minutes.
 */
fun convertMillisToTime(millis: Long): Pair<Hour, Minute> {
    val hours = (millis / 3.6e6).toInt()
    val minutesMillis = millis - (hours * 3.6e6)
    val minutes = (minutesMillis / 60_000L).roundToInt()
    return Pair(first = hours, second = minutes)
}

/**
 * Convert the provided hours and minutes to milliseconds.
 */
fun convertTimeToMillis(hours: Hour, minutes: Minute): Long {
    val hoursMillis = hours * 3.6e6
    val minutesMillis = minutes * 60_000L
    return (hoursMillis + minutesMillis).roundToLong()
}
