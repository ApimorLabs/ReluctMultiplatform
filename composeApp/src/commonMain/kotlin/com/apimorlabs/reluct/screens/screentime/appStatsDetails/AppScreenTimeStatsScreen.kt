package com.apimorlabs.reluct.screens.screentime.appStatsDetails

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.time_limit_change_arg
import com.apimorlabs.reluct.features.screenTime.states.events.ScreenTimeStatsEvents
import com.apimorlabs.reluct.features.screenTime.statistics.AppScreenTimeStatsViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppScreenTimeStatsScreen(
    packageName: String,
    onBackClicked: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: AppScreenTimeStatsViewModel = koinViewModel { parametersOf(packageName) }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events =
        viewModel.events.collectAsStateWithLifecycle(initialValue = ScreenTimeStatsEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarHostState = snackbarHostState
    )

    AppScreenTimeStatsUI(
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        toggleDistractingState = viewModel::toggleDistractingState,
        togglePausedState = viewModel::togglePausedState,
        saveTimeLimit = viewModel::saveTimeLimit,
        onSelectDay = viewModel::selectDay,
        onUpdateWeekOffset = viewModel::updateWeekOffset,
        goBack = onBackClicked
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<ScreenTimeStatsEvents>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is ScreenTimeStatsEvents.ShowMessageDone -> {
                val message = events.msg
                launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is ScreenTimeStatsEvents.TimeLimitChange -> {
                val message =
                    getString(Res.string.time_limit_change_arg, events.app.appInfo.appName)
                launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            else -> {}
        }
    }
}