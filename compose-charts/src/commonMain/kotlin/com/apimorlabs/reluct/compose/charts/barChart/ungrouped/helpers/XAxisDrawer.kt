package com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

internal class XAxisDrawer(
    private val labelRatio: Int = 3,
    private val xAxisStyle: TextStyle,
    private val axisLineThickness: Dp = 1.dp,
    private val axisLineColor: Color = Color.Black
) {
    private val bounds = Rect.Zero

    private val axisLinePaint = Paint().apply {
        isAntiAlias = true
        color = axisLineColor
        style = PaintingStyle.Stroke
    }

    fun requiredHeight(drawScope: DrawScope): Float =
        with(drawScope) { (3f / 2f) * axisLineThickness.toPx() }

    fun drawAxisLine(drawScope: DrawScope, drawableArea: Rect) = with(drawScope) {
        val lineThickness = axisLineThickness.toPx()
        val y = drawableArea.top + (lineThickness / 2f)

        drawContext.canvas.drawLine(
            p1 = Offset(
                x = drawableArea.left,
                y = y
            ),
            p2 = Offset(
                x = drawableArea.right,
                y = y
            ),
            paint = axisLinePaint.apply {
                strokeWidth = lineThickness
            }
        )
    }

    fun drawIntervalLines(drawScope: DrawScope, drawableArea: Rect) = with(drawScope) {
        val minLabelHeight = (xAxisStyle.fontSize.toPx() * labelRatio.toFloat())
        val totalHeight = drawableArea.height
        val labelCount = max((drawableArea.height / minLabelHeight).roundToInt(), 2)
        val lineThickness = axisLineThickness.toPx()

        for (i in 1..labelCount) {
            val y =
                drawableArea.bottom - (i * (totalHeight / labelCount)) + (bounds.height / 2f)

            drawContext.canvas.drawLine(
                p1 = Offset(
                    x = drawableArea.left,
                    y = y
                ),
                p2 = Offset(
                    x = drawableArea.right,
                    y = y
                ),
                paint = axisLinePaint.apply {
                    strokeWidth = lineThickness
                }
            )
        }
    }
}
