package com.apimorlabs.reluct.screens.screentime.limits

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AppShortcut
import androidx.compose.material.icons.rounded.DoNotDisturbOnTotalSilence
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.apps_text
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.distracting_apps_text
import com.apimorlabs.reluct.compose.ui.distractions_header
import com.apimorlabs.reluct.compose.ui.focus_mode_header
import com.apimorlabs.reluct.compose.ui.manage_distracting_apps
import com.apimorlabs.reluct.compose.ui.manage_distracting_apps_desc
import com.apimorlabs.reluct.compose.ui.manually_pause_apps
import com.apimorlabs.reluct.compose.ui.no_paused_apps_text
import com.apimorlabs.reluct.compose.ui.non_distracting_apps_text
import com.apimorlabs.reluct.compose.ui.paused_apps_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.turn_on_dnd
import com.apimorlabs.reluct.compose.ui.turn_on_dnd_desc
import com.apimorlabs.reluct.compose.ui.turn_on_focus
import com.apimorlabs.reluct.compose.ui.turn_on_focus_desc
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.compose.ui.util.rememberScrollContext
import com.apimorlabs.reluct.features.screenTime.states.DistractingAppsState
import com.apimorlabs.reluct.features.screenTime.states.PausedAppsState
import com.apimorlabs.reluct.features.screenTime.states.ScreenTimeLimitState
import com.apimorlabs.reluct.screens.screentime.components.AppNameEntry
import com.apimorlabs.reluct.screens.screentime.components.LimitsDetailsCard
import com.apimorlabs.reluct.screens.screentime.components.LimitsSwitchCard
import com.apimorlabs.reluct.screens.screentime.components.ManageAppsDialog
import com.apimorlabs.reluct.util.BottomBarVisibilityHandler
import com.apimorlabs.reluct.util.getSnackbarModifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
internal fun ScreenTimeLimitsUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarState: SnackbarHostState,
    uiState: State<ScreenTimeLimitState>,
    toggleFocusMode: (Boolean) -> Unit,
    toggleDnd: (Boolean) -> Unit,
    getDistractingApps: () -> Unit,
    pauseApp: (packageName: String) -> Unit,
    resumeApp: (packageName: String) -> Unit,
    makeDistractingApp: (packageName: String) -> Unit,
    removeDistractingApp: (packageName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    val focusModeState = remember { derivedStateOf { uiState.value.focusModeState } }
    val pausedAppsState = remember { derivedStateOf { uiState.value.pausedAppsState } }

    val focusModeContainerColor by animateColorAsState(
        targetValue = if (focusModeState.value.focusModeOn) {
            Color.Green.copy(alpha = .7f)
        } else {
            MaterialTheme.colorScheme.error.copy(alpha = .8f)
        }
    )

    val focusModeContentColor by animateColorAsState(
        targetValue = if (focusModeState.value.focusModeOn) {
            Color.Black
        } else {
            MaterialTheme.colorScheme.onError
        }
    )

    // Dialogs Open State
    val showDistractingAppsDialog = remember { mutableStateOf(false) }
    val showPausedAppsDialog = remember { mutableStateOf(false) }

    val snackbarModifier = getSnackbarModifier(
        mainPadding = mainScaffoldPadding,
        scrollContext = scrollContext
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    modifier = snackbarModifier.value,
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->

        Box(
            modifier = Modifier
                .animateContentSize()
                .padding(padding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
            ) {
                // Focus Mode Header
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(Res.string.focus_mode_header))
                }

                // Turn On Focus Mode
                item {
                    LimitsSwitchCard(
                        title = stringResource(Res.string.turn_on_focus),
                        description = stringResource(Res.string.turn_on_focus_desc),
                        checked = focusModeState.value.focusModeOn,
                        onCheckedChange = { toggleFocusMode(it) },
                        icon = Icons.Rounded.AppShortcut,
                        containerColor = focusModeContainerColor,
                        contentColor = focusModeContentColor
                    )
                }

                // Enable DND
                item {
                    LimitsSwitchCard(
                        title = stringResource(Res.string.turn_on_dnd),
                        description = stringResource(Res.string.turn_on_dnd_desc),
                        checked = focusModeState.value.doNotDisturbOn,
                        onCheckedChange = { toggleDnd(it) },
                        icon = Icons.Rounded.DoNotDisturbOnTotalSilence
                    )
                }

                // Distractions Header
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(Res.string.distractions_header))
                }

                // Manage Distracting Apps
                item {
                    LimitsDetailsCard(
                        title = stringResource(Res.string.manage_distracting_apps),
                        description = stringResource(Res.string.manage_distracting_apps_desc),
                        onClick = {
                            getDistractingApps()
                            showDistractingAppsDialog.value = true
                        }
                    )
                }

                // Manually Pause Apps
                item {
                    LimitsDetailsCard(
                        title = stringResource(Res.string.manually_pause_apps),
                        description = stringResource(Res.string.manage_distracting_apps_desc),
                        onClick = { showPausedAppsDialog.value = true }
                    ) {
                        // Bottom Content
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            thickness = Dimens.MediumPadding.size
                        )
                        pausedAppsState.value.pausedApps.take(4).forEach { app ->
                            AppNameEntry(
                                modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
                                appName = app.appName,
                                icon = app.appIcon.icon.decodeToImageBitmap()
                            )
                        }
                        if (pausedAppsState.value is PausedAppsState.Loading) {
                            LinearProgressIndicator(
                                Modifier
                                    .fillMaxSize()
                                    .padding(Dimens.LargePadding.size)
                            )
                        } else if (pausedAppsState.value.pausedApps.isEmpty()) {
                            Text(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(Dimens.LargePadding.size),
                                text = stringResource(Res.string.no_paused_apps_text),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Add Padding At the Bottom
                        Spacer(modifier = Modifier.height(Dimens.MediumPadding.size))
                    }
                }

                // Bottom Space for spaceBy
                item {
                    Spacer(
                        modifier = Modifier.padding(mainScaffoldPadding)
                    )
                }
            }
        }
    }

    // All Dialogs
    ManageAppsDialog(
        openDialog = showDistractingAppsDialog,
        onDismiss = { showDistractingAppsDialog.value = false },
        isLoadingProvider = { uiState.value.distractingAppsState is DistractingAppsState.Loading },
        topItemsHeading = stringResource(Res.string.distracting_apps_text),
        bottomItemsHeading = stringResource(Res.string.non_distracting_apps_text),
        topItems = { uiState.value.distractingAppsState.distractingApps },
        bottomItems = { uiState.value.distractingAppsState.otherApps },
        onTopItemClicked = { removeDistractingApp(it.packageName) },
        onBottomItemClicked = { makeDistractingApp(it.packageName) }
    )

    ManageAppsDialog(
        openDialog = showPausedAppsDialog,
        onDismiss = { showPausedAppsDialog.value = false },
        isLoadingProvider = { uiState.value.pausedAppsState is PausedAppsState.Loading },
        topItemsHeading = stringResource(Res.string.paused_apps_text),
        bottomItemsHeading = stringResource(Res.string.apps_text),
        topItems = { uiState.value.pausedAppsState.pausedApps },
        bottomItems = { uiState.value.pausedAppsState.unPausedApps },
        onTopItemClicked = { resumeApp(it.packageName) },
        onBottomItemClicked = { pauseApp(it.packageName) }
    )
}