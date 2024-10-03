package com.apimorlabs.reluct.screens.screentime.statistics

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.time_limit_change_arg
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.features.screenTime.states.events.ScreenTimeStatsEvents
import com.apimorlabs.reluct.features.screenTime.statistics.ScreenTimeStatsViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScreenTimeStatisticsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAppUsageInfo: (packageName: String) -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: ScreenTimeStatsViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events =
        viewModel.events.collectAsStateWithLifecycle(initialValue = ScreenTimeStatsEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState,
        navigateToAppUsageInfo = { onNavigateToAppUsageInfo(it) }
    )

    ScreenTimeStatisticsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarHostState = snackbarState,
        uiState = uiState,
        getUsageData = viewModel::permissionCheck,
        onSelectDay = viewModel::selectDay,
        onUpdateWeekOffset = viewModel::updateWeekOffset,
        onAppUsageInfoClick = { appInfo -> viewModel.navigateToAppInfo(appInfo.packageName) },
        onAppTimeLimitSettingsClicked = viewModel::selectAppTimeLimit,
        onSaveAppTimeLimitSettings = viewModel::saveTimeLimit
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<ScreenTimeStatsEvents>,
    snackbarState: SnackbarHostState,
    navigateToAppUsageInfo: (packageName: String) -> Unit,
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is ScreenTimeStatsEvents.TimeLimitChange -> {
                val message =
                    getString(Res.string.time_limit_change_arg, events.app.appInfo.appName)
                launch {
                    snackbarState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is ScreenTimeStatsEvents.Navigation.NavigateToAppInfo -> {
                navigateToAppUsageInfo(events.packageName)
            }

            is ScreenTimeStatsEvents.Navigation.OpenAppTimerSettings -> {
            }

            else -> {}
        }
    }
}
