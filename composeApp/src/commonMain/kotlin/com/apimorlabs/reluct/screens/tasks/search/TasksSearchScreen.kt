package com.apimorlabs.reluct.screens.tasks.search

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
import com.apimorlabs.reluct.features.tasks.search.SearchTasksViewModel
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TasksSearchScreen(
    onNavigateToTaskDetails: (taskId: String) -> Unit,
    onBackClicked: () -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: SearchTasksViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = TasksEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState,
        navigateToTaskDetails = { id -> onNavigateToTaskDetails(id) },
        goBack = onBackClicked
    )

    TasksSearchUI(
        snackbarState = snackbarState,
        uiState = uiState,
        fetchMoreData = viewModel::fetchMoreData,
        onSearch = viewModel::search,
        onTaskClicked = { viewModel.navigateToTaskDetails(it.id) },
        onToggleTaskDone = viewModel::toggleDone
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<TasksEvents>,
    snackbarState: SnackbarHostState,
    navigateToTaskDetails: (taskId: String) -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is TasksEvents.ShowMessage -> {
                launch {
                    snackbarState.showSnackbar(
                        message = events.msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }

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

            is TasksEvents.Navigation.NavigateToTaskDetails -> navigateToTaskDetails(events.taskId)
            is TasksEvents.Navigation.GoBack -> goBack()
            else -> {}
        }
    }
}