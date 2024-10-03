package com.apimorlabs.reluct.screens.screentime.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.dialogs.CircularProgressDialog
import com.apimorlabs.reluct.compose.ui.loading_text
import com.apimorlabs.reluct.features.screenTime.states.AppTimeLimitState
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ShowAppTimeLimitDialog(
    openDialog: State<Boolean>,
    limitStateProvider: () -> AppTimeLimitState,
    onSaveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    onClose: () -> Unit,
) {
    if (openDialog.value) {
        when (val limitState = limitStateProvider()) {
            is AppTimeLimitState.Data -> {
                AppTimeLimitDialog(
                    onDismiss = onClose,
                    initialAppTimeLimit = limitState.timeLimit,
                    onSaveTimeLimit = onSaveTimeLimit
                )
            }

            else -> {
                CircularProgressDialog(
                    onDismiss = onClose,
                    loadingText = stringResource(Res.string.loading_text),
                    isVisible = openDialog
                )
            }
        }
    }
}
