package com.apimorlabs.reluct.domain.usecases.tasks

import com.apimorlabs.reluct.common.models.domain.tasks.DailyTasksStats
import com.apimorlabs.reluct.common.models.util.time.Week
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow

interface GetGroupedTasksStats {
    fun dailyTasks(weekOffset: Int = 0, dayIsoNumber: Int): Flow<DailyTasksStats>
    fun weeklyTasks(weekOffset: Int = 0): Flow<ImmutableMap<Week, DailyTasksStats>>
    fun timeRangeTasks(timeRangeMillis: LongRange): Flow<DailyTasksStats>
}
