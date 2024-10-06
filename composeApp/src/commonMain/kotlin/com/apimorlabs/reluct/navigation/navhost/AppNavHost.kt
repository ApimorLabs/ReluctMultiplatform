package com.apimorlabs.reluct.navigation.navhost

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apimorlabs.reluct.compose.ui.components.animations.slideInVerticallyFadeReversed
import com.apimorlabs.reluct.compose.ui.components.animations.slideOutVerticallyFadeReversed
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.compose.ui.util.rememberBarVisibility
import com.apimorlabs.reluct.navigation.destinations.OnBoardingDestination
import com.apimorlabs.reluct.navigation.destinations.PendingTasksLink
import com.apimorlabs.reluct.navigation.destinations.SettingsCheck
import com.apimorlabs.reluct.navigation.destinations.SettingsDestination
import com.apimorlabs.reluct.navigation.navbar.DashBoardDestinations
import com.apimorlabs.reluct.navigation.navbar.GoalsDestinations
import com.apimorlabs.reluct.navigation.navbar.NavBarDestinations
import com.apimorlabs.reluct.navigation.navbar.ReluctBottomNavBar
import com.apimorlabs.reluct.navigation.navbar.ScreenTimeDestinations
import com.apimorlabs.reluct.navigation.navbar.TasksDestinations
import com.apimorlabs.reluct.navigation.navhost.graphs.DashboardNavHost
import com.apimorlabs.reluct.navigation.navhost.graphs.GoalsNavHost
import com.apimorlabs.reluct.navigation.navhost.graphs.ScreenTimeNavHost
import com.apimorlabs.reluct.navigation.navhost.graphs.TasksNavHost
import com.apimorlabs.reluct.navigation.navhost.graphs.otherScreenNavGraph
import com.apimorlabs.reluct.navigation.scaleInEnterTransition
import com.apimorlabs.reluct.navigation.scaleInPopEnterTransition
import com.apimorlabs.reluct.navigation.scaleOutExitTransition
import com.apimorlabs.reluct.navigation.scaleOutPopExitTransition
import com.apimorlabs.reluct.screens.onboarding.OnBoardingScreen
import com.apimorlabs.reluct.screens.settings.SettingsScreen
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

// Route used specifically for checking if the On Boarding flow was shown and User Signed In
private const val CHECKING_ROUTE = "dummy_checking_route"

@OptIn(ExperimentalAnimationApi::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AppNavHost(settingsCheck: State<SettingsCheck?>, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val barsVisibility = rememberBarVisibility(defaultBottomBar = false)

    val mainPadding = PaddingValues(
        bottom = Dimens.ExtraLargePadding.size + Dimens.MediumPadding.size +
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    )

    // Haze blur effect
    val hazeState = remember { HazeState() }

    /**
     * Don't use the bottomBar param of Scaffold for the AppBottomBar
     * It will cause the whole screen to recompose when you hide/show the bottomBar
     * with [BarsVisibility] since the innerPadding changes during this transition
     * The AppBottomBar is provided to Box inside of this Scaffold and the padding needed by inner
     * items is mainPadding from above
     */
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            NavHost(
                modifier = Modifier,
                navController = navController,
                startDestination = CHECKING_ROUTE,
                route = "root"
            ) {
                // Checking Route
                composable(
                    route = CHECKING_ROUTE,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                    popEnterTransition = { fadeIn() },
                    popExitTransition = { fadeOut() }
                ) {
                    HandleRouteChecks(settingsCheck, navController)

                    Box(
                        modifier = Modifier
                            .statusBarsPadding()
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    SideEffect { barsVisibility.bottomBar.hide() }
                }

                // OnBoarding
                composable<OnBoardingDestination>(
                    enterTransition = { scaleInEnterTransition() },
                    exitTransition = { scaleOutExitTransition() },
                    popEnterTransition = { scaleInPopEnterTransition() },
                    popExitTransition = { scaleOutPopExitTransition() }
                ) {
                    OnBoardingScreen()

                    SideEffect { barsVisibility.bottomBar.hide() }
                }

                // Dashboard
                composable<DashBoardDestinations>(
                    enterTransition = { scaleInEnterTransition() },
                    exitTransition = { scaleOutExitTransition() },
                    popEnterTransition = { scaleInPopEnterTransition() },
                    popExitTransition = { scaleOutPopExitTransition() }
                ) {
                    DashboardNavHost(
                        mainNavController = navController,
                        barsVisibility = barsVisibility,
                        mainScaffoldPadding = mainPadding
                    )
                }

                // Tasks
                composable<TasksDestinations>(
                    deepLinks = listOf(NavDeepLink(PendingTasksLink.DEEP_LINK)),
                    enterTransition = { scaleInEnterTransition() },
                    exitTransition = { scaleOutExitTransition() },
                    popEnterTransition = { scaleInPopEnterTransition() },
                    popExitTransition = { scaleOutPopExitTransition() }
                ) {
                    TasksNavHost(
                        mainNavController = navController,
                        mainScaffoldPadding = mainPadding,
                        barsVisibility = barsVisibility
                    )
                }

                // Screen Time
                composable<ScreenTimeDestinations>(
                    enterTransition = { scaleInEnterTransition() },
                    exitTransition = { scaleOutExitTransition() },
                    popEnterTransition = { scaleInPopEnterTransition() },
                    popExitTransition = { scaleOutPopExitTransition() }
                ) {
                    ScreenTimeNavHost(
                        mainNavController = navController,
                        barsVisibility = barsVisibility,
                        mainScaffoldPadding = mainPadding
                    )
                }

                // Goals
                composable<GoalsDestinations>(
                    enterTransition = { scaleInEnterTransition() },
                    exitTransition = { scaleOutExitTransition() },
                    popEnterTransition = { scaleInPopEnterTransition() },
                    popExitTransition = { scaleOutPopExitTransition() }
                ) {
                    GoalsNavHost(
                        mainNavController = navController,
                        barsVisibility = barsVisibility,
                        mainScaffoldPadding = mainPadding
                    )
                }

                // Settings
                composable<SettingsDestination>(
                    enterTransition = { scaleInEnterTransition() },
                    exitTransition = { scaleOutExitTransition() },
                    popEnterTransition = { scaleInPopEnterTransition() },
                    popExitTransition = { scaleOutPopExitTransition() }
                ) {
                    barsVisibility.bottomBar.hide()

                    SettingsScreen(
                        onNavigateToManageAcc = { },
                        goBack = navController::popBackStack
                    )
                }

                // All Other screens that don't share Scaffolds
                otherScreenNavGraph(
                    navController = navController,
                    barsVisibility = barsVisibility
                )
            }

            // Bottom Bar
            AppBottomBar(
                modifier = Modifier.hazeChild(
                    state = hazeState,
                    style = HazeMaterials.regular()
                ),
                navController = navController,
                barsVisibility = barsVisibility
            )
        }
    }
}

@Composable
private fun BoxScope.AppBottomBar(
    navController: NavHostController,
    barsVisibility: BarsVisibility,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        modifier = modifier.align(Alignment.BottomCenter),
        visible = barsVisibility.bottomBar.isVisible,
        enter = slideInVerticallyFadeReversed(),
        exit = slideOutVerticallyFadeReversed()
    ) {
        ReluctBottomNavBar(
            navController = navController,
            startDestination = NavBarDestinations.Dashboard
        )
    }
}

@Composable
private fun HandleRouteChecks(
    settingsCheck: State<SettingsCheck?>,
    navController: NavHostController
) {
    LaunchedEffect(settingsCheck.value) {
        settingsCheck.value?.let { check ->
            if (check.isOnBoardingDone && !check.showChangeLog) {
                // Everything is ok. Go to Dashboard
                navController.navigate(DashBoardDestinations) {
                    popUpTo(CHECKING_ROUTE) { inclusive = true }
                }
            } else if (!check.isOnBoardingDone) {
                // Show On boarding flow
                navController.navigate(OnBoardingDestination) {
                    popUpTo(CHECKING_ROUTE) { inclusive = true }
                }
            } else {
                // Show Changelog Screen
            }
        }
    }
}
