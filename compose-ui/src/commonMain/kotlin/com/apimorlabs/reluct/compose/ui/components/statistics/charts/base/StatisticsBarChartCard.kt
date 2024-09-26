package com.apimorlabs.reluct.compose.ui.components.statistics.charts.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun StatisticsBarChartCard(
    bars: ImmutableList<BarParameters>,
    dataLoading: Boolean,
    noDataText: String,
    selectedDayIndex: Int,
    onBarClicked: (Int) -> Unit,
    topLeftText: @Composable () -> Unit,
    topRightText: @Composable () -> Unit,
    belowChartText: @Composable () -> Unit,
    bodyContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    shape: Shape = Shapes.large,
    selectedBarColor: Color = contentColor,
    chartHeight: Dp = 160.dp,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.MediumPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(Dimens.MediumPadding.size)
        ) {
            // Top Text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement
                    .spacedBy(Dimens.MediumPadding.size)
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart,
                    content = { topLeftText() }
                )

                Box(
                    contentAlignment = Alignment.CenterEnd,
                    content = { topRightText() }
                )
            }

            // Chart Area
            Box(contentAlignment = Alignment.Center) {
                val sumOfBarsValue = bars.sumOf { it.data }
                StatisticsBarChartBase(
                    modifier = Modifier.height(chartHeight),
                    bars = bars,
                    selectedBarColor = selectedBarColor,
                    selectedDayIndex = selectedDayIndex,
                    onBarClicked = { onBarClicked(it) }
                )
                if (dataLoading) {
                    LinearProgressIndicator()
                } else if (sumOfBarsValue.equals(0.0)) {
                    Text(
                        text = noDataText,
                        style = MaterialTheme.typography.titleLarge
                            .copy(fontWeight = FontWeight.Medium),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = contentColor
                    )
                }
            }

            // Text Below Chart for stats info
            belowChartText()

            // Body Content
            bodyContent()
        }
    }
}
