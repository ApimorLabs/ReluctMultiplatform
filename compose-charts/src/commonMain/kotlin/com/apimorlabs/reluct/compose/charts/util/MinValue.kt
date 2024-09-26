package com.apimorlabs.reluct.compose.charts.util

import androidx.compose.ui.geometry.Size
import com.apimorlabs.reluct.compose.charts.donutChart.model.ChartLabelType
import kotlin.math.min

internal fun getPieChartMinValue(chartLabelType: ChartLabelType, canvasSize: Size): Float =
    if (chartLabelType != ChartLabelType.NONE) {
        min(canvasSize.width, canvasSize.height)
            .coerceAtMost(canvasSize.height / 2)
            .coerceAtMost(canvasSize.width / 2)
    } else {
        min(canvasSize.width, canvasSize.height)
            .coerceAtMost(canvasSize.height / 1.4f)
            .coerceAtMost(canvasSize.width / 1.4f)
    }