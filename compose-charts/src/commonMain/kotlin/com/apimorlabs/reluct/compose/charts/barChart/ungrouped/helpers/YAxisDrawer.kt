package com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.apimorlabs.reluct.compose.charts.util.formatToThousandsMillionsBillions
import kotlin.math.max
import kotlin.math.roundToInt

internal class YAxisDrawer(
    private val labelRatio: Int = 3,
    private val textMeasure: TextMeasurer,
    private val yAxisStyle: TextStyle,
    private val axisLineThickness: Dp = 1.dp,
    private val axisLineColor: Color = Color.Black
) {
    private val axisLinePaint = Paint().apply {
        isAntiAlias = true
        color = axisLineColor
        style = PaintingStyle.Stroke
    }

    fun drawAxisLine(
        drawScope: DrawScope,
        drawableArea: Rect
    ) = with(drawScope) {
        val lineThickness = axisLineThickness.toPx()
        val x = drawableArea.right - (lineThickness / 2f)

        drawContext.canvas.drawLine(
            p1 = Offset(
                x = x,
                y = drawableArea.top
            ),
            p2 = Offset(
                x = x,
                y = drawableArea.bottom
            ),
            paint = axisLinePaint.apply {
                strokeWidth = lineThickness
            }
        )
    }

    fun drawAxisLabels(
        drawScope: DrawScope,
        minValue: Double,
        maxValue: Double,
        spacing: Dp
    ) = with(drawScope) {
        val minLabelHeight = (yAxisStyle.fontSize.toPx() * labelRatio.toFloat())
        val labelCount = max((size.height / minLabelHeight).roundToInt(), 2)

        for (i in 0..labelCount) {
            val value = minValue + (i * ((maxValue - minValue) / labelCount))

            val y = (size.height.toDp() - spacing - i * (size.height.toDp() - spacing) / labelCount)

            drawText(
                textMeasurer = textMeasure,
                text = value.toFloat().formatToThousandsMillionsBillions(),
                style = yAxisStyle,
                topLeft = Offset(0f, y.toPx())
            )
        }
    }
}
