package com.apimorlabs.reluct.navigation.toptabs

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.tab.ReluctTabIndicator
import com.apimorlabs.reluct.compose.ui.components.tab.TabEntry
import com.apimorlabs.reluct.compose.ui.dashboard_overview
import com.apimorlabs.reluct.compose.ui.dashboard_statistics
import com.apimorlabs.reluct.compose.ui.theme.ReluctAppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DashboardTabBar(
    tabPage: State<DashboardTabDestination>,
    onTabSelected: (tabPage: DashboardTabDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        modifier = modifier.width(250.dp),
        selectedTabIndex = tabPage.value.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            ReluctTabIndicator(currentTabPosition = tabPositions[tabPage.value.ordinal])
        },
        divider = { }
    ) {
        TabEntry(
            title = stringResource(Res.string.dashboard_overview),
            textColor = getDashboardTabTextColor(
                tabPage = DashboardTabDestination.Overview,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(DashboardTabDestination.Overview)
            }
        )
        TabEntry(
            title = stringResource(Res.string.dashboard_statistics),
            textColor = getDashboardTabTextColor(
                tabPage = DashboardTabDestination.Statistics,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(DashboardTabDestination.Statistics)
            }
        )
    }
}

@Preview
@Composable
private fun DashTabPrev() {
    ReluctAppTheme {
        val page = remember { mutableStateOf(DashboardTabDestination.Overview) }
        DashboardTabBar(
            tabPage = page,
            onTabSelected = {}
        )
    }
}

@Composable
private fun getDashboardTabTextColor(
    tabPage: DashboardTabDestination,
    selectedTabPage: DashboardTabDestination,
): Color =
    if (tabPage == selectedTabPage) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
            .copy(alpha = .5f)
    }

// Destinations
enum class DashboardTabDestination {
    Overview,
    Statistics;
}
