package com.apimorlabs.reluct.compose.ui.components.statistics.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageInfo
import com.apimorlabs.reluct.compose.charts.donutChart.model.PieChartData
import com.apimorlabs.reluct.compose.ui.components.statistics.ChartData
import com.apimorlabs.reluct.compose.ui.components.statistics.StatsDispatcher
import com.kmpalette.loader.ByteArrayLoader
import com.kmpalette.rememberDominantColorState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.withContext

@Composable
internal fun getScreenTimePieChartData(
    appsUsageProvider: () -> ImmutableList<AppUsageInfo>,
    isLoadingProvider: () -> Boolean
): State<ChartData<PieChartData>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val appsUsage = remember { derivedStateOf { appsUsageProvider() } }
    val dominantColorState = rememberDominantColorState(
        loader = ByteArrayLoader,
        defaultColor = Color.Gray,
        defaultOnColor = Color.Black
    ) { clearFilters() }

    return produceState(
        initialValue = ChartData(isLoading = isLoading.value),
        isLoading.value,
        appsUsage.value,
    ) {
        val data = withContext(StatsDispatcher.Dispatcher) {
            persistentListOf<PieChartData>().builder().apply {
                val firstItems = appsUsage.value.take(4)
                val otherItems = appsUsage.value - firstItems.toSet()
                val otherSlice = PieChartData(
                    partName = "Other",
                    data = otherItems.sumOf { it.timeInForeground }.toDouble(),
                    color = Color.Gray
                )
                for (i in firstItems.indices) {
                    val data = firstItems[i]
                    dominantColorState.updateFrom(data.appIcon.icon)
                    val slice = PieChartData(
                        partName = data.appName,
                        data = data.timeInForeground.toDouble(),
                        color = dominantColorState.color
                    )
                    add(slice)
                }
                add(otherSlice)
            }.build().toImmutableList()
        }
        value = ChartData(data = data, isLoading = isLoading.value)
    }
}
