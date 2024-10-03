package com.apimorlabs.reluct.screens.dashboard.statistics

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageInfo
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.buttons.OutlinedReluctButton
import com.apimorlabs.reluct.compose.ui.components.cards.appUsageEntry.AppUsageEntryBase
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.components.statistics.charts.screenTime.ScreenTimeStatisticsCard
import com.apimorlabs.reluct.compose.ui.components.statistics.charts.screenTime.TasksStatisticsCard
import com.apimorlabs.reluct.compose.ui.components.statistics.helpers.BarChartDefaults
import com.apimorlabs.reluct.compose.ui.components.statistics.helpers.getWeeklyTasksBarChartData
import com.apimorlabs.reluct.compose.ui.screen_time_text
import com.apimorlabs.reluct.compose.ui.tasks_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.compose.ui.util.BottomBarVisibilityHandler
import com.apimorlabs.reluct.compose.ui.util.rememberScrollContext
import com.apimorlabs.reluct.compose.ui.view_all_text
import com.apimorlabs.reluct.features.screenTime.states.ScreenTimeStatsState
import com.apimorlabs.reluct.features.screenTime.states.WeeklyUsageStatsState
import com.apimorlabs.reluct.features.tasks.states.TasksStatisticsState
import com.apimorlabs.reluct.features.tasks.states.WeeklyTasksState
import com.apimorlabs.reluct.screens.screentime.components.ShowAppTimeLimitDialog
import com.apimorlabs.reluct.screens.screentime.components.getWeeklyDeviceScreenTimeChartData
import com.apimorlabs.reluct.util.getSnackbarModifier
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DashboardStatsUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarState: SnackbarHostState,
    screenTimeUiState: State<ScreenTimeStatsState>,
    tasksStatsUiState: State<TasksStatisticsState>,
    onTasksSelectDay: (day: Int) -> Unit,
    onScreenTimeSelectDay: (day: Int) -> Unit,
    onSelectAppTimeLimit: (packageName: String) -> Unit,
    onSaveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    onAppUsageInfoClick: (appInfo: AppUsageInfo) -> Unit,
    onViewAllScreenTimeStats: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    // Bar Charts
    val barColor = BarChartDefaults.barColor
    // Screen Time Chart
    val screenTimeChartData = getWeeklyDeviceScreenTimeChartData(
        weeklyStatsProvider = { screenTimeUiState.value.weeklyData.usageStats },
        isLoadingProvider = { screenTimeUiState.value.weeklyData is WeeklyUsageStatsState.Loading },
        barColor = barColor
    )

    // Tasks Stats Chart
    val tasksChartData = getWeeklyTasksBarChartData(
        weeklyTasksProvider = { tasksStatsUiState.value.weeklyTasksState.weeklyTasks },
        isLoadingProvider = { tasksStatsUiState.value.weeklyTasksState is WeeklyTasksState.Loading },
        barColor = barColor
    )

    val showAppTimeLimitDialog = remember { mutableStateOf(false) }

    val snackbarModifier = getSnackbarModifier(
        mainPadding = mainScaffoldPadding,
        scrollContext = scrollContext
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    modifier = snackbarModifier.value,
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Box(
            modifier = Modifier
                .animateContentSize()
                .padding(padding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement
                    .spacedBy(Dimens.SmallPadding.size)
            ) {
                // Screen Time
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(Res.string.screen_time_text))
                }

                // Screen Time Chart
                item {
                    ScreenTimeStatisticsCard(
                        chartData = screenTimeChartData,
                        selectedDayText = { screenTimeUiState.value.dailyData.dayText },
                        selectedDayScreenTime = {
                            screenTimeUiState.value.dailyData.usageStat.formattedTotalScreenTime
                        },
                        weeklyTotalScreenTime = { screenTimeUiState.value.weeklyData.formattedTotalTime },
                        selectedDayIndex = { screenTimeUiState.value.selectedInfo.selectedDay },
                        onBarClicked = { onScreenTimeSelectDay(it) },
                        weekUpdateButton = {
                            // Show 2 Apps
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                screenTimeUiState.value.dailyData.usageStat.appsUsageList.take(3)
                                    .forEach { item ->
                                        AppUsageEntryBase(
                                            modifier = Modifier
                                                .padding(vertical = Dimens.SmallPadding.size)
                                                .fillMaxWidth()
                                                .clip(Shapes.large)
                                                .clickable { onAppUsageInfoClick(item) },
                                            appUsageInfo = item,
                                            onTimeSettingsClick = {
                                                onSelectAppTimeLimit(item.packageName)
                                                showAppTimeLimitDialog.value = true
                                            },
                                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                OutlinedReluctButton(
                                    modifier = Modifier.padding(vertical = Dimens.SmallPadding.size),
                                    buttonText = stringResource(Res.string.view_all_text),
                                    icon = null,
                                    onButtonClicked = onViewAllScreenTimeStats,
                                    borderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    buttonTextStyle = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    )
                }

                // Tasks Stats
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(Res.string.tasks_text))
                }

                // Tasks Chart
                item {
                    TasksStatisticsCard(
                        chartData = tasksChartData,
                        selectedDayText = { tasksStatsUiState.value.dailyTasksState.dayText },
                        selectedDayTasksDone = {
                            tasksStatsUiState.value.dailyTasksState.dailyTasks.completedTasksCount
                        },
                        selectedDayTasksPending = {
                            tasksStatsUiState.value.dailyTasksState.dailyTasks.pendingTasksCount
                        },
                        totalWeekTaskCount = { tasksStatsUiState.value.weeklyTasksState.totalWeekTasksCount },
                        selectedDayIndex = { tasksStatsUiState.value.selectedDay },
                        onBarClicked = { onTasksSelectDay(it) },
                        weekUpdateButton = {}
                    )
                }

                // Bottom Space for spaceBy
                item {
                    Spacer(
                        modifier = Modifier.padding(mainScaffoldPadding)
                    )
                }
            }
        }
    }

    // Dialogs
    // App Time Limit Dialog
    ShowAppTimeLimitDialog(
        openDialog = showAppTimeLimitDialog,
        limitStateProvider = { screenTimeUiState.value.appTimeLimit },
        onSaveTimeLimit = onSaveTimeLimit,
        onClose = { showAppTimeLimitDialog.value = false }
    )
}