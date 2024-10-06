package com.apimorlabs.reluct.navigation.navhost.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.topBar.ReluctPageHeading
import com.apimorlabs.reluct.compose.ui.dashboard_destination_text
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.navigation.NavHelpers.navigateNavBarElements
import com.apimorlabs.reluct.navigation.destinations.AppScreenTimeStatsDestination
import com.apimorlabs.reluct.navigation.destinations.DashboardOverviewDestination
import com.apimorlabs.reluct.navigation.destinations.DashboardStatsDestination
import com.apimorlabs.reluct.navigation.destinations.GoalDetailsDestination
import com.apimorlabs.reluct.navigation.destinations.SettingsDestination
import com.apimorlabs.reluct.navigation.destinations.TaskDetailsDestination
import com.apimorlabs.reluct.navigation.navbar.NavBarDestinations
import com.apimorlabs.reluct.navigation.scaleInEnterTransition
import com.apimorlabs.reluct.navigation.scaleInPopEnterTransition
import com.apimorlabs.reluct.navigation.scaleOutExitTransition
import com.apimorlabs.reluct.navigation.scaleOutPopExitTransition
import com.apimorlabs.reluct.navigation.toptabs.TabBar
import com.apimorlabs.reluct.navigation.toptabs.TabDestinations
import com.apimorlabs.reluct.navigation.toptabs.getCurrentTab
import com.apimorlabs.reluct.navigation.toptabs.rememberDashboardTabs
import com.apimorlabs.reluct.screens.dashboard.overview.DashboardOverviewScreen
import com.apimorlabs.reluct.screens.dashboard.statistics.DashboardStatsScreen
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@ExperimentalAnimationApi
@Composable
internal fun DashboardNavHost(
    mainNavController: NavHostController,
    barsVisibility: BarsVisibility,
    mainScaffoldPadding: PaddingValues,
    navController: NavHostController = rememberNavController(),
) {
    val destinations = rememberDashboardTabs()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val tabPage = remember(currentDestination) {
        derivedStateOf {
            getCurrentTab(currentDestination, destinations)
        }
    }
    val startDestination by remember(destinations) {
        derivedStateOf { destinations.first().destination.route }
    }

    Scaffold(
        topBar = {
            DashboardScreenTopBar(
                tabPage = tabPage,
                tabs = destinations,
                // profilePicUrl = "https://pbs.twimg.com/profile_images/1451052243067805698/LIEt076e_400x400.jpg",
                updateTabPage = {
                    navController.navigateNavBarElements(it.route, startDestination)
                },
                onSettingsClicked = {
                    mainNavController.navigate(SettingsDestination)
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = startDestination
        ) {
            // Overview
            composable<DashboardOverviewDestination>(
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                DashboardOverviewScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToScreenTime = {
                        mainNavController.navigateNavBarElements(
                            route = NavBarDestinations.ScreenTime.route,
                            startDestination = null
                        )
                    },
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(TaskDetailsDestination(it))
                    },
                    onNavigateToGoalDetails = { goalId: String? ->
                        mainNavController.navigate(GoalDetailsDestination(goalId))
                    }
                )
            }

            // Dashboard Stats
            composable<DashboardStatsDestination>(
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                DashboardStatsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAppUsageInfo = { packageName ->
                        mainNavController.navigate(AppScreenTimeStatsDestination(packageName))
                    },
                    onNavigateToScreenTimeStats = {
                        mainNavController.navigateNavBarElements(
                            route = NavBarDestinations.ScreenTime.route,
                            startDestination = null
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun DashboardScreenTopBar(
    tabPage: State<TabDestinations<out Any>>,
    tabs: ImmutableList<TabBar<out Any>>,
    // profilePicUrl: String?,
    updateTabPage: (TabDestinations<out Any>) -> Unit,
    onSettingsClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .statusBarsPadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        ReluctPageHeading(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(Res.string.dashboard_destination_text),
            extraItems = {
                IconButton(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        CircleShape
                    ),
                    onClick = onSettingsClicked,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings"
                    )
                }
                /*ProfilePicture(
                    modifier = Modifier,//.padding(4.dp),
                    pictureUrl = profilePicUrl,
                    size = 36.dp
                )*/
            }
        )

        LazyRow {
            item {
                TabBar(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    selectedTabPage = tabPage,
                    tabs = tabs,
                    onTabSelected = { updateTabPage(it) }
                )
            }
        }
    }
}
