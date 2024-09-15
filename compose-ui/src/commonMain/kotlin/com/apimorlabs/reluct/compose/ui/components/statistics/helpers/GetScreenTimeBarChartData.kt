package com.apimorlabs.reluct.compose.ui.components.statistics.helpers

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.apimorlabs.reluct.common.models.domain.usageStats.UsageStats
import com.apimorlabs.reluct.common.models.util.time.Week
import com.apimorlabs.reluct.compose.charts.barChart.model.GBarParameters
import com.apimorlabs.reluct.compose.ui.components.statistics.ChartData
import com.apimorlabs.reluct.compose.ui.components.statistics.StatsDispatcher
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.withContext
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.compose.common.charts.barChart.BarChartData

@Composable
fun getWeeklyDeviceScreenTimeChartData(
    weeklyStatsProvider: () -> ImmutableMap<Week, UsageStats>,
    isLoadingProvider: () -> Boolean,
    barColor: Color
): State<ChartData<GBarParameters>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val weeklyStats = remember { derivedStateOf { weeklyStatsProvider() } }
    return produceState(
        initialValue = ChartData(isLoading = isLoading.value),
        isLoading.value,
        weeklyStats.value
    ) {
        val data = withContext(StatsDispatcher.Dispatcher) {
            persistentListOf<GBarParameters>().builder().apply {
                for (key in weeklyStats.value.keys) {
                    val data = weeklyStats.value[key]
                    add(
                        GBarParameters(
                            data = listOf(data?.totalScreenTime?.toDouble() ?: 0.0),
                            barColor = barColor,
                            dataName = key.dayAcronym,
                            uniqueId = key.isoDayNumber
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
): State<ChartData<BarChartData.Bar>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val weeklyStats = remember { derivedStateOf { weeklyStatsProvider() } }
    return produceState(
        initialValue = ChartData(isLoading = isLoading.value),
        isLoading.value,
        weeklyStats.value
    ) {
        val data = withContext(StatsDispatcher.Dispatcher) {
            persistentListOf<BarChartData.Bar>().builder().apply {
                for (key in weeklyStats.value.keys) {
                    val data = weeklyStats.value[key]
                    add(
                        BarChartData.Bar(
                            value = data?.appUsageInfo?.timeInForeground?.toFloat() ?: 0f,
                            color = barColor,
                            label = key.dayAcronym,
                            uniqueId = key.isoDayNumber
                        )
                    )
                }
            }.build().toImmutableList()
        }
        value = ChartData(data = data, isLoading = isLoading.value)
    }
}
