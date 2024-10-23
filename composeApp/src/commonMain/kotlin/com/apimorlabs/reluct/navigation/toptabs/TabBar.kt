package com.apimorlabs.reluct.navigation.toptabs

import androidx.compose.foundation.layout.width
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.apimorlabs.reluct.compose.ui.components.tab.ReluctTabIndicator
import com.apimorlabs.reluct.compose.ui.components.tab.TabEntry
import kotlinx.collections.immutable.ImmutableList

@Composable
fun TabBar(
    selectedTabPage: State<TabDestinations<out Any>>,
    tabs: ImmutableList<TabBar<out Any>>,
    onTabSelected: (tabPage: TabDestinations<out Any>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedIndex by remember(selectedTabPage, tabs) {
        derivedStateOf {
            tabs.indexOfFirst { it.destination == selectedTabPage.value }.takeIf { it >= 0 } ?: 0
        }
    }

    TabRow(
        modifier = modifier.width(250.dp),
        selectedTabIndex = selectedIndex,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            ReluctTabIndicator(currentTabPosition = tabPositions[selectedIndex])
        },
        divider = { }
    ) {
        tabs.forEachIndexed { _, tabPage ->
            TabEntry(
                title = tabPage.label,
                textColor = GetTabTextColor(
                    tabPage = tabPage.destination,
                    selectedTabPage = selectedTabPage.value
                ),
                onClick = { onTabSelected(tabPage.destination) }
            )
        }
    }
}

@Stable
data class TabBar<T>(val destination: TabDestinations<T>, val label: String)

internal fun getCurrentTab(
    currentDestination: NavDestination?,
    allTabs: ImmutableList<TabBar<out Any>>
): TabDestinations<out Any> {
    allTabs.forEach { item ->
        val isSelected = currentDestination?.hierarchy?.any {
            it.route == item.destination.route::class.qualifiedName
        } ?: false
        if (isSelected) return item.destination
    }
    return allTabs.first().destination
}
