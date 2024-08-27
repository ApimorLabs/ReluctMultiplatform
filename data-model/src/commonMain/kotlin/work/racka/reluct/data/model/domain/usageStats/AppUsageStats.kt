package work.racka.reluct.data.model.domain.usageStats

data class AppUsageStats(
    val appUsageInfo: AppUsageInfo,
    val dateFormatted: String = "...",
    val dayIsoNumber: Int = 0
)