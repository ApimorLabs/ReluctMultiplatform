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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.apimorlabs.reluct.navigation.NavHelpers.navigateNavBarElements
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReluctBottomNavBar(
    navController: NavHostController,
    graphStartDestination: String,
    modifier: Modifier = Modifier,
) {
    val items = NavBarDestinations.entries
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = NavBarElevation.Level2
    ) {
        FloatingNavigationBar(
            modifier = modifier.navigationBarsPadding(),
            containerColor = Color.Transparent
        ) {
            items.forEach { item ->
                val selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } ?: false

                NavigationBarItem(
                    label = {
                        Text(
                            text = stringResource(item.label),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    selected = selected,
                    // colors = ,
                    icon = {
                        Icon(
                            imageVector = if (selected) item.iconActive else item.iconInactive,
                            contentDescription = stringResource(item.label)
                        )
                    },
                    onClick = {
                        navController.navigateNavBarElements(
                            route = item.route,
                            startDestination = graphStartDestination
                        )
                    }
                )
            }
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
