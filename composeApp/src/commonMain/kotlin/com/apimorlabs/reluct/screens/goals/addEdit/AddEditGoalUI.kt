package com.apimorlabs.reluct.screens.goals.addEdit

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.add_goal_text
import com.apimorlabs.reluct.compose.ui.apps_text
import com.apimorlabs.reluct.compose.ui.components.bottomSheet.addEditGoal.LazyColumnAddEditGoal
import com.apimorlabs.reluct.compose.ui.components.buttons.OutlinedReluctButton
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.dialogs.DiscardPromptDialog
import com.apimorlabs.reluct.compose.ui.components.images.ImageWithDescription
import com.apimorlabs.reluct.compose.ui.components.topBar.ReluctSmallTopAppBar
import com.apimorlabs.reluct.compose.ui.delete_goal_message
import com.apimorlabs.reluct.compose.ui.discard_changes_text
import com.apimorlabs.reluct.compose.ui.discard_goal_text
import com.apimorlabs.reluct.compose.ui.edit_goal_text
import com.apimorlabs.reluct.compose.ui.email_done
import com.apimorlabs.reluct.compose.ui.empty
import com.apimorlabs.reluct.compose.ui.exit_text
import com.apimorlabs.reluct.compose.ui.goal_not_found_text
import com.apimorlabs.reluct.compose.ui.new_goal_text
import com.apimorlabs.reluct.compose.ui.selected_apps_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.util.EditTitles
import com.apimorlabs.reluct.features.goals.states.AddEditGoalState
import com.apimorlabs.reluct.features.goals.states.GoalAppsState
import com.apimorlabs.reluct.features.goals.states.ModifyGoalState
import com.apimorlabs.reluct.screens.screentime.components.ManageAppsDialog
import com.apimorlabs.reluct.util.BackPressHandler
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddEditGoalUI(
    snackbarState: SnackbarHostState,
    uiState: State<AddEditGoalState>,
    onSave: () -> Unit,
    onCreateNewGoal: () -> Unit,
    onSyncRelatedApps: () -> Unit,
    onUpdateGoal: (goal: Goal) -> Unit,
    onModifyApps: (appInfo: AppInfo, isAdd: Boolean) -> Unit,
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val modifyGoalState = remember { derivedStateOf { uiState.value.modifyGoalState } }

    val titles = getTitles(modifyGoalStateProvider = { modifyGoalState.value })

    val canGoBack by remember {
        derivedStateOf {
            modifyGoalState.value !is ModifyGoalState.Data
        }
    }
    val openExitDialog = remember { mutableStateOf(false) }
    val openRelatedAppsDialog = remember { mutableStateOf(false) }

    // Call this when you trying to Go Back safely!
    fun goBackAttempt(canGoBack: Boolean) {
        if (canGoBack) onGoBack() else openExitDialog.value = true
    }
    BackPressHandler { goBackAttempt(canGoBack) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            ReluctSmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
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
                targetState = modifyGoalState.value,
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { targetState ->
                when (targetState) {
                    is ModifyGoalState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is ModifyGoalState.NotFound -> {
                        ImageWithDescription(
                            painter = painterResource(Res.drawable.empty),
                            imageSize = 300.dp,
                            description = stringResource(Res.string.goal_not_found_text),
                            descriptionTextStyle = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is ModifyGoalState.Saved -> {
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
                                buttonText = stringResource(Res.string.new_goal_text),
                                icon = Icons.Rounded.Add,
                                shape = Shapes.large,
                                buttonColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                onButtonClicked = onCreateNewGoal
                            )
                            OutlinedReluctButton(
                                buttonText = stringResource(Res.string.exit_text),
                                icon = Icons.AutoMirrored.Rounded.ArrowBack,
                                shape = Shapes.large,
                                borderColor = MaterialTheme.colorScheme.primary,
                                onButtonClicked = onGoBack
                            )
                        }
                    }

                    else -> {}
                }
            }

            EditGoalList(
                getModifyGoalState = { modifyGoalState.value },
                onUpdateGoal = onUpdateGoal,
                onGoBack = { goBackAttempt(canGoBack) },
                onSyncRelatedApps = onSyncRelatedApps,
                onOpenAppsDialog = { openRelatedAppsDialog.value = true },
                onSave = onSave
            )
        }
    }

    // Manage Related Apps
    ManageAppsDialog(
        openDialog = openRelatedAppsDialog,
        onDismiss = { openRelatedAppsDialog.value = false },
        isLoadingProvider = { uiState.value.appsState is GoalAppsState.Loading },
        topItemsHeading = stringResource(Res.string.selected_apps_text),
        bottomItemsHeading = stringResource(Res.string.apps_text),
        topItems = { uiState.value.appsState.selectedApps },
        bottomItems = { uiState.value.appsState.unselectedApps },
        onTopItemClicked = { app -> onModifyApps(app, false) },
        onBottomItemClicked = { app -> onModifyApps(app, true) }
    )

    // Discard Dialog
    val dialogText = stringResource(Res.string.delete_goal_message)
    DiscardPromptDialog(
        dialogTitleProvider = { titles.value.dialogTitle },
        dialogTextProvider = { dialogText },
        openDialog = openExitDialog,
        onClose = { openExitDialog.value = false },
        onPositiveClick = onGoBack
    )
}

@Composable
private fun EditGoalList(
    getModifyGoalState: () -> ModifyGoalState,
    onUpdateGoal: (goal: Goal) -> Unit,
    onGoBack: () -> Unit,
    onSyncRelatedApps: () -> Unit,
    onOpenAppsDialog: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val goalState = getModifyGoalState()

    if (goalState is ModifyGoalState.Data) {
        LazyColumnAddEditGoal(
            modifier = modifier,
            goal = goalState.goal,
            onUpdateGoal = onUpdateGoal,
            onDiscard = onGoBack,
            onSave = { onSave() },
            onShowAppPicker = {
                onOpenAppsDialog()
                onSyncRelatedApps()
            }
        )
    }
}

@Composable
private fun getTitles(modifyGoalStateProvider: () -> ModifyGoalState): State<EditTitles> {
    val editGoalText = stringResource(Res.string.edit_goal_text)
    val discardChangesText = stringResource(Res.string.discard_changes_text)
    val discardGoalText = stringResource(Res.string.discard_goal_text)
    val addGoalText = stringResource(Res.string.add_goal_text)
    return remember {
        derivedStateOf {
            when (val goalState = modifyGoalStateProvider()) {
                is ModifyGoalState.Data -> {
                    if (goalState.isEdit) {
                        EditTitles(
                            appBarTitle = editGoalText,
                            dialogTitle = discardChangesText
                        )
                    } else {
                        EditTitles(
                            appBarTitle = addGoalText,
                            dialogTitle = discardGoalText
                        )
                    }
                }

                else -> EditTitles(
                    appBarTitle = "• • •",
                    dialogTitle = discardGoalText
                )
            }
        }
    }
}
