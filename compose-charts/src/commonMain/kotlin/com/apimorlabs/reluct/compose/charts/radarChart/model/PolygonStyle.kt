package com.apimorlabs.reluct.compose.charts.radarChart.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap

data class PolygonStyle(
    val fillColor: Color,
    val fillColorAlpha: Float,
    val borderColor: Color,
    val borderColorAlpha: Float,
    val borderStrokeWidth: Float,
    val borderStrokeCap: StrokeCap,
)
