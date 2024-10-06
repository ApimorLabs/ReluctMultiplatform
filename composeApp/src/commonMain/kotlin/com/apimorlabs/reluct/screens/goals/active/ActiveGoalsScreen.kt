package com.apimorlabs.reluct.screens.goals.active

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.goal_marked_active
import com.apimorlabs.reluct.compose.ui.goal_marked_inactive
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.features.goals.active.ActiveGoalsViewModel
import com.apimorlabs.reluct.features.goals.states.events.GoalsEvents
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ActiveGoalsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAddGoal: (defaultGoalIndex: Int?) -> Unit,
    onNavigateToGoalDetails: (goalId: String) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel = koinViewModel<ActiveGoalsViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.event.collectAsStateWithLifecycle(initialValue = GoalsEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState
    )

    ActiveGoalsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarState = snackbarState,
        uiState = uiState,
        fetchMoreData = viewModel::fetchMoreData,
        onAddGoal = onNavigateToAddGoal,
        onGoalClicked = { onNavigateToGoalDetails(it.id) }
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<GoalsEvents>,
    snackbarState: SnackbarHostState,
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

            else -> {}
        }
    }
}