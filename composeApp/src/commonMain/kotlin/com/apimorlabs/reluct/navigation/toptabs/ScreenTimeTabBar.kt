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
import com.apimorlabs.reluct.compose.ui.screen_time_limit
import com.apimorlabs.reluct.compose.ui.screen_time_stats
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScreenTimeTabBar(
    tabPage: State<ScreenTimeTabDestination>,
    onTabSelected: (tabPage: ScreenTimeTabDestination) -> Unit,
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
            title = stringResource(Res.string.screen_time_stats),
            textColor = getScreenTimeTabTextColor(
                tabPage = ScreenTimeTabDestination.Statistics,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(ScreenTimeTabDestination.Statistics)
            }
        )

        TabEntry(
            title = stringResource(Res.string.screen_time_limit),
            textColor = getScreenTimeTabTextColor(
                tabPage = ScreenTimeTabDestination.Limits,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(ScreenTimeTabDestination.Limits)
            }
        )
    }
}

@Composable
private fun getScreenTimeTabTextColor(
    tabPage: ScreenTimeTabDestination,
    selectedTabPage: ScreenTimeTabDestination,
): Color =
    if (tabPage == selectedTabPage) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
            .copy(alpha = .5f)
    }

enum class ScreenTimeTabDestination {
    Statistics, Limits;
}
