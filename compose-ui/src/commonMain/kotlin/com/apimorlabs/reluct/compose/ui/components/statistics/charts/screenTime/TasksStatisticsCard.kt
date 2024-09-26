package com.apimorlabs.reluct.compose.ui.components.statistics.charts.screenTime

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.statistics.ChartData
import com.apimorlabs.reluct.compose.ui.components.statistics.charts.base.StatisticsBarChartCard
import com.apimorlabs.reluct.compose.ui.no_completed_tasks_text
import com.apimorlabs.reluct.compose.ui.tasks_tally_text_arg
import com.apimorlabs.reluct.compose.ui.weekly_task_count_arg
import org.jetbrains.compose.resources.stringResource

@Composable
fun TasksStatisticsCard(
    chartData: State<ChartData<BarParameters>>,
    selectedDayText: () -> String,
    selectedDayTasksDone: () -> Int,
    selectedDayTasksPending: () -> Int,
    totalWeekTaskCount: () -> Int,
    selectedDayIndex: () -> Int,
    onBarClicked: (Int) -> Unit,
    weekUpdateButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatisticsBarChartCard(
        modifier = modifier.animateContentSize(),
        bars = chartData.value.data,
        selectedBarColor = MaterialTheme.colorScheme.primary,
        dataLoading = chartData.value.isLoading,
        noDataText = stringResource(Res.string.no_completed_tasks_text),
        selectedDayIndex = selectedDayIndex(),
        onBarClicked = { onBarClicked(it) },
        topLeftText = {
            Text(
                text = selectedDayText(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current
            )
        },
        topRightText = {
            Text(
                text = stringResource(
                    Res.string.tasks_tally_text_arg,
                    selectedDayTasksDone(),
                    selectedDayTasksPending() + selectedDayTasksDone()
                ),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        belowChartText = {
            Spacer(modifier = Modifier)
            Text(
                text = stringResource(
                    Res.string.weekly_task_count_arg,
                    totalWeekTaskCount()
                ),
                style = MaterialTheme.typography.titleLarge
                    .copy(fontWeight = FontWeight.Medium),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current
            )
        },
        bodyContent = weekUpdateButton
    )
}
