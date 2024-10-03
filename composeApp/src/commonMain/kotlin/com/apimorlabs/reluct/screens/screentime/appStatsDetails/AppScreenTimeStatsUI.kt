package com.apimorlabs.reluct.screens.screentime.appStatsDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AppBlocking
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_settings_header
import com.apimorlabs.reluct.compose.ui.app_time_limit
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.components.dialogs.CircularProgressDialog
import com.apimorlabs.reluct.compose.ui.components.statistics.charts.screenTime.ScreenTimeStatisticsCard
import com.apimorlabs.reluct.compose.ui.components.statistics.helpers.BarChartDefaults
import com.apimorlabs.reluct.compose.ui.distracting_app
import com.apimorlabs.reluct.compose.ui.distracting_app_desc
import com.apimorlabs.reluct.compose.ui.loading_text
import com.apimorlabs.reluct.compose.ui.manually_pause_app
import com.apimorlabs.reluct.compose.ui.manually_pause_app_desc
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.features.screenTime.states.AppScreenTimeStatsState
import com.apimorlabs.reluct.features.screenTime.states.AppSettingsState
import com.apimorlabs.reluct.features.screenTime.states.DailyAppUsageStatsState
import com.apimorlabs.reluct.features.screenTime.states.WeeklyAppUsageStatsState
import com.apimorlabs.reluct.screens.screentime.components.AppNameEntry
import com.apimorlabs.reluct.screens.screentime.components.AppTimeLimitDialog
import com.apimorlabs.reluct.screens.screentime.components.LimitsDetailsCard
import com.apimorlabs.reluct.screens.screentime.components.LimitsSwitchCard
import com.apimorlabs.reluct.screens.screentime.components.ScreenTimeWeekSelectorButton
import com.apimorlabs.reluct.screens.screentime.components.getWeeklyAppScreenTimeChartData
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AppScreenTimeStatsUI(
    snackbarHostState: SnackbarHostState,
    uiState: State<AppScreenTimeStatsState>,
    toggleDistractingState: (value: Boolean) -> Unit,
    togglePausedState: (value: Boolean) -> Unit,
    saveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    onSelectDay: (dayIsoNumber: Int) -> Unit,
    onUpdateWeekOffset: (offset: Int) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val barColor = BarChartDefaults.barColor
    val barChartData = getWeeklyAppScreenTimeChartData(
        weeklyStatsProvider = { uiState.value.weeklyData.usageStats },
        isLoadingProvider = { uiState.value.weeklyData is WeeklyAppUsageStatsState.Loading },
        barColor = barColor
    )

    val listState = rememberLazyListState()
    val showAppTimeLimitDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    TopAppInfoItem(
                        modifier = Modifier.padding(bottom = Dimens.SmallPadding.size),
                        dailyData = uiState.value.dailyData
                    )
                },
                modifier = Modifier.statusBarsPadding(),
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = appBarColors
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
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
                verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Space
                item {
                    Spacer(modifier = Modifier)
                }

                // Chart
                item {
                    ScreenTimeStatisticsCard(
                        chartData = barChartData,
                        selectedDayText = {
                            when (val data = uiState.value.dailyData) {
                                is DailyAppUsageStatsState.Data -> data.dayText
                                else -> "..."
                            }
                        },
                        selectedDayScreenTime = {
                            when (val data = uiState.value.dailyData) {
                                is DailyAppUsageStatsState.Data -> {
                                    data.usageStat.appUsageInfo.formattedTimeInForeground
                                }

                                else -> "..."
                            }
                        },
                        weeklyTotalScreenTime = { uiState.value.weeklyData.formattedTotalTime },
                        selectedDayIndex = { uiState.value.selectedInfo.selectedDay },
                        onBarClicked = { onSelectDay(it) },
                        weekUpdateButton = {
                            ScreenTimeWeekSelectorButton(
                                selectedInfoProvider = { uiState.value.selectedInfo },
                                onUpdateWeekOffset = onUpdateWeekOffset
                            )
                        }
                    )
                }

                // App Settings Header
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(Res.string.app_settings_header))
                }

                // App Extra Actions
                appExtraConfiguration(
                    getAppSettingsState = { uiState.value.appSettingsState },
                    onShowAppTimeLimitDialog = { showAppTimeLimitDialog.value = true },
                    onToggleDistractingState = toggleDistractingState,
                    onTogglePausedState = togglePausedState
                )

                // Bottom Space
                item {
                    Spacer(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .height(padding.calculateBottomPadding())
                    )
                }
            }
        }
    }

    // App Time Limit Dialog
    ShowAppTimeLimitDialog(
        openDialog = showAppTimeLimitDialog,
        appSettingsStateProvider = { uiState.value.appSettingsState },
        onSaveTimeLimit = saveTimeLimit,
        onClose = { showAppTimeLimitDialog.value = false }
    )
}

private fun LazyListScope.appExtraConfiguration(
    getAppSettingsState: () -> AppSettingsState,
    onShowAppTimeLimitDialog: (Boolean) -> Unit,
    onToggleDistractingState: (Boolean) -> Unit,
    onTogglePausedState: (Boolean) -> Unit
) {
    when (val appSettings = getAppSettingsState()) {
        is AppSettingsState.Data -> {
            // App Timer
            item {
                LimitsDetailsCard(
                    title = stringResource(Res.string.app_time_limit),
                    description = appSettings.appTimeLimit.formattedTime,
                    onClick = { onShowAppTimeLimitDialog(true) },
                    icon = Icons.Rounded.HourglassBottom
                )
            }

            // Distracting App
            item {
                LimitsSwitchCard(
                    title = stringResource(Res.string.distracting_app),
                    description = stringResource(Res.string.distracting_app_desc),
                    checked = appSettings.isDistractingApp,
                    onCheckedChange = onToggleDistractingState,
                    icon = Icons.Rounded.AppBlocking
                )
            }

            // Pause App
            item {
                LimitsSwitchCard(
                    title = stringResource(Res.string.manually_pause_app),
                    description = stringResource(Res.string.manually_pause_app_desc),
                    checked = appSettings.isPaused,
                    onCheckedChange = onTogglePausedState,
                    icon = Icons.Rounded.PauseCircle
                )
            }
        }

        else -> {
            item {
                LinearProgressIndicator()
            }
        }
    }
}

@Composable
private fun ShowAppTimeLimitDialog(
    openDialog: State<Boolean>,
    appSettingsStateProvider: () -> AppSettingsState,
    onSaveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    onClose: () -> Unit,
) {
    if (openDialog.value) {
        when (val appSettings = appSettingsStateProvider()) {
            is AppSettingsState.Data -> {
                AppTimeLimitDialog(
                    onDismiss = onClose,
                    initialAppTimeLimit = appSettings.appTimeLimit,
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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun TopAppInfoItem(
    dailyData: DailyAppUsageStatsState,
    modifier: Modifier = Modifier,
) {
    val appInfo by remember(dailyData) {
        derivedStateOf {
            if (dailyData is DailyAppUsageStatsState.Data) {
                dailyData.usageStat.appUsageInfo
            } else {
                null
            }
        }
    }

    /**
     * This all done to prevent flicker when new data is selected
     * Obtaining these directly from [dailyData] will cause multiple recomposition with
     * evaluation that will flicker the component.
     */
    val appIcon by remember(appInfo?.appName) { derivedStateOf { appInfo?.appIcon?.icon } }
    val appName by remember(appInfo?.appIcon) { derivedStateOf { appInfo?.appName } }

    Box(
        modifier = Modifier.height(Dimens.ExtraLargePadding.size),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = appName != null && appIcon != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AppNameEntry(
                modifier = modifier,
                appName = appName!!,
                icon = appIcon!!.decodeToImageBitmap(),
                textStyle = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val appBarColors: TopAppBarColors
    @Composable
    get() = TopAppBarDefaults
        .mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = LocalContentColor.current,
            titleContentColor = LocalContentColor.current,
            actionIconContentColor = LocalContentColor.current
        )
