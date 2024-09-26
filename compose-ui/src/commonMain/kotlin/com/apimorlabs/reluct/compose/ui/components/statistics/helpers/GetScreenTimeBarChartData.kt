package com.apimorlabs.reluct.compose.ui.components.statistics.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageStats
import com.apimorlabs.reluct.common.models.domain.usageStats.UsageStats
import com.apimorlabs.reluct.common.models.util.time.Week
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.ui.components.statistics.ChartData
import com.apimorlabs.reluct.compose.ui.components.statistics.StatsDispatcher
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.withContext

@Composable
fun getWeeklyDeviceScreenTimeChartData(
    weeklyStatsProvider: () -> ImmutableMap<Week, UsageStats>,
    isLoadingProvider: () -> Boolean,
    barColor: Color
): State<ChartData<BarParameters>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val weeklyStats = remember { derivedStateOf { weeklyStatsProvider() } }
    return produceState(
        initialValue = ChartData(isLoading = isLoading.value),
        isLoading.value,
        weeklyStats.value
    ) {
        val data = withContext(StatsDispatcher.Dispatcher) {
            persistentListOf<BarParameters>().builder().apply {
                for (key in weeklyStats.value.keys) {
                    val data = weeklyStats.value[key]
                    add(
                        BarParameters(
                            data = data?.totalScreenTime?.toDouble() ?: 0.0,
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

@Composable
fun getWeeklyAppScreenTimeChartData(
    weeklyStatsProvider: () -> ImmutableMap<Week, AppUsageStats>,
    isLoadingProvider: () -> Boolean,
    barColor: Color,
): State<ChartData<BarParameters>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val weeklyStats = remember { derivedStateOf { weeklyStatsProvider() } }
    return produceState(
        initialValue = ChartData(isLoading = isLoading.value),
        isLoading.value,
        weeklyStats.value
    ) {
        val data = withContext(StatsDispatcher.Dispatcher) {
            persistentListOf<BarParameters>().builder().apply {
                for (key in weeklyStats.value.keys) {
                    val data = weeklyStats.value[key]
                    add(
                        BarParameters(
                            data = data?.appUsageInfo?.timeInForeground?.toDouble() ?: 0.0,
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
