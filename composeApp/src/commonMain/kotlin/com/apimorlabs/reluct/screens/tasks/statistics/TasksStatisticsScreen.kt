package com.apimorlabs.reluct.screens.tasks.statistics

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
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
import com.apimorlabs.reluct.features.tasks.statistics.TasksStatisticsViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TasksStatisticsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: TasksStatisticsViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = TasksEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState,
        navigateToTaskDetails = { taskId ->
            onNavigateToTaskDetails(taskId)
        }
    )

    TasksStatisticsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarState = snackbarState,
        uiState = uiState,
        onSelectDay = viewModel::selectDay,
        onTaskClicked = { onNavigateToTaskDetails(it.id) },
        onToggleTaskDone = viewModel::toggleDone,
        onUpdateWeekOffset = viewModel::updateWeekOffset
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<TasksEvents>,
    snackbarState: SnackbarHostState,
    navigateToTaskDetails: (taskId: String) -> Unit,
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is TasksEvents.ShowMessageDone -> {
                val msg = if (events.isDone) {
                    getString(Res.string.task_marked_as_done, events.msg)
                } else {
                    getString(Res.string.task_marked_as_not_done, events.msg)
                }
                launch {
                    snackbarState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is TasksEvents.Navigation.NavigateToTaskDetails -> {
                navigateToTaskDetails(events.taskId)
            }

            else -> {}
        }
    }
}