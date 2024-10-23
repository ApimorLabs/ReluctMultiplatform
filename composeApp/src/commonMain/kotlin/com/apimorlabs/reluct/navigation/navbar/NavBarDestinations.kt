package com.apimorlabs.reluct.navigation.navbar

import kotlinx.serialization.Serializable

@Serializable
object DashBoardDestinations

@Serializable
object TasksDestinations

@Serializable
object ScreenTimeDestinations

@Serializable
object GoalsDestinations

@Serializable
sealed class NavBarDestinations<T>(val route: T) {
    @Serializable
    data object Dashboard : NavBarDestinations<DashBoardDestinations>(DashBoardDestinations)

    @Serializable
    data object Tasks : NavBarDestinations<TasksDestinations>(TasksDestinations)

    @Serializable
    data object ScreenTime : NavBarDestinations<ScreenTimeDestinations>(ScreenTimeDestinations)

    @Serializable
    data object Goals : NavBarDestinations<GoalsDestinations>(GoalsDestinations)
}
