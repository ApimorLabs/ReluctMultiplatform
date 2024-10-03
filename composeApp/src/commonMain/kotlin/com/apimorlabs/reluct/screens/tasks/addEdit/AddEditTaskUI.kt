package com.apimorlabs.reluct.screens.tasks.addEdit

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.tasks.EditTask
import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.add_task_button_text
import com.apimorlabs.reluct.compose.ui.add_task_text
import com.apimorlabs.reluct.compose.ui.components.bottomSheet.addEditTask.LazyColumnAddEditTaskFields
import com.apimorlabs.reluct.compose.ui.components.buttons.OutlinedReluctButton
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.dialogs.DiscardPromptDialog
import com.apimorlabs.reluct.compose.ui.components.images.ImageWithDescription
import com.apimorlabs.reluct.compose.ui.components.topBar.ReluctSmallTopAppBar
import com.apimorlabs.reluct.compose.ui.discard_button_text
import com.apimorlabs.reluct.compose.ui.discard_changes_text
import com.apimorlabs.reluct.compose.ui.discard_task
import com.apimorlabs.reluct.compose.ui.discard_task_message
import com.apimorlabs.reluct.compose.ui.edit_task_text
import com.apimorlabs.reluct.compose.ui.email_done
import com.apimorlabs.reluct.compose.ui.exit_text
import com.apimorlabs.reluct.compose.ui.no_tasks
import com.apimorlabs.reluct.compose.ui.save_button_text
import com.apimorlabs.reluct.compose.ui.task_not_found_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.util.EditTitles
import com.apimorlabs.reluct.features.tasks.states.AddEditTaskState
import com.apimorlabs.reluct.features.tasks.states.ModifyTaskState
import com.apimorlabs.reluct.screens.tasks.components.CurrentTaskLabels
import com.apimorlabs.reluct.screens.tasks.components.ManageTaskLabelsSheet
import com.apimorlabs.reluct.screens.tasks.components.ModifyTaskLabel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddEditTaskUI(
    snackbarState: SnackbarHostState,
    uiState: State<AddEditTaskState>,
    onSaveTask: () -> Unit,
    onAddTaskClicked: () -> Unit,
    onUpdateTask: (task: EditTask) -> Unit,
    onBackClicked: () -> Unit,
    onModifyTaskLabel: (ModifyTaskLabel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val modalSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val modifyTaskState = remember { derivedStateOf { uiState.value.modifyTaskState } }

    val labelsState = getLabelState(
        modifyTaskStateProvider = { modifyTaskState.value },
        availableLabelsProvider = { uiState.value.availableTaskLabels },
        onUpdateTask = onUpdateTask
    )

    val titles = getTitles(modifyTaskStateProvider = { modifyTaskState.value })

    val canGoBack by remember { derivedStateOf { modifyTaskState.value !is ModifyTaskState.Data } }
    val openDialog = remember { mutableStateOf(false) }

    // Call this when you trying to Go Back safely!
    fun goBackAttempt(canGoBack: Boolean) {
        if (canGoBack) onBackClicked() else openDialog.value = true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ReluctSmallTopAppBar(
                title = titles.value.appBarTitle,
                navigationIcon = {
                    IconButton(onClick = { goBackAttempt(canGoBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = modifyTaskState.value,
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { targetState ->
                when (targetState) {
                    is ModifyTaskState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is ModifyTaskState.Saved -> {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement
                                .spacedBy(Dimens.MediumPadding.size)
                        ) {
                            ImageWithDescription(
                                painter = painterResource(Res.drawable.email_done),
                                imageSize = 300.dp,
                                description = null,
                                descriptionTextStyle = MaterialTheme.typography.bodyLarge
                            )
                            ReluctButton(
                                buttonText = stringResource(Res.string.add_task_button_text),
                                icon = Icons.Rounded.Add,
                                shape = Shapes.large,
                                buttonColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                onButtonClicked = onAddTaskClicked
                            )
                            OutlinedReluctButton(
                                buttonText = stringResource(Res.string.exit_text),
                                icon = Icons.AutoMirrored.Rounded.ArrowBack,
                                shape = Shapes.large,
                                borderColor = MaterialTheme.colorScheme.primary,
                                onButtonClicked = onBackClicked
                            )
                        }
                    }

                    is ModifyTaskState.NotFound -> {
                        ImageWithDescription(
                            painter = painterResource(Res.drawable.no_tasks),
                            imageSize = 300.dp,
                            description = stringResource(Res.string.task_not_found_text),
                        )
                    }

                    else -> {}
                }
            }

            EditTasksList(
                getModifyTaskState = { modifyTaskState.value },
                onUpdateTask = onUpdateTask,
                onSaveTask = onSaveTask,
                onGoBack = { goBackAttempt(canGoBack) },
                onEditLabels = {
                    showBottomSheet = true
                    // scope.launch { modalSheetState.expand() }
                }
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = modalSheetState
            ) {
                ManageTaskLabelsSheet(
                    modifier = Modifier,
                    labelsState = labelsState.value,
                    onSaveLabel = { onModifyTaskLabel(ModifyTaskLabel.SaveLabel(it)) },
                    onDeleteLabel = { onModifyTaskLabel(ModifyTaskLabel.Delete(it)) },
                    onClose = {
                        scope.launch { modalSheetState.hide() }.invokeOnCompletion {
                            if (!modalSheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }
                )
            }
        }
    }

    val dialogText = stringResource(Res.string.discard_task_message)
    // Discard Dialog
    DiscardPromptDialog(
        dialogTitleProvider = { titles.value.dialogTitle },
        dialogTextProvider = { dialogText },
        openDialog = openDialog,
        onClose = { openDialog.value = false },
        onPositiveClick = onBackClicked
    )
}

@Composable
private fun EditTasksList(
    getModifyTaskState: () -> ModifyTaskState,
    onUpdateTask: (task: EditTask) -> Unit,
    onSaveTask: () -> Unit,
    onGoBack: () -> Unit,
    onEditLabels: () -> Unit
) {
    val tasksState = getModifyTaskState()
    if (tasksState is ModifyTaskState.Data) {
        LazyColumnAddEditTaskFields(
            task = tasksState.task,
            saveButtonText = stringResource(Res.string.save_button_text),
            discardButtonText = stringResource(Res.string.discard_button_text),
            onSave = { onSaveTask() },
            onUpdateTask = onUpdateTask,
            onDiscard = onGoBack,
            onEditLabels = onEditLabels
        )
    }
}

@Composable
private fun getLabelState(
    modifyTaskStateProvider: () -> ModifyTaskState,
    availableLabelsProvider: () -> ImmutableList<TaskLabel>,
    onUpdateTask: (task: EditTask) -> Unit
) = remember {
    derivedStateOf {
        val tasksStats = modifyTaskStateProvider()
        val task = if (tasksStats is ModifyTaskState.Data) tasksStats.task else null
        CurrentTaskLabels(
            availableLabels = availableLabelsProvider(),
            selectedLabels = task?.taskLabels ?: persistentListOf(),
            onUpdateSelectedLabels = { labels ->
                task?.copy(taskLabels = labels)?.let(onUpdateTask)
            }
        )
    }
}

@Composable
private fun getTitles(
    modifyTaskStateProvider: () -> ModifyTaskState,
): State<EditTitles> {
    val editAppBarTitle = stringResource(Res.string.edit_task_text)
    val editDialogTitle = stringResource(Res.string.discard_changes_text)
    val addAppBarTitle = stringResource(Res.string.add_task_text)
    val discardTaskTitle = stringResource(Res.string.discard_task)

    return remember {
        derivedStateOf {
            when (val goalState = modifyTaskStateProvider()) {
                is ModifyTaskState.Data -> {
                    if (goalState.isEdit) {
                        EditTitles(
                            appBarTitle = editAppBarTitle,
                            dialogTitle = editDialogTitle
                        )
                    } else {
                        EditTitles(
                            appBarTitle = addAppBarTitle,
                            dialogTitle = discardTaskTitle
                        )
                    }
                }

                else -> EditTitles(
                    appBarTitle = "• • •",
                    dialogTitle = discardTaskTitle
                )
            }
        }
    }
}
