package com.apimorlabs.reluct.common.models.domain.goals

import com.apimorlabs.reluct.common.models.util.time.Week
import kotlinx.collections.immutable.ImmutableList

data class GoalDuration(
    val goalInterval: GoalInterval,
    val timeRangeInMillis: LongRange?,
    val formattedTimeRange: ClosedRange<String>?,
    val selectedDaysOfWeek: ImmutableList<Week>
)
