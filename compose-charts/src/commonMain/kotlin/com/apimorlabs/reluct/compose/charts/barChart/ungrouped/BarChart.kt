package com.apimorlabs.reluct.compose.charts.barChart.ungrouped

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.components.BarChartContent
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers.LabelValueDrawer
import com.apimorlabs.reluct.compose.charts.baseComponets.ChartDescription
import com.apimorlabs.reluct.compose.charts.baseComponets.model.LegendPosition
import com.apimorlabs.reluct.compose.charts.util.ChartDefaultValues
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Composable function to render a bar chart with an optional legend.
 *
 * @param modifier Modifier for configuring the layout and appearance of the bar chart.
 * @param chartParameters List of BarParameters describing the data for the bar chart.
 * @param gridColor Color of the grid lines (default is Gray).
 * @param xAxisData List of labels for the X-axis.
 * @param animateChart Flag to enable chart animations (default is true).
 * @param descriptionStyle TextStyle for configuring the appearance of chart description (legend) text.
 * @param yAxisStyle TextStyle for configuring the appearance of the Y-axis labels.
 * @param xAxisStyle TextStyle for configuring the appearance of the X-axis labels.
 * @param horizontalArrangement Horizontal arrangement for legend items (default is [Arrangement.Center]).
 * @param backgroundLineWidth Width of the background grid lines (default is 1.0).
 * @param showXAxis Flag to determine whether to display the X-axis (default is true).
 * @param showYAxis Flag to determine whether to display the Y-axis (default is true).
 * @param showXAxisLabels Flag to determine whether to display the X-axis labels (default is true).
 * @param showYAxisLabels Flag to determine whether to display the Y-axis labels (default is true).
 * @param showIntervalLines Flag to determine whether to display the Interval lines for Y-axis labels
 * (default is false).
 * @param xAxisDrawLocation Flag to determine where to draw X-axis line labels
 * (default is [LabelValueDrawer.DrawLocation.XAxis]).
 * @param barsSpacingFactor The factor by which the bars are spaced. Should be between 0.0f - 1f (default is 0.1).
 * @param legendPosition Position of the legend within the chart (default is [LegendPosition.TOP]).
 * @param barCornerRadius radius of the bar corner in the chart (default is 10dp).
 *
 * @see BarParameters
 * @see LegendPosition
 */
@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    chartParameters: ImmutableList<BarParameters> = ChartDefaultValues.barParameters,
    gridColor: Color = ChartDefaultValues.gridColor,
    xAxisData: ImmutableList<String> = persistentListOf(),
    animateChart: Boolean = ChartDefaultValues.ANIMATED_CHART,
    descriptionStyle: TextStyle = ChartDefaultValues.descriptionDefaultStyle,
    yAxisStyle: TextStyle = ChartDefaultValues.axesStyle,
    xAxisStyle: TextStyle = ChartDefaultValues.axesStyle,
    horizontalArrangement: Arrangement.Horizontal = ChartDefaultValues.headerArrangement,
    backgroundLineWidth: Float = ChartDefaultValues.backgroundLineWidth.value,
    showXAxis: Boolean = ChartDefaultValues.showXAxis,
    showYAxis: Boolean = ChartDefaultValues.showyAxis,
    showXAxisLabels: Boolean = ChartDefaultValues.showXAxisLabel,
    showYAxisLabels: Boolean = ChartDefaultValues.showYAxisLabel,
    showIntervalLines: Boolean = ChartDefaultValues.showIntervalLines,
    xAxisDrawLocation: LabelValueDrawer.DrawLocation = ChartDefaultValues.xAxisDrawLocation,
    barsSpacingFactor: Float = ChartDefaultValues.barsSpacingFactor,
    legendPosition: LegendPosition = ChartDefaultValues.legendPosition,
    barCornerRadius: Dp = ChartDefaultValues.barCornerRadius,
    selectedBarColor: Color = ChartDefaultValues.selectedBarColor,
    selectedBarIndex: Int? = null,
    onBarClicked: (index: Int) -> Unit = {}
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        when (legendPosition) {
            LegendPosition.TOP -> {
                LazyRow(
                    horizontalArrangement = horizontalArrangement,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    items(chartParameters) { details ->
                        ChartDescription(
                            chartColor = details.barColor,
                            chartName = details.dataName,
                            descriptionStyle = descriptionStyle,
                        )
                    }
                }

                BarChartContent(
                    barsParameters = chartParameters,
                    gridColor = gridColor,
                    xAxisData = xAxisData,
                    animateChart = animateChart,
                    yAxisStyle = yAxisStyle,
                    xAxisStyle = xAxisStyle,
                    backgroundLineWidth = backgroundLineWidth,
                    showXAxis = showXAxis,
                    showYAxis = showYAxis,
                    barsSpacingFactor = barsSpacingFactor,
                    barCornerRadius = barCornerRadius,
                    selectedBarColor = selectedBarColor,
                    selectedBarIndex = selectedBarIndex,
                    showXAxisLabels = showXAxisLabels,
                    showYAxisLabels = showYAxisLabels,
                    showIntervalLines = showIntervalLines,
                    xAxisLabelDrawLocation = xAxisDrawLocation,
                    onBarClicked = onBarClicked
                )
            }

            LegendPosition.BOTTOM -> {
                BarChartContent(
                    barsParameters = chartParameters,
                    gridColor = gridColor,
                    xAxisData = xAxisData,
                    animateChart = animateChart,
                    yAxisStyle = yAxisStyle,
                    xAxisStyle = xAxisStyle,
                    backgroundLineWidth = backgroundLineWidth,
                    showXAxis = showXAxis,
                    showYAxis = showYAxis,
                    barsSpacingFactor = barsSpacingFactor,
                    modifier = Modifier.weight(1f),
                    barCornerRadius = barCornerRadius,
                    selectedBarColor = selectedBarColor,
                    selectedBarIndex = selectedBarIndex,
                    showXAxisLabels = showXAxisLabels,
                    showYAxisLabels = showYAxisLabels,
                    showIntervalLines = showIntervalLines,
                    xAxisLabelDrawLocation = xAxisDrawLocation,
                    onBarClicked = onBarClicked
                )

                LazyRow(
                    horizontalArrangement = horizontalArrangement,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    items(chartParameters) { details ->
                        ChartDescription(
                            chartColor = details.barColor,
                            chartName = details.dataName,
                            descriptionStyle = descriptionStyle,
                        )
                    }
                }
            }

            LegendPosition.DISAPPEAR -> {
                BarChartContent(
                    barsParameters = chartParameters,
                    gridColor = gridColor,
                    xAxisData = xAxisData,
                    animateChart = animateChart,
                    yAxisStyle = yAxisStyle,
                    xAxisStyle = xAxisStyle,
                    backgroundLineWidth = backgroundLineWidth,
                    showXAxis = showXAxis,
                    showYAxis = showYAxis,
                    barsSpacingFactor = barsSpacingFactor,
                    barCornerRadius = barCornerRadius,
                    selectedBarColor = selectedBarColor,
                    selectedBarIndex = selectedBarIndex,
                    showXAxisLabels = showXAxisLabels,
                    showYAxisLabels = showYAxisLabels,
                    showIntervalLines = showIntervalLines,
                    xAxisLabelDrawLocation = xAxisDrawLocation,
                    onBarClicked = onBarClicked
                )
            }
        }
    }
}
