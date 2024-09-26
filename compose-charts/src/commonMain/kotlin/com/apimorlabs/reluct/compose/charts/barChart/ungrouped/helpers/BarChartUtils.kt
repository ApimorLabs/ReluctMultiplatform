package com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters

internal object BarChartUtils {
    fun DrawScope.axisAreas(
        totalSize: Size,
        withYAxisLabels: Boolean,
        xAxisDrawer: XAxisDrawer,
        labelDrawer: LabelValueDrawer,
    ): Pair<Rect, Rect> {
        // yAxis
        val yAxisTop = labelDrawer.requiredAboveBarHeight(this)

        // Either 50dp or 10% of the chart width.
        val yAxisRight = if (withYAxisLabels) minOf(50.dp.toPx(), size.width * 10f / 100f) else 0f

        // xAxis
        val xAxisRight = totalSize.width

        // Measure the size of the text and line.
        val xAxisTop = totalSize.height - xAxisDrawer.requiredHeight(this)

        return Pair(
            Rect(yAxisRight, xAxisTop, xAxisRight, totalSize.height),
            Rect(0f, yAxisTop, yAxisRight, xAxisTop)
        )
    }

    fun barDrawableArea(xAxisArea: Rect): Rect {
        return Rect(
            left = xAxisArea.left,
            top = 0f,
            right = xAxisArea.right,
            bottom = xAxisArea.top
        )
    }

    fun List<BarParameters>.forEachWithArea(
        drawScope: DrawScope,
        barDrawableArea: Rect,
        progress: Float,
        labelDrawer: LabelValueDrawer,
        barsSpacingFactor: Float,
        block: (barArea: Rect, bar: BarParameters, index: Int) -> Unit,
    ) {
        require(barsSpacingFactor in 0f..1f)
        val totalBars = this.size
        val widthOfBarArea = barDrawableArea.width / totalBars
        val offsetOfBar = widthOfBarArea * barsSpacingFactor
        // Gets `this` context from List<BarParameters>
        val maxBarValue = maxByOrNull { it.data }?.data ?: 0.0

        // Gets `this` context from List<BarParameters>
        forEachIndexed { index, bar ->
            val left = barDrawableArea.left + (index * widthOfBarArea)
            val height = barDrawableArea.height

            val barHeight = (height - labelDrawer.requiredAboveBarHeight(drawScope)) * progress

            val barArea = Rect(
                left = left + offsetOfBar,
                top = (barDrawableArea.bottom - (bar.data / maxBarValue) * barHeight).toFloat(),
                right = left + widthOfBarArea - offsetOfBar,
                bottom = barDrawableArea.bottom
            )

            block(barArea, bar, index)
        }
    }
}
