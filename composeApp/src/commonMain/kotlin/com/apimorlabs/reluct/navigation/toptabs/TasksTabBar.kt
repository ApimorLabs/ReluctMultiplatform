package com.apimorlabs.reluct.navigation.toptabs

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.tab.ReluctTabIndicator
import com.apimorlabs.reluct.compose.ui.components.tab.TabEntry
import com.apimorlabs.reluct.compose.ui.tasks_done
import com.apimorlabs.reluct.compose.ui.tasks_pending
import com.apimorlabs.reluct.compose.ui.tasks_stats
import org.jetbrains.compose.resources.stringResource

@Composable
fun TasksTabBar(
    tabPage: State<TasksTabDestination>,
    onTabSelected: (tabPage: TasksTabDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        modifier = modifier.width(300.dp),
        selectedTabIndex = tabPage.value.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            ReluctTabIndicator(currentTabPosition = tabPositions[tabPage.value.ordinal])
        },
        divider = { }
    ) {
        TabEntry(
            title = stringResource(Res.string.tasks_pending),
            textColor = getTasksTabTextColor(
                tabPage = TasksTabDestination.Tasks,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(TasksTabDestination.Tasks)
            }
        )
        TabEntry(
            title = stringResource(Res.string.tasks_done),
            textColor = getTasksTabTextColor(
                tabPage = TasksTabDestination.Done,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(TasksTabDestination.Done)
            }
        )
        TabEntry(
            title = stringResource(Res.string.tasks_stats),
            textColor = getTasksTabTextColor(
                tabPage = TasksTabDestination.Statistics,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(TasksTabDestination.Statistics)
            }
        )
    }
}

@Composable
private fun getTasksTabTextColor(
    tabPage: TasksTabDestination,
    selectedTabPage: TasksTabDestination,
): Color =
    if (tabPage == selectedTabPage) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
            .copy(alpha = .5f)
    }

enum class TasksTabDestination {
    Tasks, Done, Statistics;
}
