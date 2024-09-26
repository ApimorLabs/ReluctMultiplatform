package com.apimorlabs.reluct.compose.ui.components.statistics.charts.screenTime

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.donutChart.model.PieChartData
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.statistics.ChartData
import com.apimorlabs.reluct.compose.ui.components.statistics.charts.base.StatisticsPieChartBase
import com.apimorlabs.reluct.compose.ui.screen_time_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.unlocks_text
import org.jetbrains.compose.resources.stringResource

@Composable
fun DailyScreenTimePieChart(
    chartData: State<ChartData<PieChartData>>,
    unlockCountProvider: () -> Long,
    screenTimeTextProvider: () -> String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    chartSize: Dp = 160.dp
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .clip(shape),
        onClick = onClick
    ) {
        AnimatedContent(
            modifier = Modifier.padding(Dimens.MediumPadding.size),
            targetState = chartData.value.isLoading,
            contentAlignment = Alignment.Center
        ) { isLoading ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .height(chartSize)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LinearProgressIndicator()
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pie Chart
                    StatisticsPieChartBase(
                        modifier = Modifier,
                        slices = chartData.value.data,
                        contentColor = contentColor,
                        dataLoading = chartData.value.isLoading,
                        middleText = "",
                        onClick = onClick,
                        chartSize = chartSize
                    )
                    Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
                    // Left Text
                    StatsDetails(
                        modifier = Modifier,
                        contentColor = contentColor,
                        screenTimeText = screenTimeTextProvider(),
                        unlockCount = if (chartData.value.isLoading) "..." else unlockCountProvider().toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsDetails(
    contentColor: Color,
    screenTimeText: String,
    unlockCount: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement
            .spacedBy(Dimens.SmallPadding.size)
    ) {
        Text(
            text = stringResource(Res.string.screen_time_text),
            style = MaterialTheme.typography.bodyMedium
                .copy(fontWeight = FontWeight.Medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = contentColor
        )
        Text(
            text = screenTimeText,
            style = MaterialTheme.typography.titleLarge
                .copy(fontWeight = FontWeight.Medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier)

        Text(
            text = stringResource(Res.string.unlocks_text),
            style = MaterialTheme.typography.bodyMedium
                .copy(fontWeight = FontWeight.Medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = contentColor
        )
        Text(
            text = unlockCount,
            style = MaterialTheme.typography.titleLarge
                .copy(fontWeight = FontWeight.Medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
