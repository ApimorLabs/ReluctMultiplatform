package com.apimorlabs.reluct.domain.usecases.time.impl

import com.apimorlabs.reluct.common.models.util.time.StatisticsTimeUtils
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.domain.usecases.time.GetWeekRangeFromOffset
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class GetWeekRangeFromOffsetImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetWeekRangeFromOffset {
    override suspend fun getOffset(weekOffset: Int): String = withContext(dispatcher) {
        // Monday to Sunday
        val weeklyTimeRange = StatisticsTimeUtils
            .weekLocalDateTimeStringRange(weekOffset = weekOffset)

        if (weekOffset == 0) {
            "This Week"
        } else {
            val start = TimeUtils.getFormattedDateString(
                dateTime = weeklyTimeRange.start,
                showShortIntervalAsDay = false
            )
            val end = TimeUtils.getFormattedDateString(
                dateTime = weeklyTimeRange.endInclusive,
                showShortIntervalAsDay = false
            )
            "$start - $end"
        }
    }
}
