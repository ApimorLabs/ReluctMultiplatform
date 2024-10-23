package com.apimorlabs.reluct.navigation.navhost.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.screen_time_destination_text
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.navigation.NavHelpers.navigateNavBarElements
import com.apimorlabs.reluct.navigation.destinations.AppScreenTimeStatsDestination
import com.apimorlabs.reluct.navigation.destinations.ScreenTimeLimitsDestination
import com.apimorlabs.reluct.navigation.destinations.ScreenTimeStatsDestination
import com.apimorlabs.reluct.navigation.scaleInEnterTransition
import com.apimorlabs.reluct.navigation.scaleInPopEnterTransition
import com.apimorlabs.reluct.navigation.scaleOutExitTransition
import com.apimorlabs.reluct.navigation.scaleOutPopExitTransition
import com.apimorlabs.reluct.navigation.toptabs.TopTabBar
import com.apimorlabs.reluct.navigation.toptabs.rememberScreenTimeTabs
import com.apimorlabs.reluct.screens.screentime.limits.ScreenTimeLimitsScreen
import com.apimorlabs.reluct.screens.screentime.statistics.ScreenTimeStatisticsScreen
import org.jetbrains.compose.resources.stringResource

@ExperimentalAnimationApi
@Composable
internal fun ScreenTimeNavHost(
    mainNavController: NavHostController,
    barsVisibility: BarsVisibility,
    mainScaffoldPadding: PaddingValues,
    navController: NavHostController = rememberNavController(),
) {
    val destinations = rememberScreenTimeTabs()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val tabPage = remember(currentDestination) {
        derivedStateOf {
            com.apimorlabs.reluct.navigation.toptabs.getCurrentTab(currentDestination, destinations)
        }
    }
    val startDestination by remember(destinations) {
        derivedStateOf { destinations.first().destination.route }
    }

    Scaffold(
        topBar = {
            TopTabBar(
                title = stringResource(Res.string.screen_time_destination_text),
                tabPage = tabPage,
                tabs = destinations,
                updateTabPage = {
                    navController.navigateNavBarElements(it.route, startDestination)
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = ScreenTimeStatsDestination
        ) {
            // Statistics
            composable<ScreenTimeStatsDestination>(
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                ScreenTimeStatisticsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAppUsageInfo = { packageName ->
                        mainNavController.navigate(AppScreenTimeStatsDestination(packageName))
                    }
                )
            }

            // Limits
            composable<ScreenTimeLimitsDestination>(
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                ScreenTimeLimitsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAppUsageInfo = { packageName ->
                        mainNavController.navigate(AppScreenTimeStatsDestination(packageName))
                    }
                )
            }
        }
    }
}
