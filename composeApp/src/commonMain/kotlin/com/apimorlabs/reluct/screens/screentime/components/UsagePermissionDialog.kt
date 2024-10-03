package com.apimorlabs.reluct.screens.screentime.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.cancel
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.ok
import com.apimorlabs.reluct.compose.ui.open_settings_dialog_title
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.usage_permissions_rationale_dialog_text
import com.apimorlabs.reluct.screens.util.GetPermissionsManager
import com.apimorlabs.reluct.screens.util.PermissionsManager
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun UsagePermissionDialog(
    openDialog: State<Boolean>,
    onClose: () -> Unit
) {
    if (openDialog.value) {
        var permManager: PermissionsManager? = remember { null }
        GetPermissionsManager(
            onPermissionsManager = { permManager = it }
        )

        AlertDialog(
            onDismissRequest = onClose,
            title = {
                Text(text = stringResource(Res.string.open_settings_dialog_title))
            },
            text = {
                Text(text = stringResource(Res.string.usage_permissions_rationale_dialog_text))
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
                        permManager?.requestUsageAccessPermission()
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