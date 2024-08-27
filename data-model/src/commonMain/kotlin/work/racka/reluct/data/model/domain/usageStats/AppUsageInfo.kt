package work.racka.reluct.data.model.domain.usageStats

import javax.swing.Icon

data class AppUsageInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Icon,
    val timeInForeground: Long = 0,
    val formattedTimeInForeground: String = "",
    val appLaunchCount: Int = 0,
)
