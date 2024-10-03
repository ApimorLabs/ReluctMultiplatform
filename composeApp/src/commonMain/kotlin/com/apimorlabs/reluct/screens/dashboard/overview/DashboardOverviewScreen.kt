package com.apimorlabs.reluct.screens.dashboard.overview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.task_marked_as_done
import com.apimorlabs.reluct.compose.ui.task_marked_as_not_done
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.features.dashboard.DashboardOverviewViewModel
import com.apimorlabs.reluct.features.dashboard.states.events.DashboardEvents
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardOverviewScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToScreenTime: () -> Unit,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
    onNavigateToGoalDetails: (goalId: String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: DashboardOverviewViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events =
        viewModel.events.collectAsStateWithLifecycle(initialValue = DashboardEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarHostState = snackbarHostState
    )

    DashboardOverviewUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        getUsageData = { viewModel.permissionCheck(isGranted = it) },
        openScreenTimeStats = onNavigateToScreenTime,
        openPendingTask = { onNavigateToTaskDetails(it.id) },
        onToggleTaskDone = { isDone, task -> viewModel.toggleDone(task = task, isDone = isDone) },
        onGoalClicked = { onNavigateToGoalDetails(it.id) }
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<DashboardEvents>,
    snackbarHostState: SnackbarHostState,
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is DashboardEvents.ShowMessageDone -> {
                launch {
                    val msg = if (events.isDone) {
                        getString(Res.string.task_marked_as_done, events.msg)
                    } else {
                        getString(Res.string.task_marked_as_not_done, events.msg)
                    }
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            else -> {}
        }
    }
}
