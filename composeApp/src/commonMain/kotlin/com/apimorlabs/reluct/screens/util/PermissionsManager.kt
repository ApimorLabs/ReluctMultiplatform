package com.apimorlabs.reluct.screens.util

import androidx.compose.runtime.Composable

interface PermissionsManager {
    fun checkUsageAccessPermission(): Boolean

    fun checkCanScheduleAlarms(): Boolean

    fun checkNotificationPermission(): Boolean

    fun openAppNotificationSettings()

    fun requestUsageAccessPermission()
}

@Composable
expect fun GetPermissionsManager(
    onPermissionsManager: (PermissionsManager) -> Unit
)
