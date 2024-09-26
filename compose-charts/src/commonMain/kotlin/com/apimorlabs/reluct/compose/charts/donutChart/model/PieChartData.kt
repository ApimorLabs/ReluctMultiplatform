package com.apimorlabs.reluct.compose.charts.donutChart.model

import androidx.compose.ui.graphics.Color

data class PieChartData(
    val data: Double,
    val color: Color,
    val partName: String
)
