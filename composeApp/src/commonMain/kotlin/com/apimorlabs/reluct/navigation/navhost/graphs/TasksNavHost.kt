package com.apimorlabs.reluct.navigation.navhost.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
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
import com.apimorlabs.reluct.compose.ui.components.textFields.search.PlaceholderMaterialSearchBar
import com.apimorlabs.reluct.compose.ui.search_tasks_hint_text
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.navigation.NavHelpers.navigateNavBarElements
import com.apimorlabs.reluct.navigation.destinations.AddEditTaskDestination
import com.apimorlabs.reluct.navigation.destinations.CompletedTasksDestination
import com.apimorlabs.reluct.navigation.destinations.PendingTasksDestination
import com.apimorlabs.reluct.navigation.destinations.SearchTasksDestination
import com.apimorlabs.reluct.navigation.destinations.TaskDetailsDestination
import com.apimorlabs.reluct.navigation.destinations.TasksStatisticsDestination
import com.apimorlabs.reluct.navigation.scaleInEnterTransition
import com.apimorlabs.reluct.navigation.scaleInPopEnterTransition
import com.apimorlabs.reluct.navigation.scaleOutExitTransition
import com.apimorlabs.reluct.navigation.scaleOutPopExitTransition
import com.apimorlabs.reluct.navigation.toptabs.TabBar
import com.apimorlabs.reluct.navigation.toptabs.TabDestinations
import com.apimorlabs.reluct.navigation.toptabs.rememberTasksTabs
import com.apimorlabs.reluct.screens.tasks.completed.CompletedTasksScreen
import com.apimorlabs.reluct.screens.tasks.pending.PendingTasksScreen
import com.apimorlabs.reluct.screens.tasks.statistics.TasksStatisticsScreen
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@ExperimentalAnimationApi
@Composable
internal fun TasksNavHost(
    mainNavController: NavHostController,
    barsVisibility: BarsVisibility,
    mainScaffoldPadding: PaddingValues,
    navController: NavHostController = rememberNavController(),
) {
    val destinations = rememberTasksTabs()
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
            TasksScreenTopBar(
                tabPage = tabPage,
                tabs = destinations,
                navigateToSearch = { mainNavController.navigate(SearchTasksDestination) },
                updateTabPage = {
                    navController.navigateNavBarElements(it.route, startDestination)
                },
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPapping ->
        NavHost(
            modifier = Modifier.padding(innerPapping),
            navController = navController,
            startDestination = PendingTasksDestination
        ) {
            // Tasks
            composable<PendingTasksDestination>(
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                PendingTasksScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAddTask = {
                        mainNavController.navigate(AddEditTaskDestination(it))
                    },
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(TaskDetailsDestination(it))
                    }
                )
            }

            // Done - Completed Tasks
            composable<CompletedTasksDestination>(
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                CompletedTasksScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAddTask = {
                        mainNavController.navigate(AddEditTaskDestination(it))
                    },
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(TaskDetailsDestination(it))
                    }
                )
            }
            // Statistics
            composable<TasksStatisticsDestination>(
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                TasksStatisticsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(TaskDetailsDestination(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun TasksScreenTopBar(
    tabPage: State<TabDestinations<out Any>>,
    tabs: ImmutableList<TabBar<out Any>>,
    navigateToSearch: () -> Unit,
    updateTabPage: (TabDestinations<out Any>) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .statusBarsPadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        PlaceholderMaterialSearchBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            hint = stringResource(Res.string.search_tasks_hint_text),
            onClick = navigateToSearch
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
