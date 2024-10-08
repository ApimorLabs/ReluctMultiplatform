package com.apimorlabs.reluct.screens.screentime.limits

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.features.screenTime.limits.ScreenTimeLimitsViewModel
import com.apimorlabs.reluct.features.screenTime.states.events.ScreenTimeLimitsEvents
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScreenTimeLimitsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAppUsageInfo: (packageName: String) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: ScreenTimeLimitsViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events =
        viewModel.events.collectAsStateWithLifecycle(initialValue = ScreenTimeLimitsEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState,
        navigateToAppUsageInfo = { onNavigateToAppUsageInfo(it) }
    )

    ScreenTimeLimitsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarState = snackbarState,
        uiState = uiState,
        toggleFocusMode = viewModel::toggleFocusMode,
        toggleDnd = viewModel::toggleDnd,
        getDistractingApps = viewModel::getDistractingApps,
        pauseApp = viewModel::pauseApp,
        resumeApp = viewModel::unPauseApp,
        makeDistractingApp = viewModel::markAsDistracting,
        removeDistractingApp = viewModel::markAsNonDistracting
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<ScreenTimeLimitsEvents>,
    snackbarState: SnackbarHostState,
    navigateToAppUsageInfo: (packageName: String) -> Unit,
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is ScreenTimeLimitsEvents.Navigation.NavigateToAppInfo -> {
                navigateToAppUsageInfo(events.packageName)
            }

            is ScreenTimeLimitsEvents.Navigation.OpenAppTimerSettings -> {
            }

            is ScreenTimeLimitsEvents.ShowMessageDone -> {
                launch {
                    snackbarState.showSnackbar(
                        message = events.msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            else -> {}
        }
    }
}
