package com.apimorlabs.reluct.compose.charts.donutChart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apimorlabs.reluct.compose.charts.baseComponets.model.LegendPosition
import com.apimorlabs.reluct.compose.charts.donutChart.components.PieChartDescriptionComposable
import com.apimorlabs.reluct.compose.charts.donutChart.components.draPieCircle
import com.apimorlabs.reluct.compose.charts.donutChart.components.drawCenterText
import com.apimorlabs.reluct.compose.charts.donutChart.components.drawPedigreeChart
import com.apimorlabs.reluct.compose.charts.donutChart.model.ChartTypes
import com.apimorlabs.reluct.compose.charts.donutChart.model.PieChartData
import com.apimorlabs.reluct.compose.charts.util.ChartDefaultValues
import com.apimorlabs.reluct.compose.charts.util.checkIfDataIsNegative
import kotlin.math.min

/**
 * Composable function to render a donut chart with an optional legend.
 *
 * @param modifier Modifier for configuring the layout and appearance of the donut chart.
 * @param pieChartData List of data for the donut chart, including labels and values.
 * @param centerTitle Title displayed in the center of the donut chart.
 * @param centerTitleStyle TextStyle for configuring the appearance of the center title.
 * @param animation Animation specification for the donut chart transitions (default is a 3-second linear animation).
 * @param descriptionStyle TextStyle for configuring the appearance of the chart description (legend) text.
 * @param textRatioStyle TextStyle for configuring the appearance of ratio text labels (default font size is 12sp).
 * @param outerCircularColor Color of the outer circular border of the donut chart (default is Gray).
 * @param innerCircularColor Color of the inner circular area of the donut chart (default is Gray).
 * @param ratioLineColor Color of the lines connecting ratio labels to chart segments (default is Gray).
 * @param legendPosition Position of the legend within the chart (default is [LegendPosition.TOP]).
 *
 * @see PieChartData
 * @see LegendPosition
 */
@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    pieChartData: List<PieChartData>,
    centerTitle: String = "",
    centerTitleStyle: TextStyle = TextStyle.Default,
    animation: AnimationSpec<Float> = TweenSpec(durationMillis = 3000),
    descriptionStyle: TextStyle = TextStyle.Default,
    textRatioStyle: TextStyle = TextStyle.Default.copy(fontSize = 12.sp),
    outerCircularColor: Color = Color.Gray,
    innerCircularColor: Color = Color.Gray,
    ratioLineColor: Color = Color.Gray,
    legendPosition: LegendPosition = ChartDefaultValues.legendPosition,
) {
    var totalSum = 0.0f
    val pieValueWithRatio = mutableListOf<Float>()
    pieChartData.forEach {
        totalSum += it.data.toFloat()
    }
    pieChartData.forEachIndexed { index, part ->
        pieValueWithRatio.add(index, 360 * part.data.toFloat() / totalSum)
    }


    val textMeasure = rememberTextMeasurer()
    val textLayoutResult: TextLayoutResult = textMeasure.measure(
        text = AnnotatedString(centerTitle.take(10))
    )
    val textSize = textLayoutResult.size

    checkIfDataIsNegative(data = pieChartData.map { it.data })
    val transitionProgress = remember(pieValueWithRatio) { Animatable(initialValue = 0F) }

    LaunchedEffect(pieChartData) {
        transitionProgress.animateTo(1F, animationSpec = animation)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        when (legendPosition) {
            LegendPosition.TOP -> {
                PieChartDescriptionComposable(
                    pieChartData = pieChartData,
                    descriptionStyle = descriptionStyle,
                    modifier = Modifier.fillMaxWidth().weight(0.5f)
                )
                drawDonutChart(
                    modifier = Modifier.weight(1.5f),
                    textMeasure = textMeasure,
                    pieChartData = pieChartData,
                    centerTitle = centerTitle,
                    centerTitleStyle = centerTitleStyle,
                    textRatioStyle = textRatioStyle,
                    outerCircularColor = outerCircularColor,
                    innerCircularColor = innerCircularColor,
                    ratioLineColor = ratioLineColor,
                    textSize = textSize,
                    pieValueWithRatio = pieValueWithRatio,
                    totalSum = totalSum,
                    transitionProgress = transitionProgress
                )
            }

            LegendPosition.BOTTOM -> {
                drawDonutChart(
                    modifier = Modifier.weight(1.5f),
                    textMeasure = textMeasure,
                    pieChartData = pieChartData,
                    centerTitle = centerTitle,
                    centerTitleStyle = centerTitleStyle,
                    textRatioStyle = textRatioStyle,
                    outerCircularColor = outerCircularColor,
                    innerCircularColor = innerCircularColor,
                    ratioLineColor = ratioLineColor,
                    textSize = textSize,
                    pieValueWithRatio = pieValueWithRatio,
                    totalSum = totalSum,
                    transitionProgress = transitionProgress
                )
                PieChartDescriptionComposable(
                    pieChartData = pieChartData,
                    descriptionStyle = descriptionStyle,
                    modifier = Modifier.fillMaxWidth().weight(0.5f)
                )
            }

            LegendPosition.DISAPPEAR -> {
                drawDonutChart(
                    modifier = Modifier.weight(1.5f),
                    textMeasure = textMeasure,
                    pieChartData = pieChartData,
                    centerTitle = centerTitle,
                    centerTitleStyle = centerTitleStyle,
                    textRatioStyle = textRatioStyle,
                    outerCircularColor = outerCircularColor,
                    innerCircularColor = innerCircularColor,
                    ratioLineColor = ratioLineColor,
                    textSize = textSize,
                    pieValueWithRatio = pieValueWithRatio,
                    totalSum = totalSum,
                    transitionProgress = transitionProgress
                )
            }
        }


    }

}

@Composable
private fun drawDonutChart(
    modifier: Modifier = Modifier,
    textMeasure: TextMeasurer,
    pieChartData: List<PieChartData>,
    centerTitle: String = "",
    centerTitleStyle: TextStyle = TextStyle.Default,
    textRatioStyle: TextStyle = TextStyle.Default.copy(fontSize = 12.sp),
    outerCircularColor: Color = Color.Gray,
    innerCircularColor: Color = Color.Gray,
    ratioLineColor: Color = Color.Gray,
    textSize: IntSize,
    pieValueWithRatio: MutableList<Float>,
    totalSum: Float,
    transitionProgress: Animatable<Float, AnimationVector1D>,
) {
    Box(
        modifier = modifier.fillMaxSize()
            .drawBehind {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val minValue = min(canvasWidth, canvasHeight)
                    .coerceAtMost(canvasHeight / 2)
                    .coerceAtMost(canvasWidth / 2)
                val arcWidth = (size.minDimension.dp.toPx() * 0.13f).coerceAtMost(minValue / 4)

                drawCenterText(
                    textMeasure = textMeasure,
                    centerTitle = centerTitle,
                    centerTitleStyle = centerTitleStyle,
                    canvasHeight = canvasHeight,
                    canvasWidth = canvasWidth,
                    textSize = textSize
                )

                drawPedigreeChart(
                    pieValueWithRatio = pieValueWithRatio,
                    pieChartData = pieChartData,
                    totalSum = totalSum,
                    transitionProgress = transitionProgress,
                    textMeasure = textMeasure,
                    textRatioStyle = textRatioStyle,
                    ratioLineColor = ratioLineColor,
                    arcWidth = arcWidth,
                    minValue = minValue,
                    pieChart = ChartTypes.DONUT_CHART
                )
                //draw outer circle
                draPieCircle(
                    circleColor = outerCircularColor,
                    radiusRatioCircle = (minValue / 2) + (arcWidth / 1.5f)
                )
                //draw inner circle
                draPieCircle(
                    circleColor = innerCircularColor,
                    radiusRatioCircle = (minValue / 2) - (arcWidth / 1.5f)
                )


            }
    )

}
