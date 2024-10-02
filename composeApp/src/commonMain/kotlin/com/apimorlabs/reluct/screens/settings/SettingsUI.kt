package com.apimorlabs.reluct.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_blocking_desc_text
import com.apimorlabs.reluct.compose.ui.app_blocking_text
import com.apimorlabs.reluct.compose.ui.components.cards.cardWithActions.ReluctDescriptionCard
import com.apimorlabs.reluct.compose.ui.components.topBar.ReluctSmallTopAppBar
import com.apimorlabs.reluct.compose.ui.manage_acc_desc
import com.apimorlabs.reluct.compose.ui.manage_acc_text
import com.apimorlabs.reluct.compose.ui.settings_text
import com.apimorlabs.reluct.compose.ui.support_development_desc_text
import com.apimorlabs.reluct.compose.ui.support_development_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.themes_desc_text
import com.apimorlabs.reluct.compose.ui.themes_text
import com.apimorlabs.reluct.compose.ui.turn_on_dnd
import com.apimorlabs.reluct.compose.ui.turn_on_dnd_desc
import com.apimorlabs.reluct.compose.ui.turn_on_focus
import com.apimorlabs.reluct.compose.ui.turn_on_focus_desc
import com.apimorlabs.reluct.features.settings.states.SettingsState
import com.apimorlabs.reluct.screens.settings.components.AppAboutInfo
import com.apimorlabs.reluct.screens.settings.components.ThemesDialog
import org.jetbrains.compose.resources.stringResource
import work.racka.reluct.android.screens.screentime.components.LimitsDetailsCard
import work.racka.reluct.android.screens.screentime.components.LimitsSwitchCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsUI(
    snackbarHostState: SnackbarHostState,
    uiState: State<SettingsState>,
    onSaveTheme: (value: Int) -> Unit,
    onToggleDnd: (value: Boolean) -> Unit,
    onToggleFocusMode: (value: Boolean) -> Unit,
    onToggleAppBlocking: (value: Boolean) -> Unit,
    onManageAccount: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val openThemeDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            ReluctSmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = stringResource(Res.string.settings_text),
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
                    snackbarData = it,
                    shape = RoundedCornerShape(10.dp),
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(Dimens.MediumPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
        ) {
            // Account Settings
            item {
                ReluctDescriptionCard(
                    title = {
                        Text(
                            text = stringResource(Res.string.manage_acc_text),
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalContentColor.current
                        )
                    },
                    description = {
                        Text(
                            text = stringResource(Res.string.manage_acc_desc),
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = .8f)
                        )
                    },
                    leftItems = {
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = null
                        )
                    },
                    rightItems = {
                        Icon(
                            imageVector = Icons.Rounded.ChevronRight,
                            contentDescription = "Open"
                        )
                    },
                    onClick = onManageAccount
                )
            }

            // Theme Settings
            item {
                ReluctDescriptionCard(
                    title = {
                        Text(
                            text = stringResource(Res.string.themes_text),
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalContentColor.current
                        )
                    },
                    description = {
                        Text(
                            text = stringResource(Res.string.themes_desc_text),
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = .8f)
                        )
                    },
                    leftItems = {
                        Icon(
                            imageVector = Icons.Rounded.DarkMode,
                            contentDescription = null
                        )
                    },
                    rightItems = {
                        Icon(
                            imageVector = Icons.Rounded.ChevronRight,
                            contentDescription = "Open"
                        )
                    },
                    onClick = { openThemeDialog.value = true }
                )
            }

            // Turn On Focus Mode
            item {
                LimitsSwitchCard(
                    title = stringResource(Res.string.turn_on_focus),
                    description = stringResource(Res.string.turn_on_focus_desc),
                    checked = uiState.value.limitSettings.focusModeOn,
                    onCheckedChange = onToggleFocusMode,
                    icon = Icons.Rounded.AppShortcut
                )
            }

            // App Blocking
            item {
                LimitsSwitchCard(
                    title = stringResource(Res.string.app_blocking_text),
                    description = stringResource(Res.string.app_blocking_desc_text),
                    checked = uiState.value.limitSettings.appBlockingEnabled,
                    onCheckedChange = onToggleAppBlocking,
                    icon = Icons.Rounded.AppBlocking
                )
            }

            // Enable DND
            item {
                LimitsSwitchCard(
                    title = stringResource(Res.string.turn_on_dnd),
                    description = stringResource(Res.string.turn_on_dnd_desc),
                    checked = uiState.value.limitSettings.dndOn,
                    onCheckedChange = onToggleDnd,
                    icon = Icons.Rounded.DoNotDisturbOnTotalSilence
                )
            }

            // Support Development
            item {
                LimitsDetailsCard(
                    title = stringResource(Res.string.support_development_text),
                    description = stringResource(Res.string.support_development_desc_text),
                    icon = Icons.Rounded.Favorite,
                    onClick = {}
                )
            }

            // About Details
            item {
                AppAboutInfo()
            }

            // Bottom Space
            item {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }

    ThemesDialog(
        openDialog = openThemeDialog,
        onDismiss = { openThemeDialog.value = false },
        currentTheme = uiState.value.themeValue,
        onSaveTheme = onSaveTheme
    )
}
