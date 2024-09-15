package com.apimorlabs.reluct.compose.charts.barChart.model

import androidx.compose.ui.graphics.Color

data class GBarParameters(
    val dataName: String,
    val data: List<Double>,
    val barColor: Color,
)
