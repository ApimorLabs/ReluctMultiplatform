package com.apimorlabs.reluct.compose.charts.barChart.ungrouped.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.charts.baseComponets.baseChartContainer
import com.apimorlabs.reluct.compose.charts.baseComponets.xAxisDrawing
import com.apimorlabs.reluct.compose.charts.util.ChartDefaultValues.specialChart
import com.apimorlabs.reluct.compose.charts.util.checkIfDataValid
import com.apimorlabs.reluct.compose.charts.util.formatToThousandsMillionsBillions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun BarChartContent(
    barsParameters: List<BarParameters>,
    gridColor: Color,
    xAxisData: List<String>,
    isShowGrid: Boolean,
    animateChart: Boolean,
    showGridWithSpacer: Boolean,
    yAxisStyle: TextStyle,
    xAxisStyle: TextStyle,
    backgroundLineWidth: Float,
    yAxisRange: Int,
    showXAxis: Boolean,
    showYAxis: Boolean,
    barWidth: Dp,
    spaceBetweenBars: Dp,
    spaceBetweenGroups: Dp,
    barCornerRadius: Dp,
    selectedBarColor: Color,
    selectedBarIndex: Int?,
    onBarClicked: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val textMeasure = rememberTextMeasurer()

    val animatedProgress = remember(barsParameters) {
        if (animateChart) Animatable(0f) else Animatable(1f)
    }
    var upperValue by rememberSaveable {
        mutableStateOf(barsParameters.getUpperValue())
    }
    var lowerValue by rememberSaveable {
        mutableStateOf(barsParameters.getLowerValue())
    }
    var maxWidth by remember { mutableStateOf(0.dp) }
    var yTextLayoutResult by remember { mutableStateOf(0.dp) }
    var maxHeight by remember { mutableStateOf(0f) }
    var xRegionWidthWithoutSpacing by remember { mutableStateOf(0.dp) }
    var xRegionWidth by remember { mutableStateOf(0.dp) }

    //initial height set at 0.dp
    var boxWidth by remember { mutableStateOf(0.dp) }
    var boxHeight by remember { mutableStateOf(0.dp) }

    // Index and Offset of each bar
    val barAreas = remember {
        mutableMapOf<Int, Rect>()
    }

    // get local density from composable
    val density = LocalDensity.current

    checkIfDataValid(xAxisData = xAxisData, barParameters = barsParameters)
    Box(modifier = modifier.fillMaxSize().onGloballyPositioned {
        boxWidth = with(density) {
            it.size.width.toDp()
        }
        boxHeight = with(density) {
            it.size.height.toDp()
        }
    }
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val spacingY = (boxHeight / 10)
            xRegionWidth =
                ((barWidth + spaceBetweenBars) * barsParameters.size) + spaceBetweenGroups
            xRegionWidthWithoutSpacing = xRegionWidth - spaceBetweenGroups
            maxWidth = (xRegionWidth * xAxisData.size) - spaceBetweenGroups
            maxHeight = boxHeight.toPx() - spacingY.toPx() + 10.dp.toPx()

            baseChartContainer(
                xAxisData = xAxisData,
                textMeasure = textMeasure,
                upperValue = upperValue.toFloat(),
                lowerValue = lowerValue.toFloat(),
                isShowGrid = isShowGrid,
                backgroundLineWidth = backgroundLineWidth,
                gridColor = gridColor,
                showGridWithSpacer = showGridWithSpacer,
                spacingY = spacingY,
                yAxisStyle = yAxisStyle,
                xAxisStyle = xAxisStyle,
                yAxisRange = yAxisRange,
                showXAxis = showXAxis,
                showYAxis = showYAxis,
                isFromBarChart = true,
                xRegionWidth = xRegionWidth
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
                .padding(start = yTextLayoutResult + (yTextLayoutResult / 2))
                .horizontalScroll(rememberScrollState())
        ) {

            Canvas(
                modifier = Modifier.width(maxWidth).fillMaxHeight()
                    .pointerInput(Unit) {
                        // Detect taps on bars
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

            ) {
                yTextLayoutResult = textMeasure.measure(
                    text = AnnotatedString(
                        upperValue.toFloat().formatToThousandsMillionsBillions()
                    ),
                ).size.width.toDp()

                drawBarGroups(
                    barsParameters = barsParameters,
                    upperValue = upperValue,
                    barWidth = barWidth,
                    xRegionWidth = xRegionWidth,
                    spaceBetweenBars = spaceBetweenBars,
                    maxWidth = maxWidth,
                    height = maxHeight.dp,
                    animatedProgress = animatedProgress,
                    barCornerRadius = barCornerRadius,
                    selectedBarColor = selectedBarColor,
                    selectedBarIndex = selectedBarIndex,
                    provideBarAreas = { area ->
                        barAreas.apply {
                            clear()
                            putAll(area)
                        }
                    }
                )

                xAxisDrawing(
                    xAxisData = xAxisData,
                    textMeasure = textMeasure,
                    xAxisStyle = xAxisStyle,
                    specialChart = specialChart,
                    xRegionWidth = xRegionWidth,
                    xRegionWidthWithoutSpacing = xRegionWidthWithoutSpacing,
                    height = maxHeight.dp,
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
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
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
    linesParameters: List<BarParameters>, makeUpdateData: (List<BarParameters>) -> Unit
) {
    this.launch {
        snapshotFlow {
            linesParameters
        }.collect {
            makeUpdateData(it)
        }
    }
}
