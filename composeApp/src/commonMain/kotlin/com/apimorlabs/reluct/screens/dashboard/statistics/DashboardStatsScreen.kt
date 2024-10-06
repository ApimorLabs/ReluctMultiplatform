package com.apimorlabs.reluct.screens.dashboard.statistics

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.features.dashboard.DashboardStatisticsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardStatsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAppUsageInfo: (packageName: String) -> Unit,
    onNavigateToScreenTimeStats: () -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: DashboardStatisticsViewModel = koinViewModel()
    val screenTimeUiState = viewModel.screenTimeUiState.collectAsStateWithLifecycle()
    val tasksStatsUiState = viewModel.tasksStatsUiState.collectAsStateWithLifecycle()

    DashboardStatsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarState = snackbarState,
        screenTimeUiState = screenTimeUiState,
        tasksStatsUiState = tasksStatsUiState,
        onTasksSelectDay = { viewModel.tasksSelectDay(it) },
        onScreenTimeSelectDay = { viewModel.screenTimeSelectDay(it) },
        onSelectAppTimeLimit = { viewModel.selectAppTimeLimit(it) },
        onSaveTimeLimit = { hours, minutes -> viewModel.saveAppTimeLimit(hours, minutes) },
        onAppUsageInfoClick = { onNavigateToAppUsageInfo(it.packageName) },
        onViewAllScreenTimeStats = onNavigateToScreenTimeStats
    )
}
