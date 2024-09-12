package com.apimorlabs.reluct.compose.charts.radarChart.model

data class Polygon(
    val style: PolygonStyle,
    val values: List<Double>,
    val unit: String,
)