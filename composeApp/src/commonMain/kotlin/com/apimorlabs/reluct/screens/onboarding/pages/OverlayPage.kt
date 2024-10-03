package com.apimorlabs.reluct.screens.onboarding.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AppBlocking
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_blocking_desc_text
import com.apimorlabs.reluct.compose.ui.app_blocking_text
import com.apimorlabs.reluct.compose.ui.cancel
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.display_over_apps_desc_text
import com.apimorlabs.reluct.compose.ui.find_draw_over_apps_text
import com.apimorlabs.reluct.compose.ui.ok
import com.apimorlabs.reluct.compose.ui.open_settings_dialog_title
import com.apimorlabs.reluct.compose.ui.overlay_permissions_rationale_dialog_text
import com.apimorlabs.reluct.compose.ui.screens_present
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.turn_on_app_blocking_text
import com.apimorlabs.reluct.screens.onboarding.components.PermissionStatusCard
import com.apimorlabs.reluct.screens.screentime.components.LimitsSwitchCard
import com.apimorlabs.reluct.util.BackPressHandler
import com.apimorlabs.reluct.util.PermissionCheckHandler
import com.apimorlabs.reluct.util.PermissionsManager
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun OverlayPage(
    isGranted: Boolean,
    permManager: State<PermissionsManager?>,
    isAppBlockingEnabled: Boolean,
    updatePermissionCheck: (isGranted: Boolean) -> Unit,
    onToggleAppBlocking: (isEnabled: Boolean) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackPressHandler { goBack() } // Handle Back Presses

    val drawableSize = 200.dp

    val openDialog = remember { mutableStateOf(false) }

    val overlayMsgTxt = stringResource(Res.string.find_draw_over_apps_text)

    PermissionCheckHandler {
        if (!isGranted) {
            updatePermissionCheck(permManager.value?.checkCanScheduleAlarms() ?: false)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size) then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            ListGroupHeadingHeader(
                text = stringResource(Res.string.app_blocking_text),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 40.sp)
            )
        }

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize),
                painter = painterResource(Res.drawable.screens_present),
                contentDescription = null
            )
        }

        // App Blocking Switch
        item {
            LimitsSwitchCard(
                title = stringResource(Res.string.turn_on_app_blocking_text),
                description = stringResource(Res.string.app_blocking_desc_text),
                checked = isAppBlockingEnabled,
                onCheckedChange = onToggleAppBlocking,
                icon = Icons.Rounded.AppBlocking
            )
        }

        item {
            Text(
                text = stringResource(Res.string.display_over_apps_desc_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }

        item {
            PermissionStatusCard(
                modifier = Modifier.padding(vertical = Dimens.MediumPadding.size),
                isGranted = isGranted
            ) {
                if (!isGranted) {
                    openDialog.value = true
                }
            }
        }
    }

    // Display over other apps Dialog
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = stringResource(Res.string.open_settings_dialog_title))
            },
            text = {
                Text(text = stringResource(Res.string.overlay_permissions_rationale_dialog_text))
            },
            confirmButton = {
                ReluctButton(
                    buttonText = stringResource(Res.string.ok),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        openDialog.value = false
                        permManager.value?.requestOverlayPermission(overlayMsgTxt)
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
                    onButtonClicked = { openDialog.value = false }
                )
            }
        )
    }
}
