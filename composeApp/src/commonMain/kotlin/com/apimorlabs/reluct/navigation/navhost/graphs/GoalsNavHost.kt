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
import com.apimorlabs.reluct.compose.ui.goals_destination_text
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.navigation.NavHelpers.navigateNavBarElements
import com.apimorlabs.reluct.navigation.destinations.ActiveGoalsDestination
import com.apimorlabs.reluct.navigation.destinations.AddEditGoalDestination
import com.apimorlabs.reluct.navigation.destinations.GoalDetailsDestination
import com.apimorlabs.reluct.navigation.destinations.InactiveGoalsDestination
import com.apimorlabs.reluct.navigation.scaleInEnterTransition
import com.apimorlabs.reluct.navigation.scaleInPopEnterTransition
import com.apimorlabs.reluct.navigation.scaleOutExitTransition
import com.apimorlabs.reluct.navigation.scaleOutPopExitTransition
import com.apimorlabs.reluct.navigation.toptabs.TopTabBar
import com.apimorlabs.reluct.navigation.toptabs.getCurrentTab
import com.apimorlabs.reluct.navigation.toptabs.rememberGoalsTabs
import com.apimorlabs.reluct.screens.goals.active.ActiveGoalsScreen
import com.apimorlabs.reluct.screens.goals.inactive.InactiveGoalsScreen
import org.jetbrains.compose.resources.stringResource

@ExperimentalAnimationApi
@Composable
internal fun GoalsNavHost(
    mainNavController: NavHostController,
    barsVisibility: BarsVisibility,
    mainScaffoldPadding: PaddingValues,
    navController: NavHostController = rememberNavController(),
) {
    val destinations = rememberGoalsTabs()
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
            TopTabBar(
                title = stringResource(Res.string.goals_destination_text),
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
            startDestination = startDestination
        ) {
            // Active
            composable<ActiveGoalsDestination>(
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                ActiveGoalsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAddGoal = { defaultGoalIndex: Int? ->
                        mainNavController.navigate(
                            AddEditGoalDestination(
                                goalId = null,
                                defaultGoalIndex = defaultGoalIndex
                            )
                        )
                    },
                    onNavigateToGoalDetails = { goalId: String? ->
                        mainNavController.navigate(GoalDetailsDestination(goalId))
                    }
                )
            }

            // Inactive
            composable<InactiveGoalsDestination>(
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                InactiveGoalsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAddGoal = { defaultGoalIndex: Int? ->
                        mainNavController.navigate(
                            AddEditGoalDestination(
                                goalId = null,
                                defaultGoalIndex = defaultGoalIndex
                            )
                        )
                    },
                    onNavigateToGoalDetails = { goalId: String? ->
                        mainNavController.navigate(GoalDetailsDestination(goalId))
                    }
                )
            }
        }
    }
}
