package com.apimorlabs.reluct.compose.ui.components.statistics.charts.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.onClick
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.baseComponets.model.LegendPosition
import com.apimorlabs.reluct.compose.charts.donutChart.DonutChart
import com.apimorlabs.reluct.compose.charts.donutChart.model.ChartLabelType
import com.apimorlabs.reluct.compose.charts.donutChart.model.PieChartData
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsPieChartBase(
    slices: ImmutableList<PieChartData>,
    dataLoading: Boolean,
    middleText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    chartLabelType: ChartLabelType = ChartLabelType.NONE,
    chartSize: Dp = 160.dp,
) {
    Box(
        modifier = modifier then Modifier.size(chartSize),
        contentAlignment = Alignment.Center
    ) {
        if (dataLoading) {
            CircularProgressIndicator()
        } else {
            val lineColor = contentColor.copy(alpha = .7f)
            DonutChart(
                modifier = Modifier.fillMaxSize().onClick { onClick() },
                pieChartData = slices,
                centerTitle = middleText,
                centerTitleStyle = textStyle.copy(color = contentColor),
                textRatioStyle = textStyle.copy(color = contentColor),
                outerCircularColor = lineColor,
                innerCircularColor = lineColor,
                ratioLineColor = lineColor,
                legendPosition = LegendPosition.DISAPPEAR,
                chartLabelType = chartLabelType
            )
        }
    }
}
