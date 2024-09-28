package com.apimorlabs.reluct.features.screenTime.ui.overlay

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.AppBlocking
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.usageStats.AppUsageStats
import com.apimorlabs.reluct.common.models.util.time.Week
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.cards.cardWithActions.ReluctDescriptionCard
import com.apimorlabs.reluct.compose.ui.components.statistics.ChartData
import com.apimorlabs.reluct.compose.ui.components.statistics.charts.screenTime.ScreenTimeStatisticsCard
import com.apimorlabs.reluct.compose.ui.components.statistics.helpers.BarChartDefaults
import com.apimorlabs.reluct.compose.ui.exit_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.features.screenTime.states.AppSettingsState
import com.apimorlabs.reluct.features.screenTime.states.DailyAppUsageStatsState
import com.apimorlabs.reluct.features.screenTime.states.WeeklyAppUsageStatsState
import com.apimorlabs.reluct.features.screenTime.statistics.AppScreenTimeStatsViewModel
import com.apimorlabs.reluct.features.screenTime.ui.components.AppNameEntry
import com.apimorlabs.reluct.system.services.util.IconUtils.toBitmap
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
internal fun AppLimitedOverlayUI(
    viewModel: AppScreenTimeStatsViewModel,
    focusModeOn: Boolean,
    exit: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val barColor = BarChartDefaults.barColor
    val barChartData = getWeeklyAppScreenTimeChartData(
        weeklyStatsProvider = { uiState.weeklyData.usageStats },
        isLoadingProvider = { uiState.weeklyData is WeeklyAppUsageStatsState.Loading },
        barColor = barColor
    )

    val appSettingsData by remember(uiState.appSettingsState) {
        derivedStateOf {
            if (uiState.appSettingsState is AppSettingsState.Data) {
                uiState.appSettingsState as AppSettingsState.Data
            } else {
                null
            }
        }
    }

    val dailyDataState by remember(uiState.dailyData) {
        derivedStateOf {
            if (uiState.dailyData is DailyAppUsageStatsState.Data) {
                uiState.dailyData as DailyAppUsageStatsState.Data
            } else {
                null
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = .9f)),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
                .statusBarsPadding()
                // .navigationBarsPadding()
                .shadow(elevation = 6.dp, shape = Shapes.large)
                .background(MaterialTheme.colorScheme.background, Shapes.large),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(Dimens.MediumPadding.size),
            verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Name
            item {
                TopAppInfoItem(
                    modifier = Modifier.fillMaxWidth(),
                    dailyData = uiState.dailyData
                )
            }

            item {
                ReasonCard(
                    dailyDataState = dailyDataState,
                    appSettingsData = appSettingsData,
                    focusModeOn = focusModeOn
                )
            }

            // Chart
            val dailyData = uiState.dailyData
            item {
                /** No Button **/
                ScreenTimeStatisticsCard(
                    chartData = barChartData,
                    selectedDayText = {
                        if (dailyData is DailyAppUsageStatsState.Data) {
                            dailyData.dayText
                        } else {
                            "..."
                        }
                    },
                    selectedDayScreenTime = {
                        if (dailyData is DailyAppUsageStatsState.Data) {
                            dailyData.usageStat.appUsageInfo.formattedTimeInForeground
                        } else {
                            "..."
                        }
                    },
                    weeklyTotalScreenTime = { uiState.weeklyData.formattedTotalTime },
                    selectedDayIndex = { uiState.selectedInfo.selectedDay },
                    onBarClicked = { viewModel.selectDay(it) },
                    weekUpdateButton = { /** No Button **/ }
                )
            }

            item {
                ReluctButton(
                    modifier = Modifier.fillMaxWidth(.5f),
                    buttonText = org.jetbrains.compose.resources.stringResource(resource = Res.string.exit_text),
                    icon = Icons.AutoMirrored.Rounded.ExitToApp,
                    onButtonClicked = exit
                )
            }
        }

        // Close Button
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = Dimens.MediumPadding.size)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            onClick = exit,
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onPrimary,
                imageVector = Icons.Rounded.Close,
                contentDescription = null
            )
        }
    }
}

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
    val appIcon by remember(appInfo?.appName) {
        derivedStateOf {
            appInfo?.appIcon?.icon?.toBitmap()?.asImageBitmap()
        }
    }
    val appName by remember(appInfo?.appIcon) { derivedStateOf { appInfo?.appName } }

    if (appName != null && appIcon != null) {
        AppNameEntry(
            modifier = modifier,
            appName = appName!!,
            icon = appIcon!!,
            contentColor = MaterialTheme.colorScheme.onBackground,
            textStyle = MaterialTheme.typography.headlineSmall
        )
    } else {
        Text(
            modifier = modifier,
            text = "• • • • •",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ReasonCard(
    dailyDataState: DailyAppUsageStatsState.Data?,
    appSettingsData: AppSettingsState.Data?,
    focusModeOn: Boolean,
    modifier: Modifier = Modifier,
) {
    if (appSettingsData != null) {
        if (dailyDataState != null) {
            if (dailyDataState.usageStat.appUsageInfo.timeInForeground
                >= appSettingsData.appTimeLimit.timeInMillis &&
                appSettingsData.appTimeLimit.timeInMillis != 0L
            ) {
                // Show Time Limit Exceeded
                ReluctDescriptionCard(
                    modifier = modifier,
                    title = {
                        Text(
                            text = "Exceeded Screen Time Limit",
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalContentColor.current
                        )
                    },
                    description = {
                        Text(
                            text = "Your Limit: ${appSettingsData.appTimeLimit.formattedTime}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = .8f)
                        )
                    },
                    leftItems = {
                        Icon(
                            imageVector = Icons.Rounded.HourglassBottom,
                            contentDescription = null
                        )
                    },
                    onClick = { }
                )
            } else if (focusModeOn && appSettingsData.isDistractingApp) {
                // Show Focus Mode
                ReluctDescriptionCard(
                    modifier = modifier,
                    title = {
                        Text(
                            text = "Focus Mode Is On",
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalContentColor.current
                        )
                    },
                    description = {
                        Text(
                            text = "This is a Distracting App. Exit to focus more!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = .8f)
                        )
                    },
                    leftItems = {
                        Icon(
                            imageVector = Icons.Rounded.AppBlocking,
                            contentDescription = null
                        )
                    },
                    onClick = { }
                )
            } else {
                ReluctDescriptionCard(
                    modifier = modifier,
                    title = {
                        Text(
                            text = "App Paused",
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalContentColor.current
                        )
                    },
                    description = {
                        Text(
                            text = "App has been paused for the rest of the day!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = .8f)
                        )
                    },
                    leftItems = {
                        Icon(
                            imageVector = Icons.Rounded.PauseCircle,
                            contentDescription = null
                        )
                    },
                    onClick = { }
                )
            }
        }
    }
}

@Composable
private fun getWeeklyAppScreenTimeChartData(
    weeklyStatsProvider: () -> ImmutableMap<Week, AppUsageStats>,
    isLoadingProvider: () -> Boolean,
    barColor: Color,
): State<ChartData<BarParameters>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val weeklyStats = remember { derivedStateOf { weeklyStatsProvider() } }
    return produceState(
        initialValue = ChartData(isLoading = isLoading.value),
        isLoading.value,
        weeklyStats.value
    ) {
        val data = withContext(Dispatchers.IO) {
            persistentListOf<BarParameters>().builder().apply {
                for (key in weeklyStats.value.keys) {
                    val data = weeklyStats.value.getValue(key)
                    add(
                        BarParameters(
                            data = data.appUsageInfo.timeInForeground.toDouble(),
                            barColor = barColor,
                            dataName = key.dayAcronym,
                        )
                    )
                }
            }.build().toImmutableList()
        }
        value = ChartData(data = data, isLoading = isLoading.value)
    }
}
