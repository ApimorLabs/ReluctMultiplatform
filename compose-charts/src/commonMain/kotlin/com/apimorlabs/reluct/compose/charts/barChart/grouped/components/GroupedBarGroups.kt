package com.apimorlabs.reluct.compose.charts.barChart.grouped.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.apimorlabs.reluct.compose.charts.barChart.model.GBarParameters

internal fun DrawScope.drawGroupedBarGroups(
    barsParameters: List<GBarParameters>,
    upperValue: Double,
    barWidth: Dp,
    xRegionWidth: Dp,
    spaceBetweenBars: Dp,
    maxWidth: Dp,
    height: Dp,
    animatedProgress: Animatable<Float, AnimationVector1D>,
    barCornerRadius: Dp
) {

    barsParameters.forEachIndexed { barIndex, bar ->

        bar.data.forEachIndexed { index, data ->
            val ratio = (data.toFloat()) / (upperValue.toFloat())
            val barLength = ((height / 1.02.toFloat().dp).toDp() * animatedProgress.value) * ratio

            val xAxisLength = (index * xRegionWidth)
            val lengthWithRatio = xAxisLength + (barIndex * (barWidth + spaceBetweenBars))

            drawRoundRect(
                brush = Brush.verticalGradient(listOf(bar.barColor, bar.barColor)),
                topLeft = Offset(
                    lengthWithRatio.coerceAtMost(maxWidth).toPx(),
                    height.value - barLength.toPx()
                ),
                size = Size(
                    width = barWidth.toPx(),
                    height = barLength.toPx()
                ),
                cornerRadius = CornerRadius(barCornerRadius.toPx())
            )
        }
    }
}
