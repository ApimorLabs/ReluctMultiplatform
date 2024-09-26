package com.apimorlabs.reluct.compose.charts.barChart.ungrouped.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers.BarChartUtils.axisAreas
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers.BarChartUtils.barDrawableArea
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers.BarChartUtils.forEachWithArea
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers.BarDrawer
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers.LabelValueDrawer
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers.XAxisDrawer
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers.YAxisDrawer
import com.apimorlabs.reluct.compose.charts.util.checkIfDataValid
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun BarChartContent(
    barsParameters: ImmutableList<BarParameters>,
    gridColor: Color,
    xAxisData: ImmutableList<String>,
    animateChart: Boolean,
    yAxisStyle: TextStyle,
    xAxisStyle: TextStyle,
    backgroundLineWidth: Float,
    showIntervalLines: Boolean,
    showXAxis: Boolean,
    showYAxis: Boolean,
    showXAxisLabels: Boolean,
    showYAxisLabels: Boolean,
    xAxisLabelDrawLocation: LabelValueDrawer.DrawLocation,
    barsSpacingFactor: Float,
    barCornerRadius: Dp,
    selectedBarColor: Color,
    selectedBarIndex: Int?,
    onBarClicked: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val xAxisTextMeasure = rememberTextMeasurer()
    val yAxisTextMeasure = rememberTextMeasurer()

    val animatedProgress = remember(barsParameters) {
        if (animateChart) Animatable(0f) else Animatable(1f)
    }
    var upperValue by rememberSaveable {
        mutableStateOf(barsParameters.getUpperValue())
    }
    var lowerValue by rememberSaveable {
        mutableStateOf(barsParameters.getLowerValue())
    }

    // Index and Offset of each bar
    val barAreas = remember(barsParameters) {
        mutableMapOf<Int, Rect>()
    }

    // Drawers
    val xAxisDrawer = remember(gridColor, backgroundLineWidth) {
        XAxisDrawer(
            axisLineColor = gridColor,
            axisLineThickness = backgroundLineWidth.dp,
            xAxisStyle = xAxisStyle,
        )
    }
    val labelDrawer = remember(xAxisLabelDrawLocation, xAxisTextMeasure) {
        LabelValueDrawer(
            drawLocation = xAxisLabelDrawLocation,
            textMeasure = xAxisTextMeasure,
            labelStyle = xAxisStyle,
        )
    }
    val yAxisDrawer = remember(gridColor, backgroundLineWidth, yAxisTextMeasure) {
        YAxisDrawer(
            textMeasure = yAxisTextMeasure,
            yAxisStyle = yAxisStyle,
            axisLineThickness = backgroundLineWidth.dp,
            axisLineColor = gridColor
        )
    }
    val barDrawer = remember(barCornerRadius) {
        BarDrawer(cornerRadius = barCornerRadius)
    }

    checkIfDataValid(xAxisData = xAxisData, barParameters = barsParameters)
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            barAreas.forEach { (index, rect) ->
                                if (rect.contains(offset)) {
                                    onBarClicked(index)
                                }
                            }
                        }
                    )
                }
                .drawBehind {
                    val (xAxisArea, yAxisArea) = axisAreas(
                        totalSize = size,
                        withYAxisLabels = showYAxisLabels,
                        xAxisDrawer = xAxisDrawer,
                        labelDrawer = labelDrawer
                    )
                    val barDrawableArea = barDrawableArea(xAxisArea)

                    // Draw yAxis line.
                    if (showYAxis) {
                        yAxisDrawer
                            .drawAxisLine(
                                drawScope = this,
                                drawableArea = yAxisArea
                            )
                    }

                    // Draw Interval lines
                    if (showIntervalLines) {
                        xAxisDrawer.drawIntervalLines(
                            drawScope = this,
                            drawableArea = barDrawableArea
                        )
                    }

                    // Draw xAxis line.
                    if (showXAxis) {
                        xAxisDrawer.drawAxisLine(
                            drawScope = this,
                            drawableArea = xAxisArea
                        )
                    }

                    // Draw each bar.
                    barsParameters.forEachWithArea(
                        this,
                        barDrawableArea,
                        animatedProgress.value,
                        labelDrawer,
                        barsSpacingFactor
                    ) { barArea, bar, index ->
                        barAreas[index] = barArea
                        barDrawer.drawBar(
                            drawScope = this,
                            barArea = barArea,
                            bar = bar,
                            selected = (index == selectedBarIndex),
                            selectedBarColor = selectedBarColor
                        )
                    }
                }
        ) {
            /**
             *  Typically we could draw everything here, but because of the lack of canvas.drawText
             *  APIs we have to use Android's `nativeCanvas` which seems to be drawn behind
             *  Compose canvas.
             */
                val (xAxisArea, yAxisArea) = axisAreas(
                totalSize = size,
                withYAxisLabels = showYAxisLabels,
                xAxisDrawer = xAxisDrawer,
                labelDrawer = labelDrawer
            )
            val barDrawableArea = barDrawableArea(xAxisArea)
            val spacingY = yAxisArea.height.toDp() / 10

            if (showXAxisLabels) {
                barsParameters.forEachWithArea(
                    this,
                    barDrawableArea,
                    animatedProgress.value,
                    labelDrawer,
                    barsSpacingFactor
                ) { barArea, bar, _ ->
                    labelDrawer.drawLabel(
                        drawScope = this,
                        label = bar.dataName,
                        barArea = barArea,
                    )
                }
            }

            if (showYAxisLabels) {
                yAxisDrawer.drawAxisLabels(
                    drawScope = this,
                    minValue = lowerValue,
                    maxValue = upperValue,
                    spacing = spacingY
                )
            }
        }
    }

    LaunchedEffect(barsParameters, animateChart) {
        if (animateChart) {
            collectToSnapShotFlow(barsParameters) {
                upperValue = it.getUpperValue()
                lowerValue = it.getLowerValue()
            }

            delay(400)
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500, easing = LinearEasing)
            )
        }
    }
}

private fun List<BarParameters>.getUpperValue(): Double {
    return this.maxOfOrNull { item -> item.data }?.plus(1.0) ?: 0.0
}

private fun List<BarParameters>.getLowerValue(): Double {
    return this.minOfOrNull { item -> item.data } ?: 0.0
}

private fun CoroutineScope.collectToSnapShotFlow(
    linesParameters: List<BarParameters>,
    makeUpdateData: (List<BarParameters>) -> Unit
) {
    this.launch {
        snapshotFlow {
            linesParameters
        }.collect {
            makeUpdateData(it)
        }
    }
}
