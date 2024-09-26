package com.apimorlabs.reluct.compose.charts.donutChart.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.style.TextOverflow

internal fun DrawScope.ratioText(
    textMeasurer: TextMeasurer,
    ratioText: String,
    textRatioStyle: TextStyle,
    topLeft: Offset,
) {
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            textMeasurer = textMeasurer,
            text = ratioText,
            style = textRatioStyle,
            topLeft = topLeft,
            overflow = TextOverflow.Visible
        )
    }
}
