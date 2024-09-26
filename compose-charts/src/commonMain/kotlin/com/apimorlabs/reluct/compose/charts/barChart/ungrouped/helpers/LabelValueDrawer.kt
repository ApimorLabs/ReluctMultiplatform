package com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText

class LabelValueDrawer(
    private val drawLocation: DrawLocation = DrawLocation.XAxis,
    private val textMeasure: TextMeasurer,
    private val labelStyle: TextStyle
) {
    private val _labelTextArea: Float? = null

    fun requiredAboveBarHeight(drawScope: DrawScope): Float = when (drawLocation) {
        DrawLocation.Outside -> (3f / 2f) * drawScope.labelTextHeight()
        DrawLocation.Inside,
        DrawLocation.XAxis -> 0f
    }

    fun requiredXAxisHeight(drawScope: DrawScope): Float = when (drawLocation) {
        DrawLocation.XAxis -> drawScope.labelTextHeight()
        DrawLocation.Inside,
        DrawLocation.Outside -> 0f
    }

    fun drawLabel(
        drawScope: DrawScope,
        label: String,
        barArea: Rect,
    ) = with(drawScope) {
        val text = textMeasure.measure(
            text = label,
            style = labelStyle,
            maxLines = 1
        )

        val barCenter = (barArea.left + barArea.right) / 2
        val textCenter = text.size.width / 2
        val xCenter = barCenter - textCenter

        val yCenter = when (drawLocation) {
            DrawLocation.Inside -> (barArea.top + barArea.bottom) / 2
            DrawLocation.Outside -> (barArea.top) - text.size.height / 2
            DrawLocation.XAxis -> barArea.bottom + text.size.height
        }

        drawText(
            textLayoutResult = text,
            topLeft = Offset(
                xCenter.coerceAtMost(size.width),
                yCenter.coerceAtMost(size.height)
            )
        )
    }

    private fun DrawScope.labelTextHeight() =
        _labelTextArea ?: ((3f / 2f) * labelStyle.fontSize.toPx())

    enum class DrawLocation {
        Inside,
        Outside,
        XAxis
    }
}
