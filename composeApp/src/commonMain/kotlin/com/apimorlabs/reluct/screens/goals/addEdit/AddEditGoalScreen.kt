package com.apimorlabs.reluct.screens.goals.addEdit

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.saved_goal_value
import com.apimorlabs.reluct.features.goals.addEdit.AddEditGoalViewModel
import com.apimorlabs.reluct.features.goals.states.events.GoalsEvents
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AddEditGoalScreen(
    goalId: String?,
    defaultGoalIndex: Int?,
    onExit: () -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel = koinViewModel<AddEditGoalViewModel> {
        parametersOf(goalId, defaultGoalIndex)
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = GoalsEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState,
        onExit = onExit
    )

    AddEditGoalUI(
        snackbarState = snackbarState,
        uiState = uiState,
        onSave = viewModel::saveCurrentGoal,
        onCreateNewGoal = viewModel::newGoal,
        onSyncRelatedApps = viewModel::syncRelatedApps,
        onUpdateGoal = viewModel::updateCurrentGoal,
        onModifyApps = viewModel::modifyRelatedApps,
        onGoBack = onExit
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<GoalsEvents>,
    snackbarState: SnackbarHostState,
    onExit: () -> Unit
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is GoalsEvents.GoalSavedMessage -> {
                val msg = getString(Res.string.saved_goal_value, events.goalName)
                launch {
                    snackbarState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is GoalsEvents.Exit -> onExit()
            else -> {}
        }
    }
}