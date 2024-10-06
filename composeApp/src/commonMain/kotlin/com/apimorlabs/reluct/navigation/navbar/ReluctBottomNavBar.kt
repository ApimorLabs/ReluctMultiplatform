package com.apimorlabs.reluct.navigation.navbar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.automirrored.rounded.FactCheck
import androidx.compose.material.icons.outlined.Aod
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.rounded.Aod
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.dashboard_destination_text
import com.apimorlabs.reluct.compose.ui.goals_destination_text
import com.apimorlabs.reluct.compose.ui.screen_time_destination_text
import com.apimorlabs.reluct.compose.ui.tasks_destination_text
import com.apimorlabs.reluct.navigation.NavHelpers.navigateNavBarElements
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@Stable
private data class NavDestination<T>(
    val destination: NavBarDestinations<T>,
    val iconActive: ImageVector,
    val iconInactive: ImageVector,
    val label: String
)

@Composable
fun <T> ReluctBottomNavBar(
    navController: NavHostController,
    startDestination: NavBarDestinations<T>,
    modifier: Modifier = Modifier,
) {
    // Title Texts
    val dashboardTitle = stringResource(Res.string.dashboard_destination_text)
    val tasksTitle = stringResource(Res.string.tasks_destination_text)
    val screenTimeTitle = stringResource(Res.string.screen_time_destination_text)
    val goalsTitle = stringResource(Res.string.goals_destination_text)

    val items = remember {
        persistentListOf(
            NavDestination(
                destination = NavBarDestinations.Dashboard,
                iconActive = Icons.Rounded.GridView,
                iconInactive = Icons.Outlined.GridView,
                label = dashboardTitle
            ),
            NavDestination(
                destination = NavBarDestinations.Tasks,
                iconActive = Icons.AutoMirrored.Rounded.FactCheck,
                iconInactive = Icons.AutoMirrored.Outlined.FactCheck,
                label = tasksTitle
            ),
            NavDestination(
                destination = NavBarDestinations.ScreenTime,
                iconActive = Icons.Rounded.Aod,
                iconInactive = Icons.Outlined.Aod,
                label = screenTimeTitle
            ),
            NavDestination(
                destination = NavBarDestinations.Goals,
                iconActive = Icons.Rounded.TaskAlt,
                iconInactive = Icons.Outlined.TaskAlt,
                label = goalsTitle
            )
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    FloatingNavigationBar(
        modifier = modifier.navigationBarsPadding(),
        containerColor = Color.Transparent
    ) {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any {
                it.route == item.destination.route::class.qualifiedName
            } ?: false

            NavigationBarItem(
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                selected = selected,
                // colors = ,
                icon = {
                    Icon(
                        imageVector = if (selected) item.iconActive else item.iconInactive,
                        contentDescription = item.label
                    )
                },
                onClick = {
                    navController.navigateNavBarElements(
                        route = item.destination.route,
                        startDestination.route
                    )
                }
            )
        }
    }
}

@Composable
fun FloatingNavigationBar(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shape = shape,
        border = BorderStroke(
            width = 0.5.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                ),
            ),
        ),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .height(80.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
    }
}
