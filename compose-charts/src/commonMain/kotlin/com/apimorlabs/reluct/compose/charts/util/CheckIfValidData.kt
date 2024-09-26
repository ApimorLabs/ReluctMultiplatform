package com.apimorlabs.reluct.compose.charts.util

import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.charts.barChart.model.GBarParameters
import com.apimorlabs.reluct.compose.charts.lineChart.model.LineParameters

internal fun checkIfDataValid(
    xAxisData: List<String>,
    linesParameters: List<LineParameters> = emptyList(),
    gBarParameters: List<GBarParameters> = emptyList(),
    barParameters: List<BarParameters> = emptyList()
) {
    if (linesParameters.isEmpty()) {
        if (barParameters.isEmpty()) {
            val data = gBarParameters.map { it.data }
            data.forEach {
                check(it.size == xAxisData.size) {
                    "The data size of bar must be equal to the x-axis data size."
                }
                checkIfDataIsNegative(it)
            }
        } else {
            val data = barParameters.map { it.data }
            check(data.size == xAxisData.size) {
                "The data size of bar must be equal to the x-axis data size."
            }
            checkIfDataIsNegative(data)
        }
    } else {
        val data = linesParameters.map { it.data }
        data.forEach {
            check(it.size == xAxisData.size) {
                "The data size of line must be equal to the x-axis data size."
            }
            checkIfDataIsNegative(it)
        }
    }
}

internal fun checkIfDataIsNegative(data: List<Double>) {
    data.forEach {
        check(it >= 0.0) { "The data can't contain negative values." }
    }
}
