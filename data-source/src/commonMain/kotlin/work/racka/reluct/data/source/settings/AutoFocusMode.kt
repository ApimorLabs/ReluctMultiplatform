package work.racka.reluct.data.source.settings

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class AutoFocusMode(
    val enabled: Boolean,
    val timeRange: ClosedRange<LocalTime>
)
