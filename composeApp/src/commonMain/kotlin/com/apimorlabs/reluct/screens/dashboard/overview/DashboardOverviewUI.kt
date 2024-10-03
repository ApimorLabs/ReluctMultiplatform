package com.apimorlabs.reluct.screens.dashboard.overview

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.active_goals_text
import com.apimorlabs.reluct.compose.ui.components.buttons.ScrollToTop
import com.apimorlabs.reluct.compose.ui.components.cards.goalEntry.GoalEntry
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.components.cards.permission.PermissionsCard
import com.apimorlabs.reluct.compose.ui.components.cards.taskEntry.EntryType
import com.apimorlabs.reluct.compose.ui.components.cards.taskEntry.TaskEntry
import com.apimorlabs.reluct.compose.ui.components.images.ImageWithDescription
import com.apimorlabs.reluct.compose.ui.components.statistics.charts.screenTime.DailyScreenTimePieChart
import com.apimorlabs.reluct.compose.ui.no_tasks
import com.apimorlabs.reluct.compose.ui.no_tasks_text
import com.apimorlabs.reluct.compose.ui.permissions_unlock
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.upcoming_tasks_text
import com.apimorlabs.reluct.compose.ui.usage_permissions_details
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.compose.ui.util.rememberScrollContext
import com.apimorlabs.reluct.features.dashboard.states.DashboardOverviewState
import com.apimorlabs.reluct.features.dashboard.states.TodayScreenTimeState
import com.apimorlabs.reluct.features.dashboard.states.TodayTasksState
import com.apimorlabs.reluct.screens.dashboard.components.getScreenTimePieChartData
import com.apimorlabs.reluct.screens.screentime.components.UsagePermissionDialog
import com.apimorlabs.reluct.screens.util.BottomBarVisibilityHandler
import com.apimorlabs.reluct.screens.util.GetPermissionsManager
import com.apimorlabs.reluct.screens.util.PermissionCheckHandler
import com.apimorlabs.reluct.screens.util.PermissionsManager
import com.apimorlabs.reluct.screens.util.getSnackbarModifier
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DashboardOverviewUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarHostState: SnackbarHostState,
    uiState: State<DashboardOverviewState>,
    getUsageData: (isGranted: Boolean) -> Unit,
    openScreenTimeStats: () -> Unit,
    openPendingTask: (Task) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    onGoalClicked: (Goal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    val scope = rememberCoroutineScope()

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    val chartData = getScreenTimePieChartData(
        appsUsageProvider = { uiState.value.todayScreenTimeState.usageStats.appsUsageList },
        isLoadingProvider = { uiState.value.todayScreenTimeState is TodayScreenTimeState.Loading }
    )

    var usagePermissionGranted by remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }

    // Permission Manager
    var permManager: PermissionsManager? = remember { null }
    GetPermissionsManager(onPermissionsManager = { permManager = it })

    PermissionCheckHandler {
        if (!usagePermissionGranted && permManager != null) {
            usagePermissionGranted = permManager?.checkUsageAccessPermission() ?: false
            getUsageData(usagePermissionGranted)
        }
    }

    val snackbarModifier = getSnackbarModifier(
        mainPadding = mainScaffoldPadding,
        scrollContext = scrollContext
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
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
                // Top Space
                item {
                    Spacer(modifier = Modifier)
                }

                // Permission Card
                if (!usagePermissionGranted) {
                    item {
                        PermissionsCard(
                            imageSlot = {
                                ImageWithDescription(
                                    painter = painterResource(Res.drawable.permissions_unlock),
                                    imageSize = 200.dp,
                                    description = null,
                                    descriptionTextStyle = MaterialTheme.typography.bodyLarge
                                )
                            },
                            permissionDetails = stringResource(Res.string.usage_permissions_details),
                            onPermissionRequest = { openDialog.value = true }
                        )
                    }
                }

                // Pie Chart
                item {
                    DailyScreenTimePieChart(
                        chartData = chartData,
                        unlockCountProvider = {
                            uiState.value.todayScreenTimeState.usageStats.unlockCount
                        },
                        screenTimeTextProvider = {
                            uiState.value.todayScreenTimeState.usageStats.formattedTotalScreenTime
                        },
                        onClick = openScreenTimeStats
                    )
                }

                // Tasks and Goals
                tasksAndGoals(
                    uiStateProvider = { uiState.value },
                    onOpenPendingTask = openPendingTask,
                    onToggleTaskDone = onToggleTaskDone,
                    onGoalClicked = onGoalClicked
                )

                // Bottom Space for spaceBy
                item {
                    Spacer(
                        modifier = Modifier.padding(mainScaffoldPadding)
                    )
                }
            }

            // Scroll To Top
            ScrollToTop(
                scrollContext = scrollContext,
                onScrollToTop = {
                    scope.launch { listState.animateScrollToItem(0) }
                }
            )
        }
    }

    // Permission Dialog
    // Go To Usage Access Dialog
    UsagePermissionDialog(openDialog = openDialog, onClose = { openDialog.value = false })
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.tasksAndGoals(
    uiStateProvider: () -> DashboardOverviewState,
    onOpenPendingTask: (Task) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    onGoalClicked: (Goal) -> Unit,
) {
    // Tasks
    stickyHeader {
        ListGroupHeadingHeader(text = stringResource(Res.string.upcoming_tasks_text))
    }

    if (uiStateProvider().todayTasksState is TodayTasksState.Loading) {
        item { LinearProgressIndicator() }
    }

    // No Tasks Animation
    if (uiStateProvider().todayTasksState.pending.isEmpty()) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ImageWithDescription(
                    painter = painterResource(Res.drawable.no_tasks),
                    imageSize = 200.dp,
                    description = stringResource(Res.string.no_tasks_text),
                    descriptionTextStyle = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    // Upcoming Tasks
    items(items = uiStateProvider().todayTasksState.pending, key = { it.id }) { item ->
        TaskEntry(
            modifier = Modifier.animateItem(),
            task = item,
            entryType = EntryType.TasksWithOverdue,
            onEntryClick = { onOpenPendingTask(item) },
            onCheckedChange = { onToggleTaskDone(it, item) }
        )
    }

    // Current Active Goals
    if (uiStateProvider().goals.isNotEmpty()) {
        stickyHeader {
            ListGroupHeadingHeader(text = stringResource(Res.string.active_goals_text))
        }

        items(uiStateProvider().goals, key = { it.id }) { goal ->
            GoalEntry(
                modifier = Modifier.animateItem(),
                goal = goal,
                onEntryClick = { onGoalClicked(goal) }
            )
        }
    }
}