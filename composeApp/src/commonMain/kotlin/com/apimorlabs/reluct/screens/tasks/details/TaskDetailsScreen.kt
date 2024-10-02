package com.apimorlabs.reluct.screens.tasks.details

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
import com.apimorlabs.reluct.features.tasks.details.TaskDetailsViewModel
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
import com.apimorlabs.reluct.screens.tasks.components.ModifyTaskLabel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TaskDetailsScreen(
    taskId: String?,
    onNavigateToEditTask: (taskId: String) -> Unit,
    onBackClicked: () -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: TaskDetailsViewModel = koinViewModel { parametersOf(taskId) }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = TasksEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState,
        navigateToEditTask = { id -> onNavigateToEditTask(id) },
        goBack = onBackClicked
    )

    TaskDetailsUI(
        uiState = uiState,
        snackbarState = snackbarState,
        onEditTask = { viewModel.editTask(it.id) },
        onDeleteTask = { viewModel.deleteTask(it.id) },
        onToggleTaskDone = { isDone, task -> viewModel.toggleDone(task, isDone) },
        onBackClicked = { viewModel.goBack() },
        onModifyTaskLabel = { modifyLabel ->
            when (modifyLabel) {
                is ModifyTaskLabel.SaveLabel -> modifyLabel.label.run(viewModel::saveLabel)
                is ModifyTaskLabel.Delete -> modifyLabel.label.run(viewModel::deleteLabel)
            }
        }
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<TasksEvents>,
    snackbarState: SnackbarHostState,
    navigateToEditTask: (taskId: String) -> Unit,
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

            is TasksEvents.Navigation.NavigateToEdit -> navigateToEditTask(events.taskId)
            is TasksEvents.Navigation.GoBack -> goBack()
            else -> {}
        }
    }
}