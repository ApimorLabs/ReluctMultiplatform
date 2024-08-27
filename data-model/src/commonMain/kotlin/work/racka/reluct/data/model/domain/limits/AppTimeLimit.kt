package work.racka.reluct.data.model.domain.limits

import work.racka.reluct.data.model.domain.appInfo.AppInfo

data class AppTimeLimit(
    val appInfo: AppInfo,
    val timeInMillis: Long,
    val hours: Int,
    val minutes: Int,
    val formattedTime: String
)
