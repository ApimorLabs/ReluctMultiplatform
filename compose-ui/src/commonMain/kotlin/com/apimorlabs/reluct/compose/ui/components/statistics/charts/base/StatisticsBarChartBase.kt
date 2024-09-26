package com.apimorlabs.reluct.compose.ui.components.statistics.charts.base

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.BarChart
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun StatisticsBarChartBase(
    bars: ImmutableList<BarParameters>,
    selectedDayIndex: Int,
    onBarClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedBarColor: Color = MaterialTheme.colorScheme.primary,
) {
    val onColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    val xAxisData by remember { derivedStateOf { bars.map { it.dataName }.toImmutableList() } }
    BarChart(
        modifier = modifier,
        chartParameters = bars,
        gridColor = onColor,
        xAxisData = xAxisData,
        animateChart = true,
        showYAxis = false,
        showYAxisLabels = false,
        yAxisStyle = MaterialTheme.typography.labelMedium.copy(onColor),
        xAxisStyle = MaterialTheme.typography.labelMedium.copy(color = onColor),
        selectedBarColor = selectedBarColor,
        selectedBarIndex = selectedDayIndex,
        barsSpacingFactor = 0.05f,
        barCornerRadius = 10.dp,
        onBarClicked = { index -> onBarClicked(index) }
    )
}
