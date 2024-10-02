package com.apimorlabs.reluct.screens.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

class PermissionsManagerDesktop : PermissionsManager {
    override fun checkUsageAccessPermission(): Boolean = true

    override fun checkCanScheduleAlarms(): Boolean = true

    override fun checkNotificationPermission(): Boolean = true

    override fun openAppNotificationSettings() {
        return
    }

    override fun requestUsageAccessPermission() {
        return
    }

}

@Composable
actual fun GetPermissionsManager(
    onPermissionsManager: (PermissionsManager) -> Unit
) {
    val desktop = remember { PermissionsManagerDesktop() }
    LaunchedEffect(Unit) { onPermissionsManager(desktop) }
}
