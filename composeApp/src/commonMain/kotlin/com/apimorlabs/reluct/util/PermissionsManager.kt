package com.apimorlabs.reluct.util

import androidx.compose.runtime.Composable

interface PermissionsManager {
    fun checkUsageAccessPermission(): Boolean

    fun checkCanScheduleAlarms(): Boolean

    fun checkNotificationPermission(): Boolean

    fun openAppNotificationSettings()

    fun requestUsageAccessPermission()

    fun isNotificationPermissionRequired(): Boolean

    fun requestOverlayPermission(msgTxt: String)

    fun requestScheduleAlarmsPermission()

    fun requestNotificationPermission()
}

@Composable
expect fun GetPermissionsManager(
    onPermissionsManager: (PermissionsManager) -> Unit
)
