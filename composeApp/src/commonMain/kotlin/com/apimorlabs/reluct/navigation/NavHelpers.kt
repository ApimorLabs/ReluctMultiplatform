package com.apimorlabs.reluct.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

internal object NavHelpers {
    fun getStringArgs(backStackEntry: NavBackStackEntry, key: String?): String? {
        val arg = backStackEntry.arguments?.getString(key)
        return if (arg == "null") null else arg
    }

    /** Useful when destination is launched from a deeplink and no backstack is present **/
    // This is Android only
    /*fun NavHostController.popBackStackOrExitActivity(activity: Activity) {
        if (!popBackStack()) {
            activity.finish()
        }
    }*/

    /**
     * Navigates Nav bar elements that can be the Top or Bottom Navbar while making sure only one
     * destination instance is on the stack and pop unwanted destinations such that it always
     * returns to start destination when you pop the current destination
     * Make [startDestination] null if you don't want to have a start destination
     */
    fun <T: Any, V: Any> NavHostController.navigateNavBarElements(route: T, startDestination: V?) {
        navigate(route) {
            popUpTo(graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navigate(route = route) {
            val popDestination = startDestination ?: graph.findStartDestination().route
            popDestination?.let { dest ->
                popUpTo(dest) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
