package com.apimorlabs.reluct.compose.charts.donutChart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.baseComponets.ChartDescription
import com.apimorlabs.reluct.compose.charts.donutChart.model.PieChartData
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun PieChartDescriptionComposable(
    pieChartData: ImmutableList<PieChartData>,
    modifier: Modifier = Modifier,
    descriptionStyle: TextStyle = TextStyle.Default
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(pieChartData) { details ->
            ChartDescription(
                chartColor = details.color,
                chartName = details.partName,
                descriptionStyle = descriptionStyle,
            )
        }
    }
}
