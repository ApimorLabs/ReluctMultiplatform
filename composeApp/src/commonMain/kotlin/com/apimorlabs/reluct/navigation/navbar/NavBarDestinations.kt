package com.apimorlabs.reluct.navigation.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.automirrored.rounded.FactCheck
import androidx.compose.material.icons.outlined.Aod
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.rounded.Aod
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.dashboard_destination_text
import com.apimorlabs.reluct.compose.ui.goals_destination_text
import com.apimorlabs.reluct.compose.ui.screen_time_destination_text
import com.apimorlabs.reluct.compose.ui.tasks_destination_text
import org.jetbrains.compose.resources.StringResource

enum class NavBarDestinations(
    val route: String,
    val iconActive: ImageVector,
    val iconInactive: ImageVector,
    val label: StringResource
) {
    Dashboard(
        route = "dashboard_destination",
        iconActive = Icons.Rounded.GridView,
        iconInactive = Icons.Outlined.GridView,
        label = Res.string.dashboard_destination_text
    ),

    Tasks(
        route = "tasks_destination",
        iconActive = Icons.AutoMirrored.Rounded.FactCheck,
        iconInactive = Icons.AutoMirrored.Outlined.FactCheck,
        label = Res.string.tasks_destination_text
    ),

    ScreenTime(
        route = "screen_time_destination",
        iconActive = Icons.Rounded.Aod,
        iconInactive = Icons.Outlined.Aod,
        label = Res.string.screen_time_destination_text
    ),

    Goals(
        route = "goals_destination",
        iconActive = Icons.Rounded.TaskAlt,
        iconInactive = Icons.Outlined.TaskAlt,
        label = Res.string.goals_destination_text
    );
}
