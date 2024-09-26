package com.apimorlabs.reluct.compose.ui.components.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.cancel
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.ok
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiscardPromptDialog(
    dialogTitleProvider: () -> String,
    dialogTextProvider: () -> String,
    openDialog: State<Boolean>,
    onClose: () -> Unit,
    onPositiveClick: () -> Unit,
    modifier: Modifier = Modifier,
    properties: MultiplatformDialogProperties = MultiplatformDialogProperties()
) {
    val title = remember { derivedStateOf(dialogTitleProvider) }
    val text = remember { derivedStateOf(dialogTextProvider) }
    MultiplatformAlertDialog(
        isVisible = openDialog.value,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        onCloseDialog = onClose,
        properties = properties,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        title = { Text(text = title.value, style = MaterialTheme.typography.titleMedium) },
        text = { Text(text = text.value, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            ReluctButton(
                buttonText = stringResource(Res.string.ok),
                icon = null,
                shape = Shapes.large,
                buttonColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onButtonClicked = {
                    onClose()
                    onPositiveClick()
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
