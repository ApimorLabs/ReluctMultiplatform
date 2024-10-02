package com.apimorlabs.reluct.screens.tasks.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.cancel
import com.apimorlabs.reluct.compose.ui.components.buttons.OutlinedReluctButton
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.cards.taskEntry.TaskDetailsHeading
import com.apimorlabs.reluct.compose.ui.components.cards.taskEntry.TaskInfoCard
import com.apimorlabs.reluct.compose.ui.components.cards.taskLabelEntry.TaskLabelPill
import com.apimorlabs.reluct.compose.ui.components.cards.taskLabelEntry.TaskLabelsEntryMode
import com.apimorlabs.reluct.compose.ui.components.images.ImageWithDescription
import com.apimorlabs.reluct.compose.ui.components.topBar.ReluctSmallTopAppBar
import com.apimorlabs.reluct.compose.ui.delete_button_text
import com.apimorlabs.reluct.compose.ui.delete_task
import com.apimorlabs.reluct.compose.ui.delete_task_message
import com.apimorlabs.reluct.compose.ui.edit_button_text
import com.apimorlabs.reluct.compose.ui.no_description_text
import com.apimorlabs.reluct.compose.ui.no_tasks
import com.apimorlabs.reluct.compose.ui.ok
import com.apimorlabs.reluct.compose.ui.task_details_text
import com.apimorlabs.reluct.compose.ui.task_not_found_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.features.tasks.states.TaskDetailsState
import com.apimorlabs.reluct.features.tasks.states.TaskState
import com.apimorlabs.reluct.screens.tasks.components.CurrentTaskLabels
import com.apimorlabs.reluct.screens.tasks.components.ManageTaskLabelsSheet
import com.apimorlabs.reluct.screens.tasks.components.ModifyTaskLabel
import com.apimorlabs.reluct.screens.tasks.components.TaskLabelsPage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class,)
@Composable
internal fun TaskDetailsUI(
    snackbarState: SnackbarHostState,
    uiState: State<TaskDetailsState>,
    onEditTask: (task: Task) -> Unit,
    onDeleteTask: (task: Task) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    onBackClicked: () -> Unit,
    onModifyTaskLabel: (ModifyTaskLabel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val modalSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val openDialog = remember { mutableStateOf(false) }

    val taskState = remember { derivedStateOf { uiState.value.taskState } }
    val labelsState by getLabelState(availableLabelsProvider = { uiState.value.availableTaskLabels })
    val taskLabelsPage = remember { mutableStateOf<TaskLabelsPage>(TaskLabelsPage.ShowLabels) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ReluctSmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = stringResource(Res.string.task_details_text),
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            when (val state = taskState.value) {
                is TaskState.Data -> {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        DetailsBottomBar(
                            onEditTaskClicked = { onEditTask(state.task) },
                            onDeleteTaskClicked = { openDialog.value = true }
                        )
                    }
                }

                else -> {}
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
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
                .navigationBarsPadding()
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = taskState.value,
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { targetState ->
                when (targetState) {
                    is TaskState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is TaskState.Data -> {}
                    else -> {
                        ImageWithDescription(
                            painter = painterResource(Res.drawable.no_tasks),
                            imageSize = 300.dp,
                            description = stringResource(Res.string.task_not_found_text),
                            descriptionTextStyle = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            DetailsLazyColumn(
                stateProvider = { taskState.value },
                listState = listState,
                onToggleTaskDone = onToggleTaskDone,
                onUpdateTaskLabelsPage = { taskLabelsPage.value = it },
                onShowBottomSheet = { scope.launch { modalSheetState.show() } }
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = modalSheetState
            ) {
                ManageTaskLabelsSheet(
                    modifier = Modifier.statusBarsPadding(),
                    entryMode = TaskLabelsEntryMode.ViewLabels,
                    startPage = taskLabelsPage.value,
                    labelsState = labelsState,
                    onSaveLabel = { onModifyTaskLabel(ModifyTaskLabel.SaveLabel(it)) },
                    onDeleteLabel = { onModifyTaskLabel(ModifyTaskLabel.Delete(it)) },
                    onClose = { scope.launch { modalSheetState.hide() } }
                )
            }
        }
    }

    // Delete Task Dialog
    DeleteTaskDialog(
        openDialog = openDialog,
        onClose = { openDialog.value = false },
        getCurrentTask = {
            when (val state = taskState.value) {
                is TaskState.Data -> state.task
                else -> null
            }
        },
        onDeleteTask = onDeleteTask
    )
}

@Composable
private fun DetailsLazyColumn(
    stateProvider: () -> TaskState,
    listState: LazyListState,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    onUpdateTaskLabelsPage: (TaskLabelsPage) -> Unit,
    onShowBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val providedState = remember { derivedStateOf { stateProvider() } }
    when (val state = providedState.value) {
        is TaskState.Data ->
            LazyColumn(
                modifier = modifier,
                state = listState,
                verticalArrangement = Arrangement
                    .spacedBy(Dimens.MediumPadding.size)
            ) {
                item {
                    TaskDetailsHeading(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.task.title,
                        textStyle = MaterialTheme.typography.headlineMedium
                            .copy(fontWeight = FontWeight.Medium),
                        isChecked = state.task.done,
                        onCheckedChange = { isDone ->
                            onToggleTaskDone(isDone, state.task)
                        }
                    )
                }

                item {
                    Text(
                        text = state.task.description
                            .ifBlank { stringResource(Res.string.no_description_text) },
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalContentColor.current
                            .copy(alpha = .8f)
                    )
                }

                // Task Labels
                if (state.task.taskLabels.isNotEmpty()) {
                    item {
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement
                                .spacedBy(Dimens.SmallPadding.size),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(
                                state.task.taskLabels,
                                key = { it.id }
                            ) { item ->
                                TaskLabelPill(
                                    modifier = Modifier.clickable {
                                        onUpdateTaskLabelsPage(TaskLabelsPage.ModifyLabel(item))
                                        onShowBottomSheet()
                                    },
                                    name = item.name,
                                    colorHex = item.colorHexString
                                )
                            }
                        }
                    }
                }

                item {
                    TaskInfoCard(
                        task = state.task,
                        shape = Shapes.large
                    )
                }

                // Bottom Space
                item {
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }

        else -> {}
    }
}

@Composable
private fun DetailsBottomBar(
    onEditTaskClicked: () -> Unit,
    onDeleteTaskClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.MediumPadding.size)
            .padding(bottom = Dimens.MediumPadding.size)
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(Dimens.MediumPadding.size)
    ) {
        ReluctButton(
            modifier = Modifier.weight(1f),
            buttonText = stringResource(Res.string.edit_button_text),
            icon = Icons.Rounded.Edit,
            onButtonClicked = onEditTaskClicked,
            buttonColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = Shapes.large
        )

        OutlinedReluctButton(
            modifier = Modifier.weight(1f),
            buttonText = stringResource(Res.string.delete_button_text),
            icon = Icons.Rounded.Delete,
            onButtonClicked = onDeleteTaskClicked,
            borderColor = MaterialTheme.colorScheme.primary,
            shape = Shapes.large
        )
    }
}

@Composable
private fun DeleteTaskDialog(
    openDialog: State<Boolean>,
    onClose: () -> Unit,
    getCurrentTask: () -> Task?,
    onDeleteTask: (task: Task) -> Unit,
    modifier: Modifier = Modifier
) {
    if (openDialog.value) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onClose,
            title = {
                Text(text = stringResource(Res.string.delete_task))
            },
            text = {
                Text(text = stringResource(Res.string.delete_task_message))
            },
            confirmButton = {
                ReluctButton(
                    buttonText = stringResource(Res.string.ok),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        onClose()
                        getCurrentTask()?.run(onDeleteTask)
                    }
                )
            },
            dismissButton = {
                ReluctButton(
                    buttonText = stringResource(Res.string.cancel),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onButtonClicked = onClose
                )
            }
        )
    }
}

@Composable
private fun getLabelState(availableLabelsProvider: () -> ImmutableList<TaskLabel>) =
    remember {
        derivedStateOf {
            CurrentTaskLabels(
                availableLabels = availableLabelsProvider(),
                selectedLabels = persistentListOf(),
                onUpdateSelectedLabels = {}
            )
        }
    }