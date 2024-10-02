package com.apimorlabs.reluct.screens.tasks.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.apimorlabs.reluct.common.models.domain.tasks.DailyTasksStats
import com.apimorlabs.reluct.common.models.util.time.Week
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.ui.components.statistics.ChartData
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
internal fun getWeeklyTasksBarChartData(
    weeklyTasksProvider: () -> ImmutableMap<Week, DailyTasksStats>,
    isLoadingProvider: () -> Boolean,
    barColor: Color
): State<ChartData<BarParameters>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val weeklyStats = remember { derivedStateOf { weeklyTasksProvider() } }
    return produceState(
        initialValue = ChartData(isLoading = isLoading.value),
        isLoading.value,
        weeklyStats.value
    ) {
        val data = withContext(Dispatchers.IO) {
            persistentListOf<BarParameters>().builder().apply {
                for (key in weeklyStats.value.keys) {
                    val data = weeklyStats.value[key]
                    add(
                        BarParameters(
                            data = data?.completedTasksCount?.toDouble() ?: 0.0,
                            barColor = barColor,
                            dataName = key.dayAcronym,
                        )
                    )
                }
            }.build().toImmutableList()
        }
        value = ChartData(data = data, isLoading = isLoading.value)
    }
}
