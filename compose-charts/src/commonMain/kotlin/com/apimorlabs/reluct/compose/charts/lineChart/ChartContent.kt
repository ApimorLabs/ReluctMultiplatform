package com.apimorlabs.reluct.compose.charts.lineChart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.baseComponets.baseChartContainer
import com.apimorlabs.reluct.compose.charts.baseComponets.model.GridOrientation
import com.apimorlabs.reluct.compose.charts.lineChart.components.drawDefaultLineWithShadow
import com.apimorlabs.reluct.compose.charts.lineChart.components.drawQuarticLineWithShadow
import com.apimorlabs.reluct.compose.charts.lineChart.model.LineParameters
import com.apimorlabs.reluct.compose.charts.lineChart.model.LineType
import com.apimorlabs.reluct.compose.charts.util.checkIfDataValid
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun ChartContent(
    linesParameters: ImmutableList<LineParameters>,
    gridColor: Color,
    xAxisData: ImmutableList<String>,
    isShowGrid: Boolean,
    barWidthPx: Dp,
    animateChart: Boolean,
    showGridWithSpacer: Boolean,
    yAxisStyle: TextStyle,
    xAxisStyle: TextStyle,
    yAxisRange: Int,
    showXAxis: Boolean,
    showYAxis: Boolean,
    specialChart: Boolean,
    gridOrientation: GridOrientation,
    modifier: Modifier = Modifier,
) {
    val textMeasure = rememberTextMeasurer()

    val clickedPoints = remember { mutableStateListOf<Pair<Float, Float>>() }

    val animatedProgress = remember {
        if (animateChart) Animatable(0f) else Animatable(1f)
    }
    var upperValue by rememberSaveable {
        mutableStateOf(linesParameters.getUpperValue())
    }
    var lowerValue by rememberSaveable {
        mutableStateOf(linesParameters.getLowerValue())
    }
    checkIfDataValid(xAxisData = xAxisData, linesParameters = linesParameters)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    clickedPoints.add(offset.x to offset.y)
                }
            }
    ) {
        val textLayoutResult = textMeasure.measure(
            text = AnnotatedString(xAxisData.last().toString()),
        ).size.width
        val spacingX = (size.width / 50.dp.toPx()).dp
        val spacingY = (size.height / 8.dp.toPx()).dp
        val xRegionWidth =
            (size.width.toDp() / (xAxisData.size - 1).toDp()).toDp() - (textLayoutResult.toDp() / 2)

        baseChartContainer(
            xAxisData = xAxisData,
            textMeasure = textMeasure,
            upperValue = upperValue.toFloat(),
            lowerValue = lowerValue.toFloat(),
            isShowGrid = isShowGrid,
            backgroundLineWidth = barWidthPx.toPx(),
            gridColor = gridColor,
            showGridWithSpacer = showGridWithSpacer,
            spacingY = spacingY,
            yAxisStyle = yAxisStyle,
            xAxisStyle = xAxisStyle,
            yAxisRange = yAxisRange,
            showXAxis = showXAxis,
            showYAxis = showYAxis,
            specialChart = specialChart,
            isFromBarChart = false,
            gridOrientation = gridOrientation,
            xRegionWidth = xRegionWidth
        )

        if (specialChart) {
            check(linesParameters.size < 2) { "Special case must contain just one line" }

            linesParameters.forEach { line ->
                drawQuarticLineWithShadow(
                    line = line,
                    lowerValue = lowerValue.toFloat(),
                    upperValue = upperValue.toFloat(),
                    animatedProgress = animatedProgress,
                    spacingX = spacingX,
                    spacingY = spacingY,
                    specialChart = true,
                    clickedPoints = clickedPoints,
                    xRegionWidth = xRegionWidth,
                    textMeasure
                )
            }
        } else {
            if (linesParameters.size >= 2) {
                clickedPoints.clear()
            }
            linesParameters.forEach { line ->
                if (line.lineType == LineType.DEFAULT_LINE) {
                    drawDefaultLineWithShadow(
                        line = line,
                        lowerValue = lowerValue.toFloat(),
                        upperValue = upperValue.toFloat(),
                        animatedProgress = animatedProgress,
                        spacingX = spacingX,
                        spacingY = spacingY,
                        clickedPoints = clickedPoints,
                        textMeasure = textMeasure,
                        xRegionWidth = xRegionWidth
                    )
                } else {
                    drawQuarticLineWithShadow(
                        line = line,
                        lowerValue = lowerValue.toFloat(),
                        upperValue = upperValue.toFloat(),
                        animatedProgress = animatedProgress,
                        spacingX = spacingX,
                        spacingY = spacingY,
                        specialChart = false,
                        clickedPoints = clickedPoints,
                        xRegionWidth = xRegionWidth,
                        textMeasure
                    )
                }
            }
        }
    }

    LaunchedEffect(linesParameters, animateChart) {
        if (animateChart) {
            collectToSnapShotFlow(linesParameters) {
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

private fun List<LineParameters>.getUpperValue(): Double {
    return this.flatMap { item -> item.data }.maxOrNull()?.plus(1.0) ?: 0.0
}

private fun List<LineParameters>.getLowerValue(): Double {
    return this.flatMap { item -> item.data }.minOrNull() ?: 0.0
}

private fun CoroutineScope.collectToSnapShotFlow(
    linesParameters: List<LineParameters>,
    makeUpdateData: (List<LineParameters>) -> Unit,
) {
    this.launch {
        snapshotFlow {
            linesParameters
        }.collect {
            makeUpdateData(it)
        }
    }
}
