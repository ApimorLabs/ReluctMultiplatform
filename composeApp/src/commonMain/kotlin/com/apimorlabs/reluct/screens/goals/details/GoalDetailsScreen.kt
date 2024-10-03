package com.apimorlabs.reluct.screens.goals.details

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.deleted_goal_value
import com.apimorlabs.reluct.compose.ui.goal_marked_active
import com.apimorlabs.reluct.compose.ui.goal_marked_inactive
import com.apimorlabs.reluct.features.goals.details.GoalDetailsViewModel
import com.apimorlabs.reluct.features.goals.states.events.GoalsEvents
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun GoalDetailsScreen(
    goalId: String?,
    onExit: () -> Unit,
    onNavigateToEditGoal: (goalId: String) -> Unit
) {
    val viewModel = koinViewModel<GoalDetailsViewModel> { parametersOf(goalId) }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = GoalsEvents.Nothing)

    val snackbarState = remember { SnackbarHostState() }
    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState,
        onExit = onExit
    )

    GoalDetailsUI(
        snackbarState = snackbarState,
        uiState = uiState,
        onEditGoal = onNavigateToEditGoal,
        onDeleteGoal = viewModel::deleteGoal,
        onToggleGoalActive = viewModel::toggleGoalActiveState,
        onGoBack = onExit,
        onSyncData = viewModel::syncData,
        onUpdateCurrentValue = viewModel::updateCurrentValue
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
            is GoalsEvents.ChangedGoalState -> {
                val msg = if (events.isActive) {
                    getString(Res.string.goal_marked_active)
                } else {
                    getString(Res.string.goal_marked_inactive)
                }
                launch {
                    snackbarState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is GoalsEvents.DeletedGoal -> {
                val msg = getString(Res.string.deleted_goal_value, events.goalName)
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
