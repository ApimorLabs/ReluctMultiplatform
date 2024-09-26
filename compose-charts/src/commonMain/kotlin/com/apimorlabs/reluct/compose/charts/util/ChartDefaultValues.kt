package com.apimorlabs.reluct.compose.charts.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.charts.barChart.model.GBarParameters
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers.LabelValueDrawer
import com.apimorlabs.reluct.compose.charts.baseComponets.model.GridOrientation
import com.apimorlabs.reluct.compose.charts.baseComponets.model.LegendPosition
import com.apimorlabs.reluct.compose.charts.donutChart.model.ChartLabelType
import com.apimorlabs.reluct.compose.charts.lineChart.model.LineParameters
import com.apimorlabs.reluct.compose.charts.lineChart.model.LineType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal object ChartDefaultValues {

    val lineParameters: ImmutableList<LineParameters> = listOf(
        LineParameters(
            label = "revenue",
            data = emptyList(),
            lineColor = Color.Blue,
            lineType = LineType.CURVED_LINE,
            lineShadow = true,
        )
    ).toImmutableList()

    val gBarParameters: ImmutableList<GBarParameters> = listOf(
        GBarParameters(
            dataName = "revenue",
            data = emptyList(),
            barColor = Color.Blue,
        )
    ).toImmutableList()

    val barParameters: ImmutableList<BarParameters> = listOf(
        BarParameters(
            dataName = "revenue",
            data = 0.0,
            barColor = Color.Blue,
        )
    ).toImmutableList()

    val barWidth = 30.dp
    val spaceBetweenBars = 10.dp
    const val barsSpacingFactor = 0.1f
    val spaceBetweenGroups = 40.dp
    const val IS_SHOW_GRID = true
    val gridColor = Color.Gray
    val selectedBarColor = Color.Blue
    const val ANIMATED_CHART = true
    val backgroundLineWidth = 1.dp
    const val SHOW_BACKGROUND_WITH_SPACER = true
    const val chartRatio = 0f
    val descriptionDefaultStyle = TextStyle(
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.W400
    )

    val headerArrangement = Arrangement.spacedBy(24.dp)
    val axesStyle = TextStyle(
        fontSize = 12.sp,
        color = Color.Gray,
    )
    const val yAxisRange = 6
    const val specialChart = false
    const val showXAxis = true
    const val showyAxis = true
    const val showXAxisLabel = true
    const val showYAxisLabel = true
    const val showIntervalLines = false
    val xAxisDrawLocation = LabelValueDrawer.DrawLocation.XAxis

    val gridOrientation = GridOrientation.HORIZONTAL
    val legendPosition = LegendPosition.DISAPPEAR
    val chartLabelType = ChartLabelType.PERCENTAGE
    val barCornerRadius = 10.dp
}
