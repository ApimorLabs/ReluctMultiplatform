package com.apimorlabs.reluct.screens.screentime.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageInfo
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_list_header
import com.apimorlabs.reluct.compose.ui.components.buttons.ScrollToTop
import com.apimorlabs.reluct.compose.ui.components.cards.appUsageEntry.AppUsageEntry
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.components.cards.permission.PermissionsCard
import com.apimorlabs.reluct.compose.ui.components.images.ImageWithDescription
import com.apimorlabs.reluct.compose.ui.components.statistics.charts.screenTime.ScreenTimeStatisticsCard
import com.apimorlabs.reluct.compose.ui.components.statistics.helpers.BarChartDefaults
import com.apimorlabs.reluct.compose.ui.empty
import com.apimorlabs.reluct.compose.ui.no_usage_data_text
import com.apimorlabs.reluct.compose.ui.permissions_unlock
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.usage_permissions_details
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.compose.ui.util.BottomBarVisibilityHandler
import com.apimorlabs.reluct.compose.ui.util.rememberScrollContext
import com.apimorlabs.reluct.features.screenTime.states.DailyUsageStatsState
import com.apimorlabs.reluct.features.screenTime.states.ScreenTimeStatsState
import com.apimorlabs.reluct.features.screenTime.states.WeeklyUsageStatsState
import com.apimorlabs.reluct.screens.screentime.components.ScreenTimeWeekSelectorButton
import com.apimorlabs.reluct.screens.screentime.components.ShowAppTimeLimitDialog
import com.apimorlabs.reluct.screens.screentime.components.UsagePermissionDialog
import com.apimorlabs.reluct.screens.screentime.components.getWeeklyDeviceScreenTimeChartData
import com.apimorlabs.reluct.screens.util.GetPermissionsManager
import com.apimorlabs.reluct.screens.util.PermissionCheckHandler
import com.apimorlabs.reluct.screens.util.PermissionsManager
import com.apimorlabs.reluct.screens.util.getSnackbarModifier
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ScreenTimeStatisticsUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarHostState: SnackbarHostState,
    uiState: State<ScreenTimeStatsState>,
    getUsageData: (isGranted: Boolean) -> Unit,
    onSelectDay: (dayIsoNumber: Int) -> Unit,
    onUpdateWeekOffset: (weekOffsetValue: Int) -> Unit,
    onAppUsageInfoClick: (app: AppUsageInfo) -> Unit,
    onAppTimeLimitSettingsClicked: (packageName: String) -> Unit,
    onSaveAppTimeLimitSettings: (hours: Int, minutes: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    val scope = rememberCoroutineScope()

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    // Screen Time Chart
    val barColor = BarChartDefaults.barColor
    val screenTimeChartData = getWeeklyDeviceScreenTimeChartData(
        weeklyStatsProvider = { uiState.value.weeklyData.usageStats },
        isLoadingProvider = { uiState.value.weeklyData is WeeklyUsageStatsState.Loading },
        barColor = barColor
    )

    val usagePermissionGranted = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    val showAppTimeLimitDialog = remember { mutableStateOf(false) }

    // Permission Manager
    var permManager: PermissionsManager? = remember { null }
    GetPermissionsManager(onPermissionsManager = { permManager = it })

    PermissionCheckHandler {
        if (!usagePermissionGranted.value && permManager != null) {
            usagePermissionGranted.value = permManager?.checkUsageAccessPermission() ?: false
            getUsageData(usagePermissionGranted.value)
        }
    }

    val snackbarModifier = getSnackbarModifier(
        mainPadding = mainScaffoldPadding,
        scrollContext = scrollContext
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
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
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement
                    .spacedBy(Dimens.SmallPadding.size)
            ) {
                // Top Space
                item {
                    Spacer(modifier = Modifier)
                }

                // Permission Card
                if (!usagePermissionGranted.value) {
                    item {
                        PermissionsCard(
                            imageSlot = {
                                ImageWithDescription(
                                    painter = painterResource(Res.drawable.permissions_unlock),
                                    imageSize = 200.dp,
                                    description = null,
                                    descriptionTextStyle = MaterialTheme.typography.bodyLarge
                                )
                            },
                            permissionDetails = stringResource(Res.string.usage_permissions_details),
                            onPermissionRequest = { openDialog.value = true }
                        )
                    }
                }

                // Chart
                item {
                    ScreenTimeStatisticsCard(
                        chartData = screenTimeChartData,
                        selectedDayText = { uiState.value.dailyData.dayText },
                        selectedDayScreenTime = { uiState.value.dailyData.usageStat.formattedTotalScreenTime },
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

                // Apps Header
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(Res.string.app_list_header))
                }

                dailyAppStatsList(
                    dailyDataProvider = { uiState.value.dailyData },
                    isLoadingProvider = { uiState.value.dailyData is DailyUsageStatsState.Loading },
                    onAppUsageInfoClick = onAppUsageInfoClick,
                    onAppTimeLimitSettingsClicked = onAppTimeLimitSettingsClicked,
                    onShowLimitDialog = { showAppTimeLimitDialog.value = true }
                )

                // Bottom Space for spaceBy
                item {
                    Spacer(
                        modifier = Modifier.padding(mainScaffoldPadding)
                    )
                }
            }

            // Scroll To Top
            ScrollToTop(
                scrollContext = scrollContext,
                onScrollToTop = { scope.launch { listState.animateScrollToItem(0) } }
            )
        }

        // Go To Usage Access Dialog
        UsagePermissionDialog(openDialog = openDialog, onClose = { openDialog.value = false })

        // App Time Limit Dialog
        ShowAppTimeLimitDialog(
            openDialog = showAppTimeLimitDialog,
            limitStateProvider = { uiState.value.appTimeLimit },
            onSaveTimeLimit = onSaveAppTimeLimitSettings,
            onClose = { showAppTimeLimitDialog.value = false }
        )
    }
}

private fun LazyListScope.dailyAppStatsList(
    dailyDataProvider: () -> DailyUsageStatsState,
    isLoadingProvider: () -> Boolean,
    onAppUsageInfoClick: (app: AppUsageInfo) -> Unit,
    onAppTimeLimitSettingsClicked: (packageName: String) -> Unit,
    onShowLimitDialog: () -> Unit
) {
    // Daily Data Loading
    item {
        val isLoading = remember { derivedStateOf { isLoadingProvider() } }
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = isLoading.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator()
            }
        }
    }

    // No App Data Animation
    item {
        val isEmpty = remember {
            derivedStateOf { dailyDataProvider() is DailyUsageStatsState.Empty }
        }
        AnimatedVisibility(
            visible = isEmpty.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ImageWithDescription(
                    painter = painterResource(Res.drawable.empty),
                    imageSize = 200.dp,
                    description = stringResource(Res.string.no_usage_data_text),
                    descriptionTextStyle = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    items(
        items = dailyDataProvider().usageStat.appsUsageList,
        key = { it.packageName }
    ) { item ->
        AppUsageEntry(
            playAnimation = true,
            modifier = Modifier.animateItem(),
            appUsageInfo = item,
            onEntryClick = { onAppUsageInfoClick(item) },
            onTimeSettingsClick = {
                onAppTimeLimitSettingsClicked(item.packageName)
                onShowLimitDialog()
            }
        )
    }
}