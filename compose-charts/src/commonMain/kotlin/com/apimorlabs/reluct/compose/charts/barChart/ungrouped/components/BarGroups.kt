package com.apimorlabs.reluct.compose.charts.barChart.ungrouped.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters

internal fun DrawScope.drawBarGroups(
    barsParameters: List<BarParameters>,
    upperValue: Double,
    barWidth: Dp,
    xRegionWidth: Dp,
    spaceBetweenBars: Dp,
    maxWidth: Dp,
    height: Dp,
    animatedProgress: Animatable<Float, AnimationVector1D>,
    barCornerRadius: Dp,
    selectedBarColor: Color,
    selectedBarIndex: Int?,
    provideBarAreas: (area: Map<Int, Rect>) -> Unit
) {
    val barAreas = mutableMapOf<Int, Rect>()

    barsParameters.forEachIndexed { barIndex, bar ->
        val ratio = (bar.data.toFloat()) / (upperValue.toFloat())
        val barLength = ((height / 1.02.toFloat().dp).toDp() * animatedProgress.value) * ratio

        // TODO: Check this first barIndex usage
        val xAxisLength = (barIndex * xRegionWidth)
        val lengthWithRatio = xAxisLength + (barIndex * (barWidth + spaceBetweenBars))
        val offset = Offset(
            x = lengthWithRatio.coerceAtMost(maxWidth).toPx(),
            y = height.value - barLength.toPx()
        )
        val size = Size(width = barWidth.toPx(), height = barLength.toPx())

        val barColor = if (barIndex == selectedBarIndex) selectedBarColor else bar.barColor
        drawRoundRect(
            brush = Brush.verticalGradient(listOf(barColor, barColor)),
            topLeft = offset,
            size = size,
            cornerRadius = CornerRadius(barCornerRadius.toPx())
        )

        // Provide Bar Area here
        barAreas[barIndex] = Rect(offset, size)
        // Provide Bar Areas to parents after the last bar has been drawn
        if (barsParameters.lastIndex == barIndex) {
            provideBarAreas(barAreas)
        }
    }
}
