package com.apimorlabs.reluct.screens.tasks.pending

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
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.features.tasks.pending.PendingTasksViewModel
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PendingTasksScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAddTask: (taskId: String?) -> Unit,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: PendingTasksViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = TasksEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState,
        navigateToTaskDetails = { id -> onNavigateToTaskDetails(id) }
    )

    PendingTasksUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        uiState = uiState,
        snackbarState = snackbarState,
        onTaskClicked = { viewModel.navigateToTaskDetails(it.id) },
        onAddTaskClicked = { onNavigateToAddTask(it?.id) },
        onToggleTaskDone = viewModel::toggleDone,
        fetchMoreData = viewModel::fetchMoreData
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
                launch {
                    snackbarState.showSnackbar(
                        message = getString(Res.string.task_marked_as_done, events.msg),
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