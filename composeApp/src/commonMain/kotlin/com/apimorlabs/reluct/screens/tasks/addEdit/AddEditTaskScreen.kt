package com.apimorlabs.reluct.screens.tasks.addEdit

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.features.tasks.addEdit.AddEditTaskViewModel
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
import com.apimorlabs.reluct.screens.tasks.components.ModifyTaskLabel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AddEditTaskScreen(
    taskId: String?,
    onBackClicked: () -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }
    val viewModel: AddEditTaskViewModel = koinViewModel { parametersOf(taskId) }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(TasksEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState,
        goBack = onBackClicked
    )

    AddEditTaskUI(
        snackbarState = snackbarState,
        uiState = uiState,
        onSaveTask = viewModel::saveCurrentTask,
        onAddTaskClicked = viewModel::newTask,
        onUpdateTask = viewModel::updateCurrentTask,
        onBackClicked = onBackClicked,
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

            is TasksEvents.Navigation.GoBack -> goBack()
            else -> {}
        }
    }
}