package com.apimorlabs.reluct.screens.settings

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_blocking_turned_off_text
import com.apimorlabs.reluct.compose.ui.app_blocking_turned_on_text
import com.apimorlabs.reluct.compose.ui.dnd_off_msg
import com.apimorlabs.reluct.compose.ui.dnd_on_msg
import com.apimorlabs.reluct.compose.ui.focus_mode_off_msg
import com.apimorlabs.reluct.compose.ui.focus_mode_on_msg
import com.apimorlabs.reluct.compose.ui.themes_changed_text
import com.apimorlabs.reluct.features.settings.AppSettingsViewModel
import com.apimorlabs.reluct.features.settings.states.SettingsEvents
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    onNavigateToManageAcc: () -> Unit,
    goBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: AppSettingsViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = SettingsEvents.Nothing)


    HandleEvents(
        eventsState = events,
        snackbarHostState = snackbarHostState,
    )

    SettingsUI(
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        onSaveTheme = viewModel::saveThemeSettings,
        onToggleDnd = viewModel::toggleDnd,
        onToggleFocusMode = viewModel::toggleFocusMode,
        onToggleAppBlocking = viewModel::toggleAppBlocking,
        onManageAccount = onNavigateToManageAcc,
        onBackClicked = goBack
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<SettingsEvents>,
    snackbarHostState: SnackbarHostState,
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is SettingsEvents.ThemeChanged -> {
                val msg = getString(Res.string.themes_changed_text)
                launch {
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is SettingsEvents.DndChanged -> {
                val msg = if (events.isEnabled) {
                    getString(Res.string.dnd_on_msg)
                } else {
                    getString(Res.string.dnd_off_msg)
                }
                launch {
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is SettingsEvents.FocusModeChanged -> {
                val msg = if (events.isEnabled) {
                    getString(Res.string.focus_mode_on_msg)
                } else {
                    getString(Res.string.focus_mode_off_msg)
                }
                launch {
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is SettingsEvents.AppBlockingChanged -> {
                val msg = if (events.isEnabled) {
                    getString(Res.string.app_blocking_turned_on_text)
                } else {
                    getString(Res.string.app_blocking_turned_off_text)
                }
                launch {
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            else -> {}
        }
    }
}