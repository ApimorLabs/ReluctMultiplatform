package com.apimorlabs.reluct.navigation.navhost.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.SideEffect
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.navigation.destinations.AddEditGoalDestination
import com.apimorlabs.reluct.navigation.destinations.AddEditGoalLink
import com.apimorlabs.reluct.navigation.destinations.AddEditTaskDestination
import com.apimorlabs.reluct.navigation.destinations.AppScreenTimeStatsDestination
import com.apimorlabs.reluct.navigation.destinations.AppScreenTimeStatsLink
import com.apimorlabs.reluct.navigation.destinations.GoalDetailsDestination
import com.apimorlabs.reluct.navigation.destinations.GoalDetailsLink
import com.apimorlabs.reluct.navigation.destinations.OtherGraphDestination
import com.apimorlabs.reluct.navigation.destinations.SearchTasksDestination
import com.apimorlabs.reluct.navigation.destinations.TaskDetailsDestination
import com.apimorlabs.reluct.navigation.destinations.TaskDetailsLink
import com.apimorlabs.reluct.navigation.scaleInEnterTransition
import com.apimorlabs.reluct.navigation.scaleInPopEnterTransition
import com.apimorlabs.reluct.navigation.scaleOutExitTransition
import com.apimorlabs.reluct.navigation.scaleOutPopExitTransition
import com.apimorlabs.reluct.screens.goals.addEdit.AddEditGoalScreen
import com.apimorlabs.reluct.screens.goals.details.GoalDetailsScreen
import com.apimorlabs.reluct.screens.screentime.appStatsDetails.AppScreenTimeStatsScreen
import com.apimorlabs.reluct.screens.tasks.addEdit.AddEditTaskScreen
import com.apimorlabs.reluct.screens.tasks.details.TaskDetailsScreen
import com.apimorlabs.reluct.screens.tasks.search.TasksSearchScreen

@ExperimentalAnimationApi
fun NavGraphBuilder.otherScreenNavGraph(
    navController: NavHostController,
    barsVisibility: BarsVisibility
) {
    navigation<OtherGraphDestination>(
        startDestination = TaskDetailsDestination(null)
    ) {
        // Task Details
        composable<TaskDetailsDestination>(
            deepLinks = listOf(NavDeepLink(TaskDetailsLink.DEEP_LINK)),
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { navBackStackEntry ->

            val detailDest = navBackStackEntry.toRoute<TaskDetailsDestination>()
            TaskDetailsScreen(
                taskId = detailDest.taskId,
                onNavigateToEditTask = {
                    navController.navigate(
                        AddEditTaskDestination(it)
                    )
                },
                onBackClicked = { navController.popBackStack() }
            )

            SideEffect { barsVisibility.bottomBar.hide() }
        }

        // Add Task
        composable<AddEditTaskDestination>(
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { navBackStackEntry ->
            // Its safe to cast since this composable will always be inside an activity
            val addEditTaskDest = navBackStackEntry.toRoute<AddEditTaskDestination>()
            AddEditTaskScreen(
                taskId = addEditTaskDest.taskId,
                onBackClicked = { navController.popBackStack() }
            )

            SideEffect { barsVisibility.bottomBar.hide() }
        }

        // Task Search
        composable<SearchTasksDestination>(
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) {
            TasksSearchScreen(
                onNavigateToTaskDetails = {
                    navController.navigate(TaskDetailsDestination(it))
                },
                onBackClicked = { navController.popBackStack() }
            )

            SideEffect { barsVisibility.bottomBar.hide() }
        }

        // Goal Details
        composable<GoalDetailsDestination>(
            deepLinks = listOf(NavDeepLink(GoalDetailsLink.DEEP_LINK)),
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { backStackEntry ->

            val goalDest = backStackEntry.toRoute<GoalDetailsDestination>()
            GoalDetailsScreen(
                goalId = goalDest.goalId,
                onExit = { navController.popBackStack() },
                onNavigateToEditGoal = { goalId ->
                    navController.navigate(
                        AddEditGoalDestination(goalId)
                    )
                }
            )

            SideEffect { barsVisibility.bottomBar.hide() }
        }

        // Add or Edit Goal
        composable<AddEditGoalDestination>(
            deepLinks = listOf(NavDeepLink(AddEditGoalLink.DEEP_LINK)),
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { backStackEntry ->

            val goalIDest = backStackEntry.toRoute<AddEditGoalDestination>()
            AddEditGoalScreen(
                goalId = goalIDest.goalId,
                defaultGoalIndex = goalIDest.defaultGoalIndex,
                onExit = { navController.popBackStack() }
            )

            SideEffect { barsVisibility.bottomBar.hide() }
        }

        // App Screen Time Stats
        composable<AppScreenTimeStatsDestination>(
            deepLinks = listOf(NavDeepLink(AppScreenTimeStatsLink.DEEP_LINK)),
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { backStackEntry ->

            val stats = backStackEntry.toRoute<AppScreenTimeStatsDestination>()
            AppScreenTimeStatsScreen(
                packageName = stats.packageName ?: "",
                onBackClicked = { navController.popBackStack() }
            )

            SideEffect { barsVisibility.bottomBar.hide() }
        }
    }
}
