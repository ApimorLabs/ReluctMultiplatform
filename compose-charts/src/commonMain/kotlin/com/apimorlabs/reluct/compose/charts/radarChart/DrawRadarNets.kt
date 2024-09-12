package com.apimorlabs.reluct.compose.charts.radarChart

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.apimorlabs.reluct.compose.charts.radarChart.model.NetLinesStyle
import com.apimorlabs.reluct.compose.charts.radarChart.model.RadarChartConfig

internal fun DrawScope.drawRadarNet(
    netLinesStyle: NetLinesStyle,
    config: RadarChartConfig
) {

    val netCornersPoints = config.netCornersPoints
    val stepsStartPoints = config.stepsStartPoints
    val stepsEndPoints = config.stepsEndPoints

    for (endpoint in netCornersPoints) {
        drawLine(
            color = netLinesStyle.netLineColor,
            start = center,
            end = endpoint,
            strokeWidth = netLinesStyle.netLinesStrokeWidth,
            cap = netLinesStyle.netLinesStrokeCap,
        )

        for (index in stepsStartPoints.indices) {
            drawLine(
                color = netLinesStyle.netLineColor,
                start = stepsStartPoints[index],
                end = stepsEndPoints[index],
                strokeWidth = netLinesStyle.netLinesStrokeWidth,
                cap = netLinesStyle.netLinesStrokeCap,
            )
        }

    }

}
